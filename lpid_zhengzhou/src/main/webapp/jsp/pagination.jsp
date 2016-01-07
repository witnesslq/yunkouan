<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- 分页信息 -->
<c:if test="${page.totalPage gt 1}">
	<div align="right" style="">
		<div>
			当前： 第${page.currentPage }页  共${page.totalPage }页 | 
			<c:if test="${page.hasPrevious}">
			<img src="${pageContext.request.contextPath}/resources/images/navLeft.gif" width="14" height="16" alt="第一页" onclick="gotoPage(1,${page.totalPage })"/>
			<img src="${pageContext.request.contextPath}/resources/images/left.gif" width="16" height="16" alt="上一页"  onclick="gotoPage(${page.previousPage },${page.totalPage })"/>
			</c:if>
			<c:if test="${page.hasNext}">
			<img src="${pageContext.request.contextPath}/resources/images/right.gif" width="16" height="16"  alt="下一页" onclick="gotoPage(${page.nextPage},${page.totalPage})"/>
			<img src="${pageContext.request.contextPath}/resources/images/navRight.gif" width="14" height="16"  alt="最后一页" onclick="gotoPage(${page.totalPage},${page.totalPage})"/> 
			</c:if>
			<input type="text" style="width:25px;text-align:center;" value="${page.currentPage}" id="toPageNum" onkeydown="if(event.keyCode==13){gotoPage(document.getElementById('toPageNum').value);}"/>
			<input type="button" class="button" value="GO"  onclick="gotoPage(document.getElementById('toPageNum').value,${page.totalPage})"/>
		
			<script type="text/javascript">
				function gotoPage(pageNum,maxval) {
					if(!/^[1-9]+[0-9]*]*$/.test(pageNum)) {
						alert("请输入有效页码");
						return false;
					}
					if(pageNum>maxval) {
						alert("请输入有效页码");
						return false;
					}
					document.getElementById('recodepage').value=pageNum;
					var form = document.getElementById('recodepage').form;
					form.submit();
				}
			</script>
		</div>
	</div>
</c:if>
<input id="recodepage" type="hidden" name="currentPage" value="${page.currentPage }" >