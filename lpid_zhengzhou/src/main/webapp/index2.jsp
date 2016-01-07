<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html class="html-w">
  <head>
		<script type="text/javascript" src="${ctx}/resources/js/biz/index.js"></script>
  </head>
  <body class="login-bg">
  	<input type="hidden" id="indexPage" value="index2"/>
    <div class="login-box"></div>
	<div class="login-main">
	    <div class="login-title"> <img src="${ctx}/resources/images/logo.png" /><font>国检违禁品智能查验系统</font> </div>
	    <div class="login-content">
			<input class="form-control" id="loginName" placeholder="用户名" type="text"  tabIndex="1"/>
	        <input class="form-control" id="password" placeholder="密码" type="password"  tabIndex="1"/>
		    <input type="hidden" id="channel" name="channel" value="A"/>
		    <!--  
		    <select id="channel" class="form-control">
		         <option value="">请选择X光机</option>
		         <option value="A">A</option>
		         <option value="B">B</option>
		    </select>
		    -->
	    </div>
	    <div class="login-footer">
	        <button id="login" class="btn btn-primary" type="button" tabIndex="1">登录</button>
	        <button id="reset" class="btn btn-default" type="button" tabIndex="1">重置</button>
	    </div>
	</div>
</body>
</html>