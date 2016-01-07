<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html class="scroll" lang="en" ng-app="myApp">
<head>
    <meta charset="UTF-8">
    <title>角色管理</title>
    <script type="text/javascript" src="${ctx}/resources/js/common.js" ></script>
    <link rel="stylesheet" href="${ctx}/resources/js/verify/css/style.css" type="text/css" />
</head>
<body class="scroll" ng-controller="userController">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <div class="device-main">
                    <h3>角色管理</h3>
                    <div class="device-table">
                        <div class="add-btn">
                            <button id="addrole">
                                <i class="icon-plus"></i>
                                添加
                            </button>
                        </div>
                        <table class="table table-bordered record-list">
                            <thade>
                                <tr>
                                    <th>序号</th>
                                    <th>角色名称</th>
                                    <th>角色描述</th>
                                    <th>管理操作</th>
                                </tr>
                            </thade>
                            <tbody>
                                <c:forEach var="role" items="${roleList}" varStatus="it">
						            <tr>
						              <td><c:out value="${it.count}"/></td>
						              <td>${role.name}</td>
						              <td>${role.description}</td>
						              <td>
						                <a class="operation" href="javascript:void(0);">权限设置</a> | 
						                <a class="modify" href="javascript:void(0);">修改</a> | 
						                <a class="delete" href="javascript:void(0);">删除</a>
						              </td>
						              <input class="roleId" type="hidden" value="${role.id}" />
						            </tr>
						          </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--权限设置-->
    <div class="popup-main-left" id="purviewSetting" style="display: none; width:400px;">
      <h4>全选/取消</h4>
      <ul id="permissionAllot"></ul>
      <button id="authCommit" class="btn btn-primary">确定</button>
      <button class="btn btn-default close-lg"  type="button">取消</button>
    </div>
    <!--新增角色-->
    <div class="revise" id="addRolePopup" style="display: none;">
      <form id="addRoleForm" name="addRoleForm" action="" method="post" target="main">
      <ul>
        <li>
            <!-- <p class="errRoleName"></p> -->
          <font>角色名称：</font>
          <div class="modify-role">
            <input class="form-control" type="text" id="roleName" name="roleName"/>
          </div>
        </li>
        <li>
            <!-- <p class="errRoleDescribe"></p> -->
          <font style="vertical-align: top;">角色描述：</font>
          <div class="modify-role">
            <textarea class="form-control required" id="roleDescribe" name="roleDescribe"></textarea>
          </div>
        </li>
      </ul>
      <div class="login-footer">
        <button id="addRole" class="btn btn-primary" type="button">确定</button>
        <button class="btn btn-default close-lg" type="button">取消</button>
      </div>
      </form>
    </div>
    <!-- 修改角色 -->
    <div class="revise" id="modifyRolePopup" style="display: none;">
      <form id="modifyRoleForm" name="modifyRoleForm" action="" method="post" target="main">
      <ul>
        <li>
            <!-- <p class="errRoleName"></p> -->
          <font>角色名称：</font>
          <div class="modify-role">
            <input class="form-control" type="text" id="updateRoleName" name="roleName"/>
          </div>
        </li>
        <li>
            <!-- <p class="errRoleDescribe"></p> -->
          <font style="vertical-align: top;">角色描述：</font>
          <div class="modify-role">
            <textarea class="form-control required" id="updateRoleDescribe" name="roleDescribe"></textarea>
          </div>
        </li>
      </ul>
      <div class="login-footer">
        <button id="modifyRole" class="btn btn-primary" type="button">确定</button>
        <button class="btn btn-default close-lg" type="button">取消</button>
      </div>
      <input type="hidden" name="roleId" id="roleIdHidden" value="-1"/>
      </form>
    </div>
    <!-- 删除确认 -->
    <div class="delete-content">
    </div>
    <script type="text/javascript" src="${ctx}/resources/js/verify/js/verify.js"></script>
    <script type="text/javascript">
        $(function(){
            var trElem;
            var beforModfiy = null;  //这个变量是为了取得修改前的值，用于和修改后的值做比较
            function roleName(current,type){
            	var roleNameValue = null;
            	if(type == "add"){
            		roleNameValue = $("#roleName").val();
            	}else{
            		roleNameValue = $("#updateRoleName").val();
            	}
                
                var $parent = $(current).parents("li");
                //验证角色名称是否为空
                if(roleNameValue == "" || roleNameValue == null){
                    var errorNull = '角色名称不能为空';
                    $parent.find(".formtips").remove();
                    $parent.prepend('<p class="formtips onError">' + errorNull + '</p>');
                    return false;
                }else if(roleNameValue.length > 16){//长度验证
                    var errorMsg = '角色名称长度不能大于16位';
                    $parent.find(".formtips").remove();
                    $parent.prepend('<p class="formtips onError">' + errorMsg + '</p>');
                    return false;
                }
                
                //在修改时，如果角色名称没做修改，就返回
            	if(roleNameValue == beforModfiy) { 
            		return true;
            	}
            	
                //角色名是否已存在验证
            	var checkRepeatRoleNameResult = true;
                $.ajax({
                  url:"${ctx}/role/checkRoleName?roleName=" + roleNameValue,
                  type:"POST",
                  async:false,
                  dataType:"text", 
                  success:function(data){
                      if(data != "" && data != null){
                          if(data == 2){
                              var errorRepeat = '角色名已存在，请重新输入角色名！';
                              $parent.find(".formtips").remove();
                              $parent.prepend('<p class="formtips onError">' + errorRepeat + '</p>');
                              checkRepeatRoleNameResult = false;
                          }
                      }
                  },
                  error:function(err) {
                      console.log("判断角色名是否重复时发生错误:" + err);
                  }
              }); 
              
              if(!checkRepeatRoleNameResult) {
            	  return false;
              }
              
              var okMsg = '输入正确.';
              $parent.find(".formtips").remove();
              $parent.prepend('<p class="formtips onSuccess">'+okMsg+'</p>');
              
              return true;
          }//roleName(current)
          
          //新增角色验证名称
          $("#roleName").blur(function(){ 
              roleName(this,"add");
          });
          
          //修改角色验证名称
          $("#updateRoleName").blur(function(){ 
              roleName(this,"#updateRoleName","modify");
          });
          
          
          //提交新增加的角色
          function presentAddRole(){
              var commitForm = $("#addRoleForm");
              $("#roleName").trigger('blur');
              $(".required").trigger('blur');
              var numError = $('form .onError').length;
              if(numError > 0){
                 return false;
              } 
              commitForm.attr("action","${ctx}/role/addrole");
              commitForm.submit();     
          }
          
          
          //新增角色
          $("#addrole").click(function(){ //点击修改角色/添加角色弹窗
        	   beforModfiy = ""; //增加的时候原来的值清空
               $("#addEditRoleForm input").val(""); //每次点击新增先清空input的值
               $("#addEditRoleForm textarea").val(""); //每次点击新增先清空textarea的值
               tooltipElem({
                   addContent : "#addRolePopup", //添加角色
                   title : "新增角色"
               });
               $('.required').blur(function(){
                   var $parent = $(this).parents("li");
                   $parent.find(".formtips").remove();
                   //验证角色描述
                   if( $(this).is('#roleDescribe') ){
                       if(this.value.length > 128){
                           var errorMsg = '角色描述长度不能大于128.';
                           $parent.prepend('<p class="formtips onError">'+errorMsg+'</p>');
                           return false;
                       }else{
                           var okMsg = '输入正确.';
                           $parent.prepend('<p class="formtips onSuccess">'+okMsg+'</p>');
                       }
                   }
                 }).keyup(function(){
                     $(this).triggerHandler("blur");
                 }).focus(function(){
                     $(this).triggerHandler("blur");
                 });//end blur
            }); 
            
            
            //提交新增角色，最终验证。
            $('#addRole').click(function(){
                presentAddRole();
            }); 
            
            //修改提交更新
            function presentUpDate(){
            	$("#updateRoleName").trigger('blur');
                $(".required").trigger('blur');
                var numError = $('form .onError').length;
                if(numError > 0){
                   return false;
                } 
                var commitForm = $("#modifyRoleForm");
                commitForm.attr("action","${ctx}/role/updaterole");
                commitForm.submit();
            }
            //修改角色
            $(".modify").click(function(){ //点击修改角色/添加角色弹窗
                tooltipElem({
                    addContent : "#modifyRolePopup", //修改角色
                    title : "修改角色"
                });
                var updateTr = $(this).parents("tr");
                $("#updateRoleName").val(updateTr.find("td:nth-child(2)").text());
                $("#updateRoleDescribe").val(updateTr.find("td:nth-child(3)").text());
                $("#roleIdHidden").val(updateTr.find(".roleId").val());
                beforModfiy = updateTr.find("td:nth-child(2)").text(); //点击修改取得修改前的值
            });
            
            //提交更新,最终确认
            $("#modifyRole").click(function(){
                presentUpDate();
            });
            
            //设置权限
            $(".operation").click(function(){ //点击设置权限弹窗
                tooltipElem({
                    addContent : "#purviewSetting", //设置权限
                    title : "设置权限"
                });
                var value = $(this).text();
                $(".title").text(value);
                $("#roleModify").hide();
            
                trElem = $(this).closest("tr");
                var roleId = trElem.find(".roleId").val();
                
                //请求后台图片处理
                $.ajax({
                    url:"${ctx}/role/menutree?roleId=" + roleId,
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
                        console.log("请求权限菜单时发生错误:" + err);
                    }
                });
            });
            
            //权限设置提交
            $("#authCommit").click(function(){
                /** 格式：Object { id=1, name="游戏管理", open=true, 更多...}, **/
                var menuArray = "[";
                //nodes的内容形式：表单验证通过时，表单可以提交，否则不能提交
                $("#permissionAllot li").each(function(){
                    var menuFirstLevel = $(this).find(".pitchon");
                    //一级菜单选中
                    if(menuFirstLevel.is(":checked")) {
                        menuArray += '{ "id"=' + menuFirstLevel.attr("data-id") + ',';
                        menuArray += ' "name"= "' + $(menuFirstLevel).next().text() + '"},';
                        //遍历二级 
                        $("dd input", this).each(function(){
                            if($(this).is(":checked")) {
                                menuArray += '{ "id"=' + $(this).attr("data-id") + ',';
                                menuArray += ' "name"= "' + $(this).next().text() + '"},';
                            }
                        });
                    }
                });
                
                if(menuArray.indexOf(",") > 0) {
                    menuArray = menuArray.substring(0, menuArray.length - 1);
                }
                menuArray += "]";
                
                console.log(menuArray);
                
                //请求后台图片处理
                $.ajax({
                    url:"${ctx}/role/updateRoleAuth?roleId=" + trElem.find(".roleId").val() + 
                            "&menuArray=" + menuArray + "",
                    type:"POST",
                    async:false,
                    dataType:"text", 
                    success:function(data){
                        closeTooltip();
                    },
                    error:function(err) {
                        console.log("修改权限时发生错误:" + err);
                    }
                });
                
            });
            
            //删除角色
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
                    var href = "${ctx}/role/deleterole?roleId=" + $(that).parent().next().val();
                    window.location.href = href;
                });
            });
            
        });
    </script>
</body>
</html>