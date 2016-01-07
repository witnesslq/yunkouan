<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>index</title>
<link type="text/css" href="${ctx}/resources/css/style.css"	rel="stylesheet" />
<script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.min.js"></script>
</head>
<body>
	<form enctype="multipart/form-data" action="${ctx}/xmachine/uploadFiles" method="post">
		条码号:<input type="text" name="barcode" ><br>
		图片文件1：<br> <input name="imageFile1" type="file"><br>
		图片文件2：<br> <input name="imageFile2" type="file"><br>
		原子序数文件1：<br> <input name="dataFile1" type="file"><br>
		<!-- 原子序数文件2：<br> <input name="dataFile2" type="file"><br> -->
		<input type="submit" value="上传文件">
		
		<input type="button" id="bbc" name="bbc" value="测试广播" >
	</form>
	
	<script type="text/javascript">
	 $(function(){
		//设置权限
		$("#bbc").click(function(){ //点击修改角色/添加角色弹窗
			//请求后台图片处理
            $.ajax({
                url:"${ctx}/operator/roletree?operatorId=" + operatorId,
                type:"POST",
                async:true,
                dataType:"text", 
                success:function(data){
                    if(data != '' && data != null){
                        //console.log(data);
                        dataTree({
                            treeBox:"#permissionAllot",//树的父元素Ul的Id
                            dataParam: data
                        });
                    }
                },
                error:function(err) {
                    console.log("请求角色菜单时发生错误:" + err);
                }
            });
		}); 
	 })
	</script>
</body>
</html>
