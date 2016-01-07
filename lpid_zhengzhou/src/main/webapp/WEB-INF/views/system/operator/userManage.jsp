<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html class="scroll" lang="en" ng-app="myApp">
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <script type="text/javascript" src="${ctx}/resources/js/common.js" ></script>
</head>
<body class="scroll">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <div class="device-main">
                    <h3>用户管理</h3>
                    <div class="device-table">
                        <div class="add-btn">
                            <button class="add-operator">
                                <i class="icon-plus"></i>
                               		 添加
                            </button>
                        </div>
                        <table class="table table-bordered record-list">
                            <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>登录名</th>
                                    <th>所属角色</th>
                                    <th>最后登录时间</th>
                                    <th>E-mail</th>
                                    <th>真实姓名</th>
                                    <!-- <th>频道</th> -->
                                    <th>管理操作</th>
                                </tr>
                            </thead>
                            <tbody>
                               <c:forEach var="operator" items="${operatorList}" varStatus="it">
                                   <tr>
                               	       <td><c:out value="${it.count}"/></td>
                               	   	   <td>${operator.loginName}</td>
                               	   	   <td>
						                <c:forEach var="role" items="${operator.roles}"> 
						                  ${role.name} &nbsp;&nbsp;
						                </c:forEach>
						                </td>
						                <td>
						                	<fmt:formatDate value="${operator.lastLoginTime}" pattern="yyyy年MM月dd日 HH时mm分ss秒"/>
						                </td>
						                <td>${operator.email}</td>
						                <td>${operator.name}</td>
						                <%-- <td>${operator.channel}</td> --%>
						                <td>
						                  <a href="javascript:void(0);" class="operation">角色设置</a> | 
						                  <a class="modify-operator" href="javascript:void(0);">修改</a> | 
						                  <a class="reset-password" href="#">密码重置</a> | 
						                  <a class="delete" href="javascript:void(0);">删除</a>
						                </td>
						                <input class="operatorId" name="id" type="hidden" value="${operator.id}" >
                               	   </tr>
                               </c:forEach>
                            </tbody>
                        </table>
                        <form id="resultForm" name="resultForm" action="${ctx}/operator/initoperator" method="post" target="main-frame">
							<div class="footer">
						      <div class="page">
						        <%@ include file="/jsp/pagination.jsp"%>
						      </div>
						    </div>
					    </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--角色设置-->
      <div class="popup-main-left" id="purviewSetting" style="display: none; width:400px;">
        <h4>全选/取消</h4>
        <ul id="permissionAllot">
            
        </ul>
        <button class="btn btn-primary" id="roleCommit">确定</button>
        <button class="btn btn-default close-lg" type="button">取消</button>
      </div>
      <!--修改-->
      <div class="revise" id="addModifyOperator" style="display: none;">
        <form id="addEditOperatorForm" name="addEditOperatorForm" action="" method="post" target="main">
        <ul>
          <li><font>登录名：</font>
            <div class="modify-role">
	          <input class="form-control" type="text" id="loginName" name="loginName"/>
	        </div>
          </li>
          <li><font>真实姓名：</font>
            <div class="modify-role">
	          <input class="form-control required" type="text" id="name" name="name"/>
	        </div>
          </li>
          <li><font>E-mail：</font>
            <div class="modify-role">
	          <input class="form-control required" type="text" id="email" name="email"/>
	        </div>
          </li>
          <!-- 
          <li><font>频道：</font>
            <div class="modify-role">
	          <select class="form-control" id="channel" name="channel">
                <option value="A">A</option>
                <option value="B">B</option>
              </select>
	        </div>
          </li>
           -->
          <li class="my-password"><font>密码：</font>
            <div class="modify-role">
	          <input class="form-control required" type="password" id="password" name="password"/>
	        </div>
          </li>
          <li class="my-password"><font>确认密码：</font>
            <div class="modify-role">
	          <input class="form-control required" type="password" id="repeatPassword" name="repeatPassword"/>
	        </div>
          </li>
        </ul>
        <div class="login-footer revise-footer" style="padding:0;">
	      <button style="margin-left:46px;" class="btn btn-primary" id="operatorCommit" type="button">确定</button>
	      <button class="btn btn-default close-lg" type="button">取消</button>
	    </div>
        <input type="hidden" name="id" id="operatorId" value="-1"/>
      </form>
    </div>
    <!-- 重置密码 -->
    <div class="revise" id="resetPassword" style="display: none;">
      <form id="resetPasswordForm" name="resetPasswordForm" action="" method="post" target="main"> 
 	    <ul>
   	      <li><font>新密码：</font>
	   	    <div class="modify-role">
	          <input class="form-control" type="password" name="password"/>
	        </div>
   	      </li>
 	    </ul>
 	    <div class="login-footer revise-footer">
	      <button class="btn btn-primary" id="submitResetPwd" type="button">确定</button>
	      <button class="btn btn-default close-lg" type="button">取消</button>
	    </div>
        <input type="hidden" name="id" id="operatorId" value="-1"/>
      </form>
    </div>
    <!-- 删除确认 -->
    <div class="delete-content" style="width:230px;">
    </div>
    <script type="text/javascript">
        //删除操作员
        function deleteOperator(obj) {
            if(confirm("确定要删除该操作员吗？")){
                return true;
            }
            return false;
        }
       
        $(function(){
            var trElem;
            var addupdate = null; //0新增操作员，1是修改操作员
           
            function loginName(current){
               //验证角色登录名
                if(addupdate == 1){
                	return false;
                }
                var loginNameValue = $("#loginName").val();
                var $parent = $(current).parents("li");
                if( loginNameValue == "" || loginNameValue == null ){
                    var errorNull = '登录名称不能为空';
                    $parent.find(".formtips").remove();
                    $parent.prepend('<p class="formtips onError">' + errorNull + '</p>');
                    return false;
                }else if(loginNameValue.length > 20){
                    var errorMsg = '登录名称长度不能大于20位.';
                    $parent.find(".formtips").remove();
                    $parent.prepend('<p class="formtips onError">' + errorMsg + '</p>');
                    return false;
                }
                
                //角色名是否已存在验证
                var checkRepeatOperatorNameResult = true;
                $.ajax({
                    url:"${ctx}/operator/checkLoginName?loginName=" + $("#loginName").val(),
                    type:"POST",
                    async:false,
                    dataType:"text", 
                    success:function(data){
                       if(data != "" && data != null){
                            if(data == 2){
                                var errorRepeat = '登录名已存在，请重新输入登录名！';
                                $parent.find(".formtips").remove();
                                $parent.prepend('<p class="formtips onError">' + errorRepeat + '</p>');
                                checkRepeatOperatorNameResult =  false;
                            }
                       }
                    },
                    error:function(err) {
                        console.log("判断用户名是否重复时发生错误:" + err);
                    }
               });
                
               if(!checkRepeatOperatorNameResult){
            	   return false;
               }
               var okMsg = '输入正确.';
               $parent.find(".formtips").remove();
               $parent.prepend('<p class="formtips onSuccess">'+okMsg+'</p>');
               return true;
            }
			
            
            $("#loginName").blur(function(){
            	 loginName(this);
            })
            
            
            $('.required').blur(function(){
                var $parent = $(this).parents("li");
                $parent.find(".formtips").remove();
                
                //验证真实姓名
                if( $(this).is('#name') ){
                    if( this.value == "" ||  this.value == null){
                        var errorNull = '真实姓名不能为空';
                        $parent.prepend('<p class="formtips onError">'+errorNull+'</p>');
                        return false;
                    }else if(this.value.length > 20){
                        var errorMsg = '真实姓名长度不能大于20';
                        $parent.prepend('<p class="formtips onError">'+errorMsg+'</p>');
                        return false;
                    }else{
                        var okMsg = '输入正确';
                        $parent.prepend('<p class="formtips onSuccess">'+okMsg+'</p>');
                    }
                }
                
              //验证邮箱
              if( $(this).is('#email') ){
                  if(this.value.length > 50){
	               	   		var errorMsg = '邮箱长度不能大于50';
	                     	$parent.prepend('<p class="formtips onError">'+errorMsg+'</p>');
                      	 return false;
                  }else if((this.value!="" && !/.+@.+\.[a-zA-Z]{2,4}$/.test(this.value) )){
	                	    var errorCorrect = '请输入正确的E-Mail地址.';
	                      $parent.prepend('<p class="formtips onError">'+errorCorrect+'</p>');
                  }else{
                        var okMsg = '输入正确.';
                        $parent.prepend('<p class="formtips onSuccess">'+okMsg+'</p>');
                  }
               }
              if(addupdate == 0){
            	  //验证密码
                  if( $(this).is('#password') ){
                      if( this.value == "" ||  this.value == null){
                            var errorNull = '密码不能为空';
                            $parent.prepend('<p class="formtips onError">'+errorNull+'</p>');
                            return false;
                      }else if(this.value.length > 20){
                   	   		 var errorMsg = '密码长度不能大于20';
                         	 $parent.prepend('<p class="formtips onError">'+errorMsg+'</p>');
                          	 return false;
                      }else{
                            var okMsg = '输入正确.';
                            $parent.prepend('<p class="formtips onSuccess">'+okMsg+'</p>');
                      }
                   }
                  
                	//验证确认密码
                  if( $(this).is('#repeatPassword') ){
                	  var password = $("#password").val();
                      if( this.value == "" ||  this.value == null){
                            var errorNull = '密码不能为空';
                            $parent.prepend('<p class="formtips onError">'+errorNull+'</p>');
                            return false;
                      }else if( this.value != password ){
    	                	  	var errorUnequal = '两次密码输入不一致，请重新输入';
    	                      $parent.prepend('<p class="formtips onError">'+errorUnequal+'</p>');
    	                      return false;
                      }else{
                            var okMsg = '输入正确.';
                            $parent.prepend('<p class="formtips onSuccess">'+okMsg+'</p>');
                      }
                   }
              }
           }).keyup(function(){
              $(this).triggerHandler("blur");
           }).focus(function(){
                $(this).triggerHandler("blur");
           }); //end blur
            
           //增加会员提交注册事件执行的函数
           function addOperatorHandle() {
               $("#loginName").trigger('blur');
               $(".required").trigger('blur');
               var numError = $('.onError').length;
               if(numError){
                   return false;
               } 
        	   var commitForm = $("#addEditOperatorForm");
               commitForm.attr("action","${ctx}/operator/addoperator");
               commitForm.submit();
           }
           
           //新增操作员
           $(".add-operator").click(function(){ //点击修改操作员/添加操作员弹窗
        	   $("#loginName").removeAttr("readonly");
               addupdate = 0;
               tooltipElem({
                   addContent : "#addModifyOperator", //修改操作员/添加操作员
                   title : "添加操作员"
                });
                $(".my-password").show();
                $("#addEditOperatorForm input").val("");
              
                //提交增加
                $("#operatorCommit").get(0).onclick = addOperatorHandle;
           });
            
            
            //修改操作员
            $(".modify-operator").click(function(){ //修改操作员/添加操作员
            	$("#loginName").attr("readonly","readonly");
            	addupdate = 1;
                tooltipElem({
                    addContent : "#addModifyOperator", //修改操作员/添加操作员
                    title : "修改操作员"
                });
                $(".my-password").hide();
           
                var updateTr = $(this).closest("tr");
                $("#loginName").val($("td:nth-child(2)", updateTr).text());
                $("#email").val($("td:nth-child(5)", updateTr).text());
                $("#name").val($("td:nth-child(6)", updateTr).text());
                $("#channel").val($("td:nth-child(7)", updateTr).text());
                $("form#addEditOperatorForm #operatorId").val(updateTr.find(".operatorId").val());
                
                var commitForm = $("#addEditOperatorForm");
               
                //提交更新
                $("#operatorCommit").click(function(){
                  $(".required").trigger('blur');
                  var numError = $('.onError').length;
                  if(numError){
                      return false;
                  } 
                  commitForm.attr("action","${ctx}/operator/updateoperator");
                  commitForm.submit();
                });
            });
           
            //设置权限
            $(".operation").click(function(){ //点击修改角色/添加角色弹窗
                tooltipElem({
                    addContent : $("#purviewSetting"), //权限设置
                    title : "角色设置"
                });
                trElem = $(this).closest("tr");
                var operatorId = trElem.find(".operatorId").val();
               
                //请求后台图片处理
                $.ajax({
                    url:"${ctx}/operator/roletree?operatorId=" + operatorId,
                    type:"POST",
                    async:true,
                    dataType:"text", 
                    success:function(data){
                        if(data != '' && data != null){
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
           
            //权限修改
            $("#roleCommit").click(function(){
           	    var updateRole = "";
                /** 格式：Object { id=1, name="游戏管理", open=true, 更多...}, **/
                var roleArray = "[";
                //nodes的内容形式：表单验证通过时，表单可以提交，否则不能提交
                $("#permissionAllot li").each(function(){
                    var roleFirstLevel = $(this).find(".pitchon");
                    //一级菜单选中
                    if(roleFirstLevel.is(":checked")) {
                        roleArray += '{ "id"=' + roleFirstLevel.attr("data-id") + ',';
                        roleArray += ' "name"= "' + $(roleFirstLevel).next().text() + '"},';
                        updateRole += $(roleFirstLevel).next().text() + "  ";
                    }
                });
                
                if(roleArray.indexOf(",") > 0) {
                    roleArray = roleArray.substring(0, roleArray.length - 1);
                }
                roleArray += "]";
               
                //请求后台图片处理
                $.ajax({
                    url:"${ctx}/operator/updateOperatorRole?operatorId=" + trElem.find(".operatorId").val() + 
                            "&roleArray=" + roleArray + "",
                    type:"POST",
                    async:false,
                    dataType:"text", 
                    success:function(data){
                        $("td:nth-child(3)", trElem).text(updateRole);
                        closeTooltip();
                    },
                    error:function(err) {
                        console.log("修改角色时发生错误:" + err);
                    }
                 });
             }); 
            
             //重置密码
             $(".reset-password").click(function(){
                 tooltipElem({
                     addContent : "#resetPassword", //重置密码
                     title : "重置密码"
                 });
                 trElem = $(this).closest("tr");
                 var operatorId = trElem.find(".operatorId").val();
                
                 var resetPasswordForm = $("#resetPasswordForm");
                
                 //提交更新
                 $("#submitResetPwd").click(function(){
                     $("#operatorId", resetPasswordForm).val(operatorId);
                	
                	 resetPasswordForm.attr("action","${ctx}/operator/resetPassword");
                	 resetPasswordForm.submit();
                 });
             });
            
             //删除操作员
             $(".delete").click(function(){
                 //点击的元素
                 var that = this;
                 tooltipElem({
                     addContent : ".delete-content",
                     conformText:"您确定要删除此条记录吗？", 
                     title : "提示"
                 });
                 
                 //点击确认按钮
                 $(".delete-content .btn-primary").click(function(){
                     var href = "${ctx}/operator/deleteoperator?operatorId=" + $(that).parent().next().val();
                     window.location.href = href;
                 });
             });
        });
    </script>
</body>
</html>