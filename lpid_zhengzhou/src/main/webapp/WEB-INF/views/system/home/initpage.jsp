<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="${ctx}/resources/js/biz/initpage.js"></script>
<title>国检违禁品智能查验系统</title>
</head>
<body>
	<!--头部-->
	<div class="header-main">
		<div class="col-md-6">
			<a class="logo" href="#">国检违禁品智能查验系统</a>
		</div>
		<div class="col-md-6">
			<ul>
				<li>当前时间： <i class="extrude" id="date"></i>
				</li>
				<li><i class="icon-user"></i> <i class="extrude" id="userName">${operator.name}</i>
				</li>
				<li><a id="logout" href="#"><i class="icon-signout"> </i>退出 </a></li>
			</ul>
		</div>
	</div>
	<!--头部end-->
	<!--侧边栏导航-->
    <div class="container-fluid min-height-100">
        <div class="row min-height-100">
            <div class="col-xs-3 col-md-2 sidebar min-height-100">
                <ul id="sidebar">
                	<c:forEach var="levelFirstMenu" items="${menuList}" varStatus="it">
	                    <li>
	                    	<c:if test="${it.count == 1}">
	                    		<c:if test="${levelFirstMenu.linkUrl != '' && levelFirstMenu.linkUrl != null}">
	                        		<h3 class="active"><a target="main" href="${ctx}${levelFirstMenu.linkUrl}"><i class="${levelFirstMenu.menuIcon}"></i>${levelFirstMenu.name}</a></h3>
	                        	</c:if>
	                        	<c:if test="${levelFirstMenu.linkUrl == '' || levelFirstMenu.linkUrl == null}">
	                        		<h3 class="active"><a target="main" href="javascript:void(0)"><i class="${levelFirstMenu.menuIcon}"></i>${levelFirstMenu.name}</a></h3>
	                        	</c:if>
	                        </c:if>
	                        <c:if test="${it.count > 1}">
	                        	<c:if test="${levelFirstMenu.linkUrl != '' && levelFirstMenu.linkUrl != null}">
	                        		<h3><a target="main" href="${ctx}/${levelFirstMenu.linkUrl}"><i class="${levelFirstMenu.menuIcon}"></i>${levelFirstMenu.name}</a></h3>
	                        	</c:if>
	                        	<c:if test="${levelFirstMenu.linkUrl == '' || levelFirstMenu.linkUrl == null}">
	                        		<h3><a target="main" href="javascript:void(0);"><i class="${levelFirstMenu.menuIcon}"></i>${levelFirstMenu.name}</a></h3>
	                        	</c:if>
	                        </c:if>
	                        <c:if test="${(levelFirstMenu.menus)!= null && fn:length(levelFirstMenu.menus) > 0}">
	                        <c:if test="${it.count == 1}">
		                        <dl style="display: block;">
		                        	<c:forEach var="levelSecondMenu" items="${levelFirstMenu.menus}" varStatus="it">
		                            	<c:if test="${it.count == 1}">
		                            		<dd><a class="active" target="main" href="${ctx}${levelSecondMenu.linkUrl}">${levelSecondMenu.name}</a></dd>
		                        		</c:if>
		                        		<c:if test="${it.count > 1}">
		                        			<dd><a target="main" href="${ctx}${levelSecondMenu.linkUrl}">${levelSecondMenu.name}</a></dd>
		                        		</c:if>
		                        	</c:forEach>
		                        </dl>
	                        </c:if>
	                        <c:if test="${it.count > 1}">
		                        <dl>
		                        	<c:forEach var="levelSecondMenu" items="${levelFirstMenu.menus}" varStatus="it">
		                            	<c:if test="${it.count == 1}">
		                            		<dd><a class="active" target="main" href="${ctx}${levelSecondMenu.linkUrl}">${levelSecondMenu.name}</a></dd>
		                        		</c:if>
		                        		<c:if test="${it.count > 1}">
		                        			<dd><a target="main" href="${ctx}${levelSecondMenu.linkUrl}">${levelSecondMenu.name}</a></dd>
		                        		</c:if>
		                        	</c:forEach>
		                        </dl>
	                        </c:if>
	                        </c:if>
	                    </li>
                    </c:forEach>
                </ul>
            </div>
            <!--侧边栏导航end-->
            <!--右侧主框架-->
            <div class="col-md-10 col-xs-9 main min-height-100 pd-0">
                <iframe name="main" frameborder="0" scrolling="no" src="${ctx}/screenscompare/compareshow"></iframe>
            </div>
        </div>
    </div>
</body>
</html>