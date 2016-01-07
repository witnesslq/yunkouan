/**
 * Created by Administrator on 2015/6/26.
 */
     var tooltip = function(message){
    	    console.log("message:" + message);
            var shade = "<div class='tooltip-content tooltip-cons' style='position: absolute; max-width:400px;'><div class='content'><div><p style='margin:0; font-size: 16px;'> " + message + " </p></div></div></div>";
            $("body").append(shade);
            move(0,0,".tooltip-content");
            setTimeout(function(){
                move(1,1,".tooltip-content");
            },3000);
        }

        /*
         * move : 提示消息渐隐，渐现
         * 参数 ： type ， opacity ，elem
         * type : 类型，用于判断
         * opacity ： 透明度，如果elem显示 opacity　＝　0, elem隐藏 opacity = 1；
         * elem ： DOM元素
         * */
        function move(type,opacity,elem){
            var timer = null;
            function _move(){
                if(type == 0){
                    opacity+=0.1;
                    $(elem).css("opacity",opacity);
                    timer = window.setTimeout(_move,60);
                    if(opacity >= 1){
                        clearTimeout(timer);
                    }
                }else{
                    opacity = opacity - 0.1;
                    $(elem).css("opacity",opacity);
                    timer = window.setTimeout(_move,60);
                    if(opacity <= 0){
                        $(elem).remove();
                        clearTimeout(timer);
                    }
                }
            }
            _move();
        }

