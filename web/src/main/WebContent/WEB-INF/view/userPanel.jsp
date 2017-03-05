<!--  userPanel.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<div class="current-date">
	Сегодня:
	<script type="text/javascript">
		var d = new Date();
		var curr_date = d.getDate();
		var curr_month = d.getMonth() + 1;
		var curr_year = d.getFullYear();
		document.write(curr_year + "-" + curr_month + "-" + curr_date);
	</script>
</div>
<div class="system-message">
	<c:forEach var="msg" items="${message}">
		<c:out value="${msg}" />
		<%--<s:message code="${msg}"/>--%>
	</c:forEach>

</div>
<div class="current-user">
	Пользователь ID:
	<c:out value="${account.user.userId}" />
	| LOGIN:
	<c:out value="${account.user.login}" />
	| ROLE:
	<c:out value="${account.user.role}" />
</div>
<div class="logout">
	<table class="login" >
		<tr>
			<td align="center"><a href="?locale=ru">RU</a></td>
			<td align="center"><a href="?locale=en">EN</a></td>
			<td align="center"></td>
			<td><form name="logout" action="${domen}/logout" method="get">
				<button name="LOGOUT" type="submit" value="LOGOUT">Выйти из системы</button>
			</form></td>
		</tr>
	</table>
</div>
<!--  userPanel.jsp -->