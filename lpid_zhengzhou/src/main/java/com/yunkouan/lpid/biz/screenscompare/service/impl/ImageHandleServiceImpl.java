package com.yunkouan.lpid.biz.screenscompare.service.impl;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yunkou.common.util.AtrImgProc;
import com.yunkou.common.util.DateTimeUtil;
import com.yunkou.common.util.FileUtils;
import com.yunkouan.lpid.biz.screenscompare.bo.ImageHandleModel;
import com.yunkouan.lpid.biz.screenscompare.imageDealDll.imgoperate.XrayButtonOperateJnaInvokeDll;
import com.yunkouan.lpid.commons.util.Constant;

/**
 * <P>Title: csc_sh</P>
 * <P>Description: 调用dll做图片处理</P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年9月4日-上午10:55:57</P>
 * @author yangli
 * @version 1.0.0
 */
@Service("imageHandleService")
public class ImageHandleServiceImpl {
    
    /**
     * 上一次处理图片的url
     */
    private String lastHandleImageUrl = "";
    private AtrImgProc atr = new AtrImgProc();
    private static Logger logger = LoggerFactory.getLogger(ImageHandleServiceImpl.class);
    static {
        //图片处理初始化
//        ImageProcJnaInvokeDll.INSTANCE.XRI_Init();
    }
    

    /**
     * 图片处理
     * 
     * 假设原来图片的名称为20141212abc.jpg
     * 1.预先生成处理后图片的名称：原名称+处理编号(ImageHandleMode)+后缀名，比如黑白名称为：20141212abc11.jpg
     * 2.调用图片处理加载待处理的图片(XRI_LoadImage)，图片参数为原来图片名称：20141212abc.jpg
     * 3.调用图片处理做相应的处理，比如黑白为：XRI_RGB2Gray()
     * 4.调用图片处理保存图片(XRI_Save)，传入的参数为20141212abc11.jpg
     * 
     * @param imageUrl 待处理图片url相对地址
     * @param handleMode 图片处理方式            
     * @return 图片处理后在web中的相对路径
     * @since 1.0.0
     * 2014年9月2日-下午5:57:51
     */
    public String imageHandle(String imageUrl, ImageHandleModel handleMode) {
        //图片处理后的文件
        File afterHandleFile = getAfterHandleFile(imageUrl, handleMode);//  resources/files/xrayPhotos/nodeal/20150130/150130_134533_00022618.jpg
        String afterHandleFilePath = afterHandleFile.getPath();// D:\tomcat\apache-tomcat-7.0.53\wtpwebapps\cscsh\resources\files\xrayPhotos\deal\20150130\150130_134533_0002261811.jpg
        													   
        //文件不存在，则调用图片处理处理
        if(!afterHandleFile.exists()) {
            /*
        	if (handleMode== ImageHandleMode.monochrome){// 如果是黑白图片处理，目前调用马兵会的图片处理，其它情况，调用王振华的图片处理.
        		// 马兵会的图片处理调用开始===============================================
        		readImage_black(FileUtils.getFilePath(imageUrl),afterHandleFilePath);
        		// 马兵会的图片处理调用结束===============================================
        	} else {
        		// 王振华的图片处理调用开始-----------------------------------------------
            	//如果本次处理和上次处理的图片一样，就不用新加载图片
            	if(!lastHandleImageUrl.equals(imageUrl)) {
            		//加载图片  // D:/tomcat/apache-tomcat-7.0.53/wtpwebapps/cscsh/resources/files/xrayPhotos/nodeal/20150130/150130_134533_00022618.jpg
            		XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_LoadImage(FileUtils.getFilePath(imageUrl));
            	}
            	//调用dll方法做图片处理
            	int resultValut = imageHandle(handleMode);
            	logger.debug("调用dll方法做图片处理结果：" + resultValut);
            	XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_Save(afterHandleFilePath);
            	// 王振华的图片处理调用结束-----------------------------------------------
        	}
        	
        	lastHandleImageUrl = imageUrl;
        	*/
            
            //根据同屏比对页面按钮的点击，调用图片处理
            XrayButtonOperateJnaInvokeDll.INSTANCE.ColorFineTune(
                    FileUtils.getFilePath(imageUrl), handleMode.getNumber(), afterHandleFilePath);
        }
        
        //图片处理后在web中的相对路径
        return afterHandleFilePath.substring(afterHandleFilePath.indexOf("resources") - 1);
    }
    /**
     * 调用dll方法做图片处理
     * 
     * @param handleMode 图片处理方式
     * @return 正确执行返回0，否则为负值，返回负值表示图像数据不存在
     * @since 1.0.0
     * 2014年9月4日-上午10:52:04
     */
    /*
    private int imageHandle(ImageHandleMode handleMode) {
        // 调用图片处理处理
        switch (handleMode) {
            case color: //彩色
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_Restore();
            case monochrome: //黑白 
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_RGB2Gray();
            case highlight: //加亮
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_BrightnessUp();
            case shadow: //减暗
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_BrightnessDown();
            case highEnergy: //高能
                break;
            case lowEnergy: //低能
                break;
            case organicReject: //有机剔除
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_OrganicsMask();
            case inorganicReject: //无机剔除
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_InorganicsMask();
            case partStrengthen: //局增
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_Enhance();
            case exceedStrengthen: //超增
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_Enhance();
            case inverse: //反色
                return XrayButtonOperateJnaInvokeDll.INSTANCE.XRI_ReverseColor();
            default:
                break;
        }
        
        return -1;
    }
    */
    /**
     * 图片处理后的文件
     * 
     * @param imageUrl 待处理图片相对路径
     * @param handleMode 图片处理方式
     * @return 处理后文件,名称规范：原文件名称+处理类型+后缀名称
     * @since 1.0.0
     * 2014年9月3日-下午6:44:09
     */
    private File getAfterHandleFile(String imageUrl, ImageHandleModel handleMode) {
        //图片名称
        String imageName = getName(imageUrl);
        //处理后文件名称:原文件名称+处理类型+后缀名称
        String afterHandleImageName = removeExtension(imageName) + handleMode.getNumber() 
                + Constant.EXTENSION_SEPARATOR + getExtension(imageName) ;
        //处理后文件存放绝对路径:DEAL_DIRECTORY + yyyyMMdd
        File afterHandleDirectoryUrl = new File(FileUtils.getFilePath(Constant.DEAL_DIRECTORY) + 
                format(new Date(), DateTimeUtil.YMD_DATE_FORMAT));
        
        //处理后文件存放绝对路径若不存在则创建
        if(!afterHandleDirectoryUrl.exists()) {
            afterHandleDirectoryUrl.mkdirs();
        }
        
        return new File(afterHandleDirectoryUrl.getPath() + File.separator + afterHandleImageName);
    }
    
	/**
	 * 读取黑白X光图片
	 * @param imageFilePath
	 */
	public void readImage_black(String imageUrl, String imageNewPath) {
		try {
			// 处理前X光图片
			File imgFile = new File(imageUrl);
			BufferedImage img = ImageIO.read(imgFile);
			
			// 图像处理后
			BufferedImage newImg = atr.img2Gray(img);
			File imgDealed = new File(imageNewPath);
			
//			atr.showImage(newImg);  // swing窗口显示黑白图片
			
			ImageIO.write(newImg, "jpg", imgDealed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
