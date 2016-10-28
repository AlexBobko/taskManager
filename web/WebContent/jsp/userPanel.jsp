<!--  userPanel.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	</c:forEach>
</div>
<div class="current-user">
	Пользователь ID:
	<c:out value="${account.user.id}" />
	| LOGIN:
	<c:out value="${account.user.login}" />
	| ROLE:
	<c:out value="${account.user.role}" />
</div>
<div class="logout">
	<form name="logout" action="go" method="post">
		<button name="LOGOUT" type="submit" value="LOGOUT">Выйти из системы</button>
	</form>
</div>
<!--  userPanel.jsp -->