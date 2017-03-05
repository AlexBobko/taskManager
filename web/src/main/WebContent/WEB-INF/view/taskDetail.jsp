<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/view/header.jsp" %>
<!-- <b>/jsp/user.jsp | мэйн</b><br /> -->
<div class="content">
    <div class="main">
        <div class="task">
            <div class="task-head">
                <h2>
                    Название: &quot;
                    <c:out value="${curTask.title}"/>
                    &quot;
                </h2>

                <p>
                    № задачи в системе:
                    <c:out value="${curTask.taskId}"/>
                    <br/> Ответственный:
                    <c:forEach items="${curTask.userList}" var="user">
                        <c:out value="${user.userId}"/>:
                        <c:out value="${user.login}"/><br/>
                    </c:forEach>
                    <br/>Дата создания:
                    <c:out value="${account.dateFormat.format(curTask.dateCreation)}"/>
                    <br/> Дедлайн:
                    <c:out value="${account.dateFormat.format(curTask.deadline)}"/>
                    <br/> Статус:
                    <c:out value="${statuses[(1).intValue()]}"/><br/>
                    <c:out value="${curTask.statusId}"/>
                </p>

                <p>
                    <c:choose>
                    <c:when test="${account.user.role == 1}">

                <form name="new-task" action="${domen}/user/tasks/newtask" method="get">
                    <button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
                </form>
                <form name="task-list" action="${domen}/user/tasks" method="get">
                    <button name="GO_TASK_LIST" type="submit" value="GO_TASK_LIST">К списку задач</button>
                </form>
                </c:when>
                <c:when test="${account.user.role == 2}">
                    <form name="new-task" action="${domen}/superior/tasks/newtask" method="get">
                        <button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
                    </form>
                    <form name="task-list" action="${domen}/superior/tasks" method="get">
                        <button name="GO_TASK_LIST" type="submit" value="GO_TASK_LIST">К списку задач</button>
                    </form>
                    <form name="task-list" action="${domen}/superior/tasks" method="post">
                        <button name="TASK_DEL" type="submit" value="${curTask.taskId}" style="color: red">Удалить</button>
                    </form>
                    <a href="${domen}/superior/tasks/assignment/${curTask.taskId}">Назначить приём</a>
                </c:when></c:choose>

                </p>
            </div>
            <div class="task-body">
                <h2>Описание задачи</h2>

                <p>
                    <c:out value="${curTask.content.body}"/>
                </p>
            </div>
            <div class="task-correct">
                <form name="task-form" action="" method="post">
                    <h3>Корректировка к описанию задачи</h3>

                    <p>
                        <textarea name="bodyTask" cols="80" rows="10"
                                  placeholder="Корректировка к описанию задачи"></textarea>
                    </p>
                    <button name="TASK_UPDATE" type="submit" value="TASK_UPDATE">Внести корректировку</button>
                </form>
            </div>
            <div class="task-history">
                <h2>История изменений</h2>

                <p>
                    <c:out value="${curTask.content.history}"/>
                </p>
            </div>
        </div>
    </div>
</div>
<!-- end main.jsp -->
<%@ include file="/WEB-INF/view/footer.jsp" %>