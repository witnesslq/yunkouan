/**
 * Created by Administrator on 2015/6/19.
 */

/**
 * 画矩形 disposeData（）
 * 参数：
 * elem:"#sContentLeft", //父元素
 * childElem:"#sContentLeft img", //子元素img
 * data:resultObj //数据
**/
function disposeData(options){
    if(options.data != "" && options.data != null){
    	with(options){
        	$(childElem).load(function(){
        		var result = data.value.coords; //获取坐标数组
            	var srcUrl = $(childElem).attr('src'); //获取X光图片的路径
            	var srcWidth = parseInt($(childElem).width()); //获取X光图片的width
            	var srcHeight = parseInt($(childElem).height()); //获取X光图片的height
            	var srcLeft = parseInt($(childElem).position().left); //获取X光图片的left
            	var srcTop = parseInt($(childElem).position().top); //获取X光图片的top
            	
            	var scalingW = srcWidth / data.value.width; //缩放比例;
            	var scalingH = srcHeight / data.value.height; //缩放比例;
            	
            	console.log("srcWidth:" + srcWidth);
            	console.log("srcHeight:" + srcHeight);
            	
            	if(srcUrl.lastIndexOf(options.data.value.imagename) <= 0) return false;
            	console.log("恭喜找到图片啦")
                for(var i = 0; i < result.length; i++){
                	var x1 = result[i].x1;
                	var x2 = result[i].x2;
                	var x3 = result[i].x3;
                	var x4 = result[i].x4;
                	var y1 = result[i].y1;
                	var y2 = result[i].y2;
                	var y3 = result[i].y3;
                	var y4 = result[i].y4;
                	
                	var divWidth = Math.round((x2-x1) * scalingW);
                	var divHeight = Math.round((y4-y1) * scalingH);
                	var divLeft = srcLeft + Math.round(x1 * scalingW);
                	var divTop = srcTop + Math.round(y1 * scalingH);
                	
                	var div = "<div style='width: " + divWidth  + "px; z-index:500; height:" + divHeight +"px; border:2px solid #f00; position:absolute; left:" + divLeft + "px; top:" + divTop + "px;'></div>"
                    $(options.elem).append(div);
                }
        	})
    	}
    }
}

