<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/jsp/header.jsp"%>
<!--  admin.jsp | админ -->
<div class="content">
	<c:out value="${errorMessage}" />
	<h2>
		Страница администратора с логином &quot;
		<c:out value="${currentUser.id}" />
		&quot;
	</h2>
	<form name="user-form" action="go" method="post">
		<button name="LOGOUT" type="submit" value="LOGOUT">Выйти из системы</button>
		<h3>Список заблокированных карт</h3>
		<table>
			<tr>
				<th>Номер карты</th>
				<th>Блокировка</th>
				<th>Владелец</th>
			</tr>
			<c:forEach var="card" items="${currentUser.cards}">
				<tr>
					<td><c:out value="${card.id}" /></td>
					<td>
					<c:choose>
						<c:when test="${!card.status}">
					<button name="CARD_UNBLOCKING" type="submit" value="${card.id}">Разблокировать карту</button>
						</c:when>
						<c:otherwise>
						Карта разблокирована
						</c:otherwise>
					</c:choose>
					</td>
					<td><c:out value="${card.userId}" /></td>
				</tr>
			</c:forEach>
		</table>
	</form>
</div>
<%@ include file="/jsp/footer.jsp"%>