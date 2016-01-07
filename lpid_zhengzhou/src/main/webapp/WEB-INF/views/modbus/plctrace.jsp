<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${ctx}/resources/js/biz/plctrace.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common/jquery.atmosphere-min.js"></script>
<title>行邮PLC参数一览及控制画面</title>
</head>
<body>
<div class="container-fluid">
        <div class="row">
            <div class="col-md-10" style="margin:8% auto 0; float:none;">
            	<img class="monitor" width="100%" src="${ctx}/resources/images/monitor.png" />
            	<!-- 上料皮带 -->
            	<div class="rack-box"></div>
            	<!-- X光机前皮带 -->
            	<div class="xray-belt-front"></div>
            	<!-- X光机后皮带 -->
            	<div class="xray-belt-behind"></div>
            	<!-- 推箱皮带 -->
            	<div class="taper-case-belt"></div>
            	<!-- 贴标机 -->
            	<div class="labellers"></div>
            	<!-- 推杆 -->
            	<div class="push-rods"></div>
            </div>
        </div>
    </div>
	<script type="text/javascript">
		var channel ="plcChannel";
		console.log("plcChannel:" + channel);
	</script>
</body>
</html>