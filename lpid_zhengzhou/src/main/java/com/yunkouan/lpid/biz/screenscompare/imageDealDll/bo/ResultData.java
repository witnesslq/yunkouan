/**  
* @Title: ResultData.java
* @Description: 算法计算结果
* @Copyright: Copyright(c) 2015
* @author yangli 
* @date 2015年6月19日-上午10:11:08
* @version 1.0.0
*/ 
package com.yunkouan.lpid.biz.screenscompare.imageDealDll.bo;

import java.util.List;

public class ResultData {
	List<ImageCoordData> result ;
	private int suspect;// 疑似度
	public List<ImageCoordData> getResult() {
		return result;
	}
	public void setResult(List<ImageCoordData> result) {
		this.result = result;
	}

	public int getSuspect() {
		return suspect;
	}

	public void setSuspect(int suspect) {
		this.suspect = suspect;
	}
	
}
