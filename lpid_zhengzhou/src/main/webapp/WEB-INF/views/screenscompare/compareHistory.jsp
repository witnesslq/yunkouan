<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>同屏比对查验结果历史数据</title>
    <link rel='icon' href='${ctx}/resources/images/1.ico' type=‘image/x-ico’ />
    <script type="text/javascript" src="${ctx}/resources/lib/My97DatePicker/WdatePicker.js"></script>
  </head>
  <body>
  	<div class="container-fluid">
  		<div class="row">
  			<form id="resultForm" name="resultForm" action="${ctx}/screensCompareHistory/screensCompareHistoryList" method="post">
	  			<div class="search form-inline">
	  				<div class="form-group">
	  					<lable>时间：</lable>
						<input id="startDate" name="startDate" type="text" value="${queryModel.startDate}" class="form-control control-time" onclick="WdatePicker()"/> - 
						<input id="endDate" name="endDate" type="text" value="${queryModel.endDate}" class="form-control control-time" onclick="WdatePicker()"/>
	  				</div>
	  				<div class="form-group">
	  					<button class="btn btn-primary" type="submit">查询</button>
	  				</div>
	  			</div>
	  			<div class="history-main">
	  				<ul>
	  					<c:forEach var="resultInfoShow" items="${baggageList}" varStatus="it">
		  					<li class="col-md-2" data-id="${resultInfoShow.id}">
		  						<img src= "${picctx}/${resultInfoShow.photoRelativePath}" />
				                <p>时间：<fmt:formatDate value="${resultInfoShow.createTime}"  pattern="yyyy年MM月dd日 HH时mm分ss秒"/></p>
		  					</li>
	  					</c:forEach>
	  				</ul>
	  			</div>
	  			<div class="footer">
			      <div class="page">
			        <%@ include file="/jsp/pagination.jsp"%>
			      </div>
			    </div>
  			</form>
  		</div>
  	</div>
    <div class="popup-main">
    	<div class="popup-bg"></div>
    	<div class="popup-content">
    		<div class="popup-header">
                <h3>历史详情</h3>
                <button class="closes">×</button>
            </div>
            <div class="popup-body">
            	<h4 id="rfidNo"></h4>
                <ul class="history-detail">
                	<li class="col-md-6"><img width="100%" id="xrayImages1" alt="" src="" /></li>
                	<li class="col-md-6"><img width="100%" id="xrayImages2" alt="" src="" /></li>
                	<li class="col-md-6"><img width="100%" id="xrayImages3" alt="" src="" /></li>
                	<li class="col-md-6"><img width="100%" id="photoRelativePath" alt="" src="" /></li>
                </ul>
            </div>
            <!-- <div class="popup-footer">
                <button class="btn btn-default closes" type="button">关闭</button>
                <button class="btn btn-primary" type="button">保存</button>
            </div> -->
    	</div>
    </div>
    <script type="text/javascript" src="${ctx}/resources/js/biz/compareHistory.js"></script>
  </body>
</html>