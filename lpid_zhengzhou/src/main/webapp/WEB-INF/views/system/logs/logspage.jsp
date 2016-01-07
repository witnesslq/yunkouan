<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>历史记录查询</title>
    <script type="text/javascript" src="${ctx}/resources/lib/My97DatePicker/WdatePicker.js"></script>
  </head>
  <body>
  <div class="container-fluid">
  		<div class="row">
  		<div class="col-md-12">
  		<form:form action="${ctx}/logs/logList" method="post" commandName="logType">
	  		<div class="search form-inline" style="margin-top:20px;">
				<div class="form-group">
					<lable>日志类型：</lable>
					<form:select name="logTypeId" path="logTypeSelect" class="form-control" items="${logTypeMap}"/>
				</div>
				<div class="form-group">
					<lable>用户名：</lable>
					<input id="operatorName" name="operatorName" class="form-control" type="text" value="${queryModel.operatorName}"/>
				</div>
				<div class="form-group">
					<lable>日志内容：</lable>
					<input id="content" class="form-control" name="content" type="text" value="${queryModel.content}" />
				</div>
				<div class="form-group">
					<lable>时间：</lable>
					<input id="startTime" name="startTime" class="form-control control-time" type="text" value="${queryModel.startTime}" onclick="WdatePicker()"/> - 
              		<input id="endTime" name="endTime" class="form-control control-time" type="text" value="${queryModel.endTime}" onclick="WdatePicker()"/>
				</div>
				<div class="form-group">
					<button class="btn btn-primary" type="submit">查询</button>
				</div>
			</div>
		    <div class="system" style="margin-top:20px;">
		      <table class="table table-bordered">
		        <thead>
		          <tr>
		            <th>用户名</th>
		            <th>日志类型</th>
		            <th>日志内容</th>
		            <th>时间</th>
		          </tr>
		        </thead>
		        <tbody>
		        <c:forEach var="log" items="${logList}" varStatus="it">
		          <tr>
		            <!-- 用户名 -->
		            <td>${log.operator.loginName}</td>
		            <!-- 日志类型 -->
		            <td>${log.logType.name}</td>
		            <!-- 日志内容 -->
		            <td>${log.content}</td>
		            <!-- 时间 -->
		            <td><fmt:formatDate value="${log.createTime}" pattern="yyyy年MM月dd日 HH时mm分ss秒"/></td>
		          </tr>
		        </c:forEach>
		        </tbody>
		      </table>
		    </div>
		    <div class="footer">
		      <div class="page">
		        <%@ include file="/jsp/pagination.jsp"%>
		      </div>
		    </div>
	   </form:form>
	   </div>
  </body>
</html>