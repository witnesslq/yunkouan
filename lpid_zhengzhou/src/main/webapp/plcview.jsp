<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<script type="text/javascript" src="${ctx}/resources/js/common/jquery.atmosphere-min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/biz/plcview.js"></script>
<title>PLC控制及数据一览</title>
</head>
<body>
	<h2>PLC数据写入</h2>
	地址:<input type="text" id="plcId" value="15"/>
	值：<!-- <input type="text" id="plcValue"/> -->
	<select name="plcValue" id="plcValue">
		<option value="0" selected="selected">0</option>
		<option value="1">1</option>
	</select>
	<button type="button" id="btnPlc">确定</button>
	
	<h2>PLC数据一览</h2>
	<table>
		<tr>
			<td>上位机下发急停指令10</td>
			<td><input type="text" id="belt_stop" readonly="readonly"></td>
		</tr>
		<tr>
			<td>上位机下发X光机钥匙开关指令11</td>
			<td><input type="text" id="xray_off" readonly="readonly"></td>
		</tr>
		<tr>
			<td>上位机下发X光机启动按钮指令12</td>
			<td><input type="text" id="xray_startup" readonly="readonly"></td>
		</tr>
		<tr>
			<td>上位机下发范指令屏蔽位15</td>
			<td><input type="text" id="controlled" readonly="readonly"></td>
		</tr>
		<tr>
			<td>上位机下发皮带机启动指令16</td>
			<td><input type="text" id="belt_startup" readonly="readonly"></td>
		</tr>
		<tr>
			<td>上位机下发分检物品编号40</td>
			<td><input type="text" id="write_package_no" readonly="readonly"></td>
		</tr>
		<tr>
			<td>上位机下发立即推包指令41</td>
			<td><input type="text" id="bag-push" readonly="readonly"></td>
		</tr>
		<tr>
			<td>PLC上传给上位机光电管1物品编号100</td>
			<td><input type="text" id="read_package_no" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>PLC上传给上位机物品长度101</td>
			<td><input type="text" id="package_length" readonly="readonly"></td>
		</tr>
		<tr>
			<td>物品经过下降沿标志位上传给上位(没有使用)102</td>
			<td><input type="text" id="read_package_no2" readonly="readonly"></td>
		</tr>
		<tr>
			<td>推杆推出状态上传给上位120</td>
			<td><input type="text" id="putters_push_status" readonly="readonly"></td>
		</tr>
		<tr>
			<td>推杆返回状态上传给上位121</td>
			<td><input type="text" id="putters_return_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>贴标动作状态上传给上位122</td>
			<td><input type="text" id="decals_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>远极限开关状态上传给上位123</td>
			<td><input type="text" id="far_on-off_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>近极限开关状态上传给上位124</td>
			<td><input type="text" id="near_on-off_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>PLC上传给上位机节能模式状态127</td>
			<td><input type="text" id="energy_status" readonly="readonly"></td>
		</tr>
		<tr>
			<td>第一段皮带机运行状态上传给上位128</td>
			<td><input type="text" id="first_belt_run_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>第二段皮带机运行状态上传给上位129</td>
			<td><input type="text" id="second_belt_run_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>X光机运行状态上传给上位130</td>
			<td><input type="text" id="x-ray_machine_run_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>有急停或范德兰德停止指令上传给上位131</td>
			<td><input type="text" id="stop_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>贴标机故障、光电管对偏或被遮挡、推杆系统异常状态上传给上位132</td>
			<td><input type="text" id="all_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>推杆长时间没有回到原点状态上传给上位140</td>
			<td><input type="text" id="putters_origin_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>原点开关故障位上传给上位141</td>
			<td><input type="text" id="origin_on-off_status" readonly="readonly"></td>
		</tr>	
		<tr>
			<td>推包器故障位上传给上位142</td>
			<td><input type="text" id="bag-pushing_status" readonly="readonly"></td>
		</tr>																						
	</table>
</body>
</html>