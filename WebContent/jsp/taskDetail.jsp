<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/jsp/header.jsp"%>
<!--  <b>/jsp/user.jsp | мэйн</b><br /> -->
<div class="content">
	<div class="main">
		<div class="task">
			<h2>
				Название: &quot;
				<c:out value="${curTask.title}" />
				&quot;
			</h2>
			<div class="task-body">
				<p>
					№ задачи в системе:
					<c:out value="${curTask.id}" />
					<br /> Ответственный:
					<c:out value="${curTaskMeta.userId}" />
					<br />Дата создания:
					<c:out value="${account.dateFormat.format(curTask.dateCreation.getTime())}" />
					<br /> Дедлайн:
					<c:out value="${account.dateFormat.format(curTask.deadline.getTime())}" />
					<br /> Статус:
					<c:out value="${curTaskMeta.statusId}" />
				</p>
				<h2>Описание задачи</h2>
				<p>
					<c:out value="${curTask.body}" />
				</p>
				<h2>История изменений</h2>
				<p>
					<c:forEach items="${curTask.getStringHistory()}" var="hist">
						<c:out value="${hist}" />
						<br />
					</c:forEach>
				</p>
				<p>
					<button name="TASK_DETAIL" type="submit" value="${task.key}">Смена статуса</button>
					<button name="" type="submit" value="${task.key}">Редактировать (если статус новое либо права)</button>
					<button name="" type="submit" value="${task.key}">Внести правку (статус 2+)</button>
					<button name="" type="submit" value="">На страницу задач(не раб)</button>
					<button name="" type="submit" value="">К списку задач(не раб)</button>
					<br /> * в зависимости от владельца ака
				</p>
				<br /> <br /> <br /> <br />
			</div>
		</div>
		<div class="task-list">
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
					</tr>
					<c:forEach items="${account.currentTasks}" var="task">
						<!--  				-->
						<tr id="<c:out value="${task.key}" />">
							<td><c:out value="${task.value.id}" /></td>
							<td><c:out value="${account.dateFormat.format(task.value.dateCreation.getTime())}" /></td>
							<td><c:out value="${task.value.title}" /></td>
							<td><c:out value="${account.tasksMeta[task.key].statusId}" /></td>
							<td><c:out value="${account.dateFormat.format(task.value.deadline.getTime())}" /></td>
							<td><button name="TASK_DETAIL" type="submit" value="${task.key}">Детали</button></td>
							<td></td>

						</tr>
					</c:forEach>
				</table>
			</form>
		</div>
	</div>
	<%@ include file="/jsp/addNewTask.jsp"%>

</div>
<%@ include file="/jsp/statuslist.jsp"%>
<br />
<br />
<p>tdis.id = 0; this.title = title; this.dateCreation.setTime(new Date()); this.body = body; this.status = 1; this.deadline = deadline;
	this.history = null; ps: Логины 1-5 ** Пароли 1112 ** Админы 1, 5</p>
<!-- end main.jsp  -->
<%@ include file="/jsp/footer.jsp"%>