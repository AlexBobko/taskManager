<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="status-list">
	1 - Создано (новый) - еще можно редактировать<br />
	2 - На утверждении - ждет утвеждения для начала работы<br /> 
	3 - В работе<br />
	 4 - На	проверке - задание передано на проверку руководителю для назначения времени приема<br /> 
	 5 - К сдаче - назначено время приема<br />
	  6 - Выполнено -
	задача закрывается и уходит в архив<br />
</div>
<button name="VIEW_HISTORY" type="submit" value="${task.key}">История</button>
<button name="APPROVE_TASK" type="submit" value="${task.key}">Утвердить задание</button>
<button name="PRODUCTION" type="submit" value="${task.key}">В работу</button>
<button name="FOR_CHECKING" type="submit" value="${task.key}">На проверку</button>
<button name="PAY_TASK" type="submit" value="${task.key}">Отправить назн время</button>
<button name="TASK_READY" type="submit" value="${task.key}">Принять</button>
<!--  -->
<p>tdis.id = 0; this.title = title; this.dateCreation.setTime(new Date()); this.body = body; this.status = 1; this.deadline = deadline;
	this.history = null; ps: Логины 1-5 ** Пароли 1112 ** Админы 1, 5</p>