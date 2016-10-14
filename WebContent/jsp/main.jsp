<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/jsp/header.jsp"%>
<p>
	<b>тестовая страничка</b>
</p>
<div class="content">
<c:out value="${errorMessage}" />
	<h2>
		Список счетов клиента &quot;
		<c:out value="${currentUser.id}" />
		&quot;
	</h2>
	<form name="user-form" action="go" method="post">
		<button name="LOGOUT" type="submit" value="LOGOUT">Выйти из системы</button>

		<c:forEach var="account" items="${currentUser.accounts}">
			<p>
				Счет №: <b><c:out value="${account.id}" /></b> . Балланс:
				<c:out value="${account.ballance}" />
				руб. <b>Пополнение счета:</b>   <input name="account_${account.id}" type="text" value="" /> руб. 
				<button name="ACCOUNT_RECHARGE" type="submit" value="${account.id}" >Пополнить</button>
				<br /> <b>Список карт счета №<c:out value="${account.id}" /></b>
			<table>
				<tr>
					<th>Номер карты</th>
					<th>Блокировка</th>
					<th>Другие действия</th>
				</tr>
				<c:forEach var="card" items="${account.cards}">
					<tr>
						<td><c:out value="${card.id}" /></td>
						<c:choose>
								<c:when test="${card.status}">
								<td><button name="CARD_BLOCKING" type="submit" value="${card.id}" >Заблокировать карту</button>
									</td>
						<td>Произвести платеж на сумму: <input name="${card.id}" type="text" class="input username" value="" />
						<button name="CREATE_PAYMENT" type="submit" value="${card.id}">Отправить платеж</button>
						</td>
						</c:when> 
						<c:otherwise><td>Карта заблокирована</td>
						<td>Нет опций</td></c:otherwise> </c:choose>
					</tr>
				</c:forEach>
			</table>
		</c:forEach>
	</form>
</div>
<%@ include file="/jsp/footer.jsp"%>