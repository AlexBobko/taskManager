<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/jsp/header.jsp"%>
	<!--  /jsp/user.jsp -->
<div class="content">
	<c:out value="${errorMessage}" />
	<h2>
		Список задач &quot;
		<c:out value="${currentUser.id}" />
		&quot;
	</h2>
		<p><c:out value="${currentUser.id}" /></p>
	<form name="user-form" action="go" method="post">
		<button name="LOGOUT" type="submit" value="LOGOUT">Выйти из системы</button>
			<table>
				<tr>
					<th>номер</th>
					<th>Дата создания</th>
					<th>Заголовок</th>
					<th>Описание</th>
					<th>Статус</th>
					<th>Дедлайн</th>
					<th>История</th>
				</tr>
				<c:forEach var="task" items="${tasks}">
					<tr>
						<td><c:out value="${task.id}" /></td>
						<td><c:out value="${task.dateCreation}" /></td>
						<td><c:out value="${task.title}" /></td>
						<td><c:out value="${task.body}" /></td>
						<td><c:out value="${task.deadline}" /></td>
						<td><c:out value="${task.history}" /></td>
					</tr>
				</c:forEach>
			</table>
	</form>
</div>

<c:forEach items="${account.taskMeta}" var="meta">
				<c:out value="${meta.key}" />
				</c:forEach>


<c:set var="number1" value="${222}" /> 
<c:set var="number2" value="${12}" /> 
<c:set var="number3" value="${10}" /> 

<c:choose>
								<c:when test="${number1 < number2}">
					     ${"number1 is less than number2"}
					 </c:when>
								<c:when test="${number1 <= number3}">
					     ${"number1 is less than equal to number2"}
					 </c:when>
								<c:otherwise>
					     ${"number1 is largest number!"}
					 </c:otherwise>
							</c:choose>
							
							
							 <c:choose>
								<c:when test="${task.value.status}">
									<button name="PRODUCTION" type="submit" value="${card.id}">В работу</button>
								</c:when>
								<c:otherwise>
									<td>Карта заблокирована</td>
									<td>Нет опций</td>
								</c:otherwise></c:choose>
<br />
<br />
<p>
		this.id = 0;
		this.title = title;
		this.dateCreation.setTime(new Date());
		this.body = body;
		this.status = 1;
		this.deadline = deadline;
		this.history = null;
ps: Логины 1-5 ** Пароли 1112 ** Админы 1, 5
</p>
<!-- end main.jsp  -->	
<%@ include file="/jsp/footer.jsp"%>