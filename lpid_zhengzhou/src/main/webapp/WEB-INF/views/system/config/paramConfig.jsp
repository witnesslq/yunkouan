<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>参数配置</title>
</head>
<body>
	<c:forEach items="${parameters}" var="item" step="1" varStatus="status">
		<c:if test="${item.name == 'suspect'}">
			<c:set var="suspect" scope="page" value="${item.value}"/>
		</c:if>
		<c:if test="${item.name == 'labelTotal'}">
			<c:set var="labelTotal" scope="page" value="${item.value}"/>
		</c:if>
	</c:forEach>

		&nbsp;标签总数：
		<input type="text" id="labelTotal" style="margin-top:5px;height:34px;width:70px;color:#000;opacity:1" name="labelTotal" value="${labelTotal}" />
		<input type="button" id="btnLabel" class="btn btn-default" value="更换标签">
	
	<!-- 该配置已经在智能查验画面显示，因此这里注释掉。 
	<br><br>
	算法报警值：<input type="text" id="suspect" name="suspect" value="${suspect}" /><br>
	<input type="button" id="btnConfig" value="确定">
	 -->
	 
	<br><br>
	<!-- <input type="button" id="test" value="更新剩余标签数量"> -->
	<script type="text/javascript">
		$(function(){
			// 更换标签
			$("#btnLabel").click(function(){
				 //请求后台图片处理
                $.ajax({
                    url:"${ctx}/label/setTotal?total=" + $("#labelTotal").val() + "&balance=" + $("#labelTotal").val(),// 标签总数
                    type:"POST",
                    async:true,
                    dataType:"text", 
                    success:function(data){
                        if(data != '' && data != null){
                            
                        }
                    },
                    error:function(err) {
                        console.log("更换标签失败！");
                    }
                });
			});
			
			// 参数配置
			$("#btnConfig").click(function(){
				 //请求后台图片处理
                $.ajax({
                    url:"${ctx}/config/updateConfigs?suspect=" + $("#suspect").val(),// 标签总数
                    type:"POST",
                    async:true,
                    dataType:"text", 
                    success:function(data){
                        if(data != '' && data != null){
                            
                        }
                    },
                    error:function(err) {
                        console.log("参数配置失败！");
                    }
                });
			});
			
			// 更换标签
			$("#test").click(function(){
				 //请求后台图片处理
                $.ajax({
                    url:"${ctx}/config/updateLabelRemain",
                    type:"POST",
                    async:true,
                    dataType:"text", 
                    success:function(data){
                        if(data != '' && data != null){
                            
                        }
                    },
                    error:function(err) {
                        console.log("更换剩余标签数量失败！");
                    }
                });
			});
		});
	</script>
</body>
</html>