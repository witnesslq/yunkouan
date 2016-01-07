<%@ page language="java" pageEncoding="utf-8" isELIgnored="false" import="com.yunkou.common.util.ResourceBundleUtil"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
String picctx = ResourceBundleUtil.getSystemString("IMAGEWEB");
application.setAttribute("picctx", picctx);
%>
<script type="text/javascript">
<!-- 定义ctx变量,供javascript使用 -->
	var ctx = "${ctx}";
	var picctx = "${picctx}";
</script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>中云智慧-后台管理系统</title>
<meta name=”format-detection” content=”telephone=yes” />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="no" />
<!-- 启用360浏览器的极速模式(webkit) -->
<meta name="renderer" content="webkit">
<!-- 避免IE使用兼容模式 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->
<meta name="HandheldFriendly" content="true">
<!-- 微软的老式浏览器 -->
<meta name="MobileOptimized" content="320">
<!-- uc强制竖屏 -->
<meta name="screen-orientation" content="portrait">
<!-- QQ强制竖屏 -->
<meta name="x5-orientation" content="portrait">
<!-- UC强制全屏 -->
<meta name="full-screen" content="yes">
<!-- QQ强制全屏 -->
<meta name="x5-fullscreen" content="true">
<!-- UC应用模式 -->
<meta name="browsermode" content="application">
<!-- QQ应用模式 -->
<meta name="x5-page-mode" content="app">
<!-- windows phone 点击无高光 -->
<meta name="msapplication-tap-highlight" content="no">
<link rel="stylesheet" href="${ctx}/resources/lib/bootstrap-3.3.4-dist/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctx}/resources/lib/bootstrap-3.3.4-dist/css/font-awesome.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctx}/resources/css/style.css" type="text/css"/>
<script type="text/javascript" src="${ctx}/resources/js/common/jQuery-v1.7.2.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common/tooltip.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/common/getParam.js"></script>