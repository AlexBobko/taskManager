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
				<button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
				<button name="GO_TASK_LIST" type="submit" value="GO_TASK_LIST">К списку задач</button>
				<h3>Добавить к описанию задачи</h3>
				<p>
					<textarea name="bodyTask" cols="80" rows="10" placeholder="Корректировка к описанию задачи"></textarea>
				</p>
				<button name="TASK_CORRECT" type="submit" value="TASK_CORRECT">Внести корректировку</button>
			</form>
		</div>
	</div>
</div>
<br />
<br />
<!-- end main.jsp  -->
<%@ include file="/jsp/footer.jsp"%>