<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/jsp/header.jsp"%>
<!--  user.jsp -->
<div class="content">
	<div class="main">
		<h2>Список текущих задач</h2>
		<form name="user-form" action="go" method="post">
			<table class="tasks">
				<tr>
					<th>номер</th>
					<th>Дата создания</th>
					<th>Заголовок</th>
					<th>Статус</th>
					<th>Дедлайн</th>
					<th>История</th>
					<th>Действие</th>
					<th>Действие2</th>
				</tr>
				<c:forEach items="${account.currentTasks}" var="task">
					<tr id="<c:out value="${task.key}" />"><!--  				-->
						<td><c:out value="${task.value.id}" /></td>
						<td><c:out value="${account.dateFormat.format(task.value.dateCreation.getTime())}" /></td>
						<td><c:out value="${task.value.title}" /></td>
						<td><c:out value="${account.tasksMeta[task.key].statusId}" /></td>
						<td><c:out value="${account.dateFormat.format(task.value.deadline.getTime())}" /></td>
						<td><button name="TASK_DETAIL" type="submit" value="${task.key}">Детали</button>
						<button name="VIEW_HISTORY" type="submit" value="${task.key}">История</button></td>
						<td>
						<button name="APPROVE_TASK" type="submit" value="${task.key}">Утвердить задание</button>
						<button name="PRODUCTION" type="submit" value="${task.key}">В работу</button>
							<td><button name="FOR_CHECKING" type="submit" value="${task.key}">На проверку</button>
							<button name="PAY_TASK" type="submit" value="${task.key}">Отправить назн время</button>
							<button name="TASK_READY" type="submit" value="${task.key}">Принять</button></td> <!--  -->
					</tr>
				</c:forEach>
			</table>
		</form>
	</div>
</div>
<br />
<br />
<!-- end main.jsp  -->
<%@ include file="/jsp/footer.jsp"%>