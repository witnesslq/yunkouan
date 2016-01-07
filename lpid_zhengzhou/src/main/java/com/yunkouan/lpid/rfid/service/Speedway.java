package com.yunkouan.lpid.rfid.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.jdom.JDOMException;
import org.llrp.ltk.exceptions.InvalidLLRPMessageException;
import org.llrp.ltk.generated.custom.messages.IMPINJ_ENABLE_EXTENSIONS;
import org.llrp.ltk.generated.custom.messages.IMPINJ_ENABLE_EXTENSIONS_RESPONSE;
import org.llrp.ltk.generated.enumerations.AISpecStopTriggerType;
import org.llrp.ltk.generated.enumerations.AirProtocols;
import org.llrp.ltk.generated.enumerations.GetReaderCapabilitiesRequestedData;
import org.llrp.ltk.generated.enumerations.GetReaderConfigRequestedData;
import org.llrp.ltk.generated.enumerations.ROSpecStartTriggerType;
import org.llrp.ltk.generated.enumerations.ROSpecState;
import org.llrp.ltk.generated.enumerations.ROSpecStopTriggerType;
import org.llrp.ltk.generated.enumerations.StatusCode;
import org.llrp.ltk.generated.interfaces.SpecParameter;
import org.llrp.ltk.generated.messages.ADD_ROSPEC;
import org.llrp.ltk.generated.messages.ADD_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.messages.CLOSE_CONNECTION;
import org.llrp.ltk.generated.messages.CLOSE_CONNECTION_RESPONSE;
import org.llrp.ltk.generated.messages.ENABLE_ROSPEC;
import org.llrp.ltk.generated.messages.ENABLE_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.messages.GET_READER_CAPABILITIES;
import org.llrp.ltk.generated.messages.GET_READER_CAPABILITIES_RESPONSE;
import org.llrp.ltk.generated.messages.GET_READER_CONFIG;
import org.llrp.ltk.generated.messages.GET_READER_CONFIG_RESPONSE;
import org.llrp.ltk.generated.messages.READER_EVENT_NOTIFICATION;
import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;
import org.llrp.ltk.generated.messages.SET_READER_CONFIG;
import org.llrp.ltk.generated.messages.SET_READER_CONFIG_RESPONSE;
import org.llrp.ltk.generated.messages.START_ROSPEC;
import org.llrp.ltk.generated.messages.START_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.messages.STOP_ROSPEC;
import org.llrp.ltk.generated.messages.STOP_ROSPEC_RESPONSE;
import org.llrp.ltk.generated.parameters.AISpec;
import org.llrp.ltk.generated.parameters.AISpecStopTrigger;
import org.llrp.ltk.generated.parameters.AntennaConfiguration;
import org.llrp.ltk.generated.parameters.Custom;
import org.llrp.ltk.generated.parameters.EPCData;
import org.llrp.ltk.generated.parameters.EPC_96;
import org.llrp.ltk.generated.parameters.GeneralDeviceCapabilities;
import org.llrp.ltk.generated.parameters.InventoryParameterSpec;
import org.llrp.ltk.generated.parameters.RFTransmitter;
import org.llrp.ltk.generated.parameters.ROBoundarySpec;
import org.llrp.ltk.generated.parameters.ROSpec;
import org.llrp.ltk.generated.parameters.ROSpecStartTrigger;
import org.llrp.ltk.generated.parameters.ROSpecStopTrigger;
import org.llrp.ltk.generated.parameters.TagReportData;
import org.llrp.ltk.generated.parameters.TransmitPowerLevelTableEntry;
import org.llrp.ltk.generated.parameters.UHFBandCapabilities;
import org.llrp.ltk.net.LLRPConnection;
import org.llrp.ltk.net.LLRPConnectionAttemptFailedException;
import org.llrp.ltk.net.LLRPConnector;
import org.llrp.ltk.net.LLRPEndpoint;
import org.llrp.ltk.types.Bit;
import org.llrp.ltk.types.LLRPMessage;
import org.llrp.ltk.types.LLRPParameter;
import org.llrp.ltk.types.SignedShort;
import org.llrp.ltk.types.UnsignedByte;
import org.llrp.ltk.types.UnsignedInteger;
import org.llrp.ltk.types.UnsignedShort;
import org.llrp.ltk.types.UnsignedShortArray;
import org.llrp.ltk.util.Util;

import com.google.gson.Gson;
import com.yunkou.common.util.JsonUtil;
import com.yunkouan.lpid.atmosphere.config.SpringApplicationContext;
import com.yunkouan.lpid.biz.screenscompare.bo.RunningPackageModel;
import com.yunkouan.lpid.biz.screenscompare.bo.XrayImageModel;
import com.yunkouan.lpid.biz.screenscompare.service.ScreensCompareHistoryService;
import com.yunkouan.lpid.commons.util.Constant;

public class Speedway implements LLRPEndpoint, Runnable {
	private ScreensCompareHistoryService historyService;

	private String rfidIP ;
	// 当前类所在路径
	private String currentPath;
	private LLRPConnection connection;

	private static Logger logger = Logger.getLogger(Speedway.class);
	private ROSpec rospec;
	private int MessageID = 23; // a random starting point
	private UnsignedInteger modelName;
	private UnsignedShort maxPowerIndex;
	private SignedShort maxPower;
	private UnsignedShort channelIndex;
	private UnsignedShort hopTableID;

	private UnsignedInteger getUniqueMessageID() {
		return new UnsignedInteger(MessageID++);
	}

	public Speedway(String rfidIP) {
		this.rfidIP = rfidIP;
	}

	private void connect(String ip) {
		// create client-initiated LLRP connection

		connection = new LLRPConnector(this, ip);

		// connect to reader
		// LLRPConnector.connect waits for successful
		// READER_EVENT_NOTIFICATION from reader
		try {
			logger.info("Initiate LLRP connection to reader");
			((LLRPConnector) connection).connect();
		} catch (LLRPConnectionAttemptFailedException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
	}

	private void disconnect() {
		LLRPMessage response;
		CLOSE_CONNECTION close = new CLOSE_CONNECTION();
		close.setMessageID(getUniqueMessageID());
		try {
			// don't wait around too long for close
			response = connection.transact(close, 4000);

			// check whether ROSpec addition was successful
			StatusCode status = ((CLOSE_CONNECTION_RESPONSE) response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("CLOSE_CONNECTION was successful");
			} else {
				logger.info(response.toXMLString());
				logger.info("CLOSE_CONNECTION Failed ... continuing anyway");
			}

		} catch (InvalidLLRPMessageException ex) {
			logger.error("CLOSE_CONNECTION: Received invalid response message");
		} catch (TimeoutException ex) {
			logger.info("CLOSE_CONNECTION Timeouts ... continuing anyway");
		}
	}

	private void enableImpinjExtensions() {
		LLRPMessage response;

		try {
			logger.info("IMPINJ_ENABLE_EXTENSIONS ...");
			IMPINJ_ENABLE_EXTENSIONS ena = new IMPINJ_ENABLE_EXTENSIONS();
			ena.setMessageID(getUniqueMessageID());

			response = connection.transact(ena, 10000);

			StatusCode status = ((IMPINJ_ENABLE_EXTENSIONS_RESPONSE) response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("IMPINJ_ENABLE_EXTENSIONS was successful");
			} else {
				logger.info(response.toXMLString());
				logger.info("IMPINJ_ENABLE_EXTENSIONS Failure");
				System.exit(1);
			}
		} catch (InvalidLLRPMessageException ex) {
			logger.error("Could not process IMPINJ_ENABLE_EXTENSIONS response");
			System.exit(1);
		} catch (TimeoutException ex) {
			logger.error("Timeout Waiting for IMPINJ_ENABLE_EXTENSIONS response");
			System.exit(1);
		}
	}

	private void factoryDefault() {
		LLRPMessage response;

		try {
			// factory default the reader
			logger.info("SET_READER_CONFIG with factory default ...");
			SET_READER_CONFIG set = new SET_READER_CONFIG();
			set.setMessageID(getUniqueMessageID());
			set.setResetToFactoryDefault(new Bit(true));
			response = connection.transact(set, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((SET_READER_CONFIG_RESPONSE) response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("SET_READER_CONFIG Factory Default was successful");
			} else {
				logger.info(response.toXMLString());
				logger.info("SET_READER_CONFIG Factory Default Failure");
				System.exit(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void getReaderCapabilities() {
		LLRPMessage response;
		GET_READER_CAPABILITIES_RESPONSE gresp;

		GET_READER_CAPABILITIES get = new GET_READER_CAPABILITIES();
		GetReaderCapabilitiesRequestedData data = new GetReaderCapabilitiesRequestedData(
				GetReaderCapabilitiesRequestedData.All);
		get.setRequestedData(data);
		get.setMessageID(getUniqueMessageID());
		logger.info("Sending GET_READER_CAPABILITIES message  ...");
		try {
			response = connection.transact(get, 10000);

			// check whether GET_CAPABILITIES addition was successful
			gresp = (GET_READER_CAPABILITIES_RESPONSE) response;
			StatusCode status = gresp.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("GET_READER_CAPABILITIES was successful");

				// get the info we need
				GeneralDeviceCapabilities dev_cap = gresp.getGeneralDeviceCapabilities();
				if ((dev_cap == null) || (!dev_cap.getDeviceManufacturerName().equals(new UnsignedInteger(25882)))) {
					logger.error("DocSample2 must use Impinj model Reader, not "
							+ dev_cap.getDeviceManufacturerName().toString());
					System.exit(1);
				}

				modelName = dev_cap.getModelName();
				logger.info("Found Impinj reader model " + modelName.toString());

				// get the max power level
				if (gresp.getRegulatoryCapabilities() != null) {
					UHFBandCapabilities band_cap = gresp.getRegulatoryCapabilities().getUHFBandCapabilities();

					List<TransmitPowerLevelTableEntry> pwr_list = band_cap.getTransmitPowerLevelTableEntryList();

					TransmitPowerLevelTableEntry entry = pwr_list.get(pwr_list.size() - 1);

					maxPowerIndex = entry.getIndex();
					maxPower = entry.getTransmitPowerValue();
					// LLRP sends power in dBm * 100
					double d = ((double) maxPower.intValue()) / 100;

					logger.info("Max power " + d + " dBm at index " + maxPowerIndex.toString());
				}
			} else {
				logger.info(response.toXMLString());
				logger.info("GET_READER_CAPABILITIES failures");
				System.exit(1);
			}
		} catch (InvalidLLRPMessageException ex) {
			logger.error("Could not display response string");
		} catch (TimeoutException ex) {
			logger.error("Timeout waiting for GET_READER_CAPABILITIES response");
			System.exit(1);
		}
	}

	private void getReaderConfiguration() {
		LLRPMessage response;
		GET_READER_CONFIG_RESPONSE gresp;

		GET_READER_CONFIG get = new GET_READER_CONFIG();
		GetReaderConfigRequestedData data = new GetReaderConfigRequestedData(GetReaderConfigRequestedData.All);
		get.setRequestedData(data);
		get.setMessageID(getUniqueMessageID());
		get.setAntennaID(new UnsignedShort(0));
		get.setGPIPortNum(new UnsignedShort(0));
		get.setGPOPortNum(new UnsignedShort(0));

		logger.info("Sending GET_READER_CONFIG message  ...");
		try {
			response = connection.transact(get, 10000);

			// check whether GET_CAPABILITIES addition was successful
			gresp = (GET_READER_CONFIG_RESPONSE) response;
			StatusCode status = gresp.getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("GET_READER_CONFIG was successful");

				List<AntennaConfiguration> alist = gresp.getAntennaConfigurationList();

				if (!alist.isEmpty()) {
					AntennaConfiguration a_cfg = alist.get(0);
					channelIndex = a_cfg.getRFTransmitter().getChannelIndex();
					hopTableID = a_cfg.getRFTransmitter().getHopTableID();
					logger.info("ChannelIndex " + channelIndex.toString() + " hopTableID " + hopTableID.toString());
				} else {
					logger.error("Could not find antenna configuration");
					System.exit(1);
				}
			} else {
				logger.info(response.toXMLString());
				logger.info("GET_READER_CONFIG failures");
				System.exit(1);
			}
		} catch (InvalidLLRPMessageException ex) {
			logger.error("Could not display response string");
		} catch (TimeoutException ex) {
			logger.error("Timeout waiting for GET_READER_CONFIG response");
			System.exit(1);
		}
	}

	private ADD_ROSPEC buildROSpecFromObjects() {
		logger.info("Building ADD_ROSPEC message from scratch ...");
		ADD_ROSPEC addRoSpec = new ADD_ROSPEC();
		addRoSpec.setMessageID(getUniqueMessageID());

		rospec = new ROSpec();

		// set up the basic info for the RO Spec.
		rospec.setCurrentState(new ROSpecState(ROSpecState.Disabled));
		rospec.setPriority(new UnsignedByte(0));
		rospec.setROSpecID(new UnsignedInteger(12345));

		// set the start and stop conditions for the ROSpec.
		// For now, we will start and stop manually
		ROBoundarySpec boundary = new ROBoundarySpec();
		ROSpecStartTrigger start = new ROSpecStartTrigger();
		ROSpecStopTrigger stop = new ROSpecStopTrigger();
		start.setROSpecStartTriggerType(new ROSpecStartTriggerType(ROSpecStartTriggerType.Null));
		stop.setROSpecStopTriggerType(new ROSpecStopTriggerType(ROSpecStopTriggerType.Null));
		stop.setDurationTriggerValue(new UnsignedInteger(0));
		boundary.setROSpecStartTrigger(start);
		boundary.setROSpecStopTrigger(stop);
		rospec.setROBoundarySpec(boundary);

		// set up what we want to do in the ROSpec. In this case
		// build the simples inventory on all channels using defaults
		AISpec aispec = new AISpec();

		// what antennas to use.
		UnsignedShortArray ants = new UnsignedShortArray();
		ants.add(new UnsignedShort(0)); // 0 means all antennas
		aispec.setAntennaIDs(ants);

		// set up the AISpec stop condition and options for inventory
		AISpecStopTrigger aistop = new AISpecStopTrigger();
		aistop.setAISpecStopTriggerType(new AISpecStopTriggerType(AISpecStopTriggerType.Null));
		aistop.setDurationTrigger(new UnsignedInteger(0));
		aispec.setAISpecStopTrigger(aistop);

		// set up any override configuration. none in this case
		InventoryParameterSpec ispec = new InventoryParameterSpec();
		ispec.setAntennaConfigurationList(null);
		ispec.setInventoryParameterSpecID(new UnsignedShort(23));
		ispec.setProtocolID(new AirProtocols(AirProtocols.EPCGlobalClass1Gen2));
		List<InventoryParameterSpec> ilist = new ArrayList<InventoryParameterSpec>();
		ilist.add(ispec);

		aispec.setInventoryParameterSpecList(ilist);
		List<SpecParameter> slist = new ArrayList<SpecParameter>();
		slist.add(aispec);
		rospec.setSpecParameterList(slist);

		addRoSpec.setROSpec(rospec);

		return addRoSpec;
	}

	private ADD_ROSPEC buildROSpecFromFile() {
		URL url = this.getClass().getResource(".");
		if(null == url) {
			logger.error("url is null_1");
			return null;
		}
		currentPath = url.getFile();
		logger.debug("currentPath:" + currentPath);
		logger.info("Loading ADD_ROSPEC message from file ADD_ROSPEC.xml ...");
		try {
			LLRPMessage addRospec = Util.loadXMLLLRPMessage(new File(currentPath + "/ADD_ROSPEC.xml"));
			return (ADD_ROSPEC) addRospec;
		} catch (FileNotFoundException ex) {
			logger.error("Could not find file");
			System.exit(1);
		} catch (IOException ex) {
			logger.error("IO Exception on file");
			System.exit(1);
		} catch (JDOMException ex) {
			logger.error("Unable to convert LTK-XML to DOM");
			System.exit(1);
		} catch (InvalidLLRPMessageException ex) {
			logger.error("Unable to convert LTK-XML to Internal Object");
			System.exit(1);
		}
		return null;
	}

	private void setReaderConfiguration() {
		URL url = this.getClass().getResource(".");
		if(url == null){
			logger.error("url is null");
			return ;
		}
		currentPath = url.getFile();
		logger.debug("currentPath:" + currentPath);
		LLRPMessage response;

		logger.info("Loading SET_READER_CONFIG message from file SET_READER_CONFIG.xml ...");
		try {
			LLRPMessage setConfigMsg = Util.loadXMLLLRPMessage(new File(currentPath + "/SET_READER_CONFIG.xml"));

			// touch up the transmit power for max
			SET_READER_CONFIG setConfig = (SET_READER_CONFIG) setConfigMsg;
			AntennaConfiguration a_cfg = setConfig.getAntennaConfigurationList().get(0);
			RFTransmitter rftx = a_cfg.getRFTransmitter();
			rftx.setChannelIndex(channelIndex);
			rftx.setHopTableID(hopTableID);
			rftx.setTransmitPower(maxPowerIndex);

			response = connection.transact(setConfig, 10000);

			// check whetherSET_READER_CONFIG addition was successful
			StatusCode status = ((SET_READER_CONFIG_RESPONSE) response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("SET_READER_CONFIG was successful");
			} else {
				logger.info(response.toXMLString());
				logger.info("SET_READER_CONFIG failures");
				System.exit(1);
			}

		} catch (TimeoutException ex) {
			logger.error("Timeout waiting for SET_READER_CONFIG response");
			System.exit(1);
		} catch (FileNotFoundException ex) {
			logger.error("Could not find file");
			System.exit(1);
		} catch (IOException ex) {
			logger.error("IO Exception on file");
			System.exit(1);
		} catch (JDOMException ex) {
			logger.error("Unable to convert LTK-XML to DOM");
			System.exit(1);
		} catch (InvalidLLRPMessageException ex) {
			logger.error("Unable to convert LTK-XML to Internal Object");
			System.exit(1);
		}
	}

	private void addRoSpec(boolean xml) {
		LLRPMessage response;

		ADD_ROSPEC addRospec = null;

		if (xml) {
			addRospec = buildROSpecFromFile();
		} else {
			addRospec = buildROSpecFromObjects();
		}

		addRospec.setMessageID(getUniqueMessageID());
		rospec = addRospec.getROSpec();

		logger.info("Sending ADD_ROSPEC message  ...");
		try {
			response = connection.transact(addRospec, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((ADD_ROSPEC_RESPONSE) response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("ADD_ROSPEC was successful");
			} else {
				logger.info(response.toXMLString());
				logger.info("ADD_ROSPEC failures");
				System.exit(1);
			}
		} catch (InvalidLLRPMessageException ex) {
			logger.error("Could not display response string");
		} catch (TimeoutException ex) {
			logger.error("Timeout waiting for ADD_ROSPEC response");
			System.exit(1);
		}
	}

	private void enable() {
		LLRPMessage response;
		try {
			// factory default the reader
			logger.info("ENABLE_ROSPEC ...");
			ENABLE_ROSPEC ena = new ENABLE_ROSPEC();
			ena.setMessageID(getUniqueMessageID());
			ena.setROSpecID(rospec.getROSpecID());

			response = connection.transact(ena, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((ENABLE_ROSPEC_RESPONSE) response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("ENABLE_ROSPEC was successful");
			} else {
				logger.error(response.toXMLString());
				logger.info("ENABLE_ROSPEC_RESPONSE failed ");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void launch() {
		LLRPMessage response;
		try {
			logger.info("START_ROSPEC ...");
			START_ROSPEC start = new START_ROSPEC();
			start.setMessageID(getUniqueMessageID());
			start.setROSpecID(rospec.getROSpecID());

			response = connection.transact(start, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((START_ROSPEC_RESPONSE) response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("START_ROSPEC was successful");
			} else {
				logger.error(response.toXMLString());
				logger.info("START_ROSPEC_RESPONSE failed ");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void stop() {
		LLRPMessage response;
		try {
			logger.info("STOP_ROSPEC ...");
			STOP_ROSPEC stop = new STOP_ROSPEC();
			stop.setMessageID(getUniqueMessageID());
			stop.setROSpecID(rospec.getROSpecID());

			response = connection.transact(stop, 10000);

			// check whether ROSpec addition was successful
			StatusCode status = ((STOP_ROSPEC_RESPONSE) response).getLLRPStatus().getStatusCode();
			if (status.equals(new StatusCode("M_Success"))) {
				logger.info("STOP_ROSPEC was successful");
			} else {
				logger.error(response.toXMLString());
				logger.info("STOP_ROSPEC_RESPONSE failed ");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void logOneCustom(Custom cust) {

		String output = "";
		if (!cust.getVendorIdentifier().equals(25882)) {
			logger.error("Non Impinj Extension Found in message");
			return;
		}
	}

	private void logOneTagReport(TagReportData tr) {
		LLRPParameter epcp = (LLRPParameter) tr.getEPCParameter();

		String epcString = "";
		if (epcp != null) {
			if (epcp.getName().equals("EPC_96")) {
				EPC_96 epc96 = (EPC_96) epcp;
				epcString += epc96.getEPC().toString();
			} else if (epcp.getName().equals("EPCData")) {
				EPCData epcData = (EPCData) epcp;
				epcString += epcData.getEPC().toString();
			}
		} else {
			logger.error("Could not find EPC in Tag Report");
			System.exit(1);
		}

		// all of these values are optional, so check their non-nullness first
		if (tr.getAntennaID() != null) {
			epcString += " Antenna: " + tr.getAntennaID().getAntennaID().toString();
		}

		if (tr.getChannelIndex() != null) {
			epcString += " ChanIndex: " + tr.getChannelIndex().getChannelIndex().toString();
		}

		if (tr.getFirstSeenTimestampUTC() != null) {
			epcString += " FirstSeen: " + tr.getFirstSeenTimestampUTC().getMicroseconds().toString();
		}

		if (tr.getInventoryParameterSpecID() != null) {
			epcString += " ParamSpecID: " + tr.getInventoryParameterSpecID().getInventoryParameterSpecID().toString();
		}

		if (tr.getLastSeenTimestampUTC() != null) {
			epcString += " LastTime: " + tr.getLastSeenTimestampUTC().getMicroseconds().toString();
		}

		if (tr.getPeakRSSI() != null) {
			epcString += " RSSI: " + tr.getPeakRSSI().getPeakRSSI().toString();
		}

		if (tr.getROSpecID() != null) {
			epcString += " ROSpecID: " + tr.getROSpecID().getROSpecID().toString();
		}

		if (tr.getTagSeenCount() != null) {
			epcString += " SeenCount: " + tr.getTagSeenCount().getTagCount().toString();
		}

		logger.debug(epcString);

		final String epc = epcString;
		// 根据epc号码，查询行李图片信息
		new Thread(new Runnable(){
			@Override
			public void run() {
				showRunningPackageModels(epc);
			}
		}).start();
	}
	
	private void showRunningPackageModels(String epc) {
		RunningPackageModel runningPackageModel = historyService.getRunningPackageModelByEPC(epc);
		List<XrayImageModel> xrayImmageModels = runningPackageModel.getXrayImages();
		Iterator<XrayImageModel> iteratorImage = xrayImmageModels.iterator();
		while(iteratorImage.hasNext()) {
			XrayImageModel xrayImageModel = iteratorImage.next();
			xrayImageModel.setRunningPackage(null);
		}
		String json = new Gson().toJson(runningPackageModel);
		String result = JsonUtil.transformJson("views", json);
		
		BroadcasterFactory broadcasterFactory = SpringApplicationContext.getBean(BroadcasterFactory.class);
    	//通过broadcasterFactory对象获取查验频道对象
    	final Broadcaster broadcasterA = broadcasterFactory.lookup(DefaultBroadcaster.class, Constant.CHANNEL_A, true);
    	//通过broadcasterFactory对象获取查验频道对象
    	final Broadcaster broadcasterB = broadcasterFactory.lookup(DefaultBroadcaster.class, Constant.CHANNEL_B, true);
    	//向查验频道的订阅者发送算法计算后的坐标
    	broadcasterA.broadcast(result);
    	broadcasterB.broadcast(result);
	}

	public void messageReceived(LLRPMessage message) {
		logger.debug("Received " + message.getName() + " message asychronously");

		if (message.getTypeNum() == RO_ACCESS_REPORT.TYPENUM) {
			RO_ACCESS_REPORT report = (RO_ACCESS_REPORT) message;

			List<TagReportData> tdlist = report.getTagReportDataList();

			for (TagReportData tr : tdlist) {
				logOneTagReport(tr);
			}

			List<Custom> clist = report.getCustomList();
			for (Custom cust : clist) {
				logOneCustom(cust);
			}
		} else if (message.getTypeNum() == READER_EVENT_NOTIFICATION.TYPENUM) {
		}
	}

	public void errorOccured(String s) {
		logger.error(s);
	}

	public static void main(String[] args) {
//		Speedway sd = new Speedway("");
//		BasicConfigurator.configure();
//
//		Logger.getRootLogger().setLevel(Level.ERROR);
//		Speedway example = new Speedway("192.168.3.61");
//		logger.setLevel(Level.INFO);
//
//		example.connect(example.rfidIP);
//		example.enableImpinjExtensions();
//		example.factoryDefault();
//		example.getReaderCapabilities();
//		example.getReaderConfiguration();
//		example.setReaderConfiguration();
//		example.addRoSpec(true);
//		example.enable();
//		example.launch();
//		try {
//			Thread.sleep(24 * 60 * 60 * 1000);
//		} catch (InterruptedException ex) {
//			logger.error("Sleep Interrupted");
//		}
//		example.stop();
//		example.disconnect();
//		System.exit(0);
	}
	
	@Override
	public void run() {
		BasicConfigurator.configure();

		Logger.getRootLogger().setLevel(Level.ERROR);
		logger.setLevel(Level.INFO);

		connect(rfidIP);
		enableImpinjExtensions();
		factoryDefault();
		getReaderCapabilities();
		getReaderConfiguration();
		setReaderConfiguration();
		addRoSpec(true);
		enable();
		launch();
		try {
			Thread.sleep(24 * 60 * 60 * 1000);
		} catch (InterruptedException ex) {
			logger.error("Sleep Interrupted");
		}
		stop();
		disconnect();
		System.exit(0);
	}
}
