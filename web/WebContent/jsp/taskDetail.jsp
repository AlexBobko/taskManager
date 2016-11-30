<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/header.jsp" %>
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
                    <c:out value="${curTask.statusId}"/>
                </p>
                <p>
                <form name="tasks-form" action="go" method="post">
                    <button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
                    <button name="GO_TASK_LIST" type="submit" value="GO_TASK_LIST">К списку задач</button>
                </form>
                </p>
            </div>
            <div class="task-body">
                <h2>Описание задачи</h2>
                <p>
                    <c:out value="${curTask.content.body}"/>
                </p>
            </div>
            <div class="task-correct">
                <form name="user-form" action="go" method="post">
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
<%@ include file="/jsp/footer.jsp" %>