<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/header.jsp" %>
<!-- superior.jsp -->
<div class="content">
    <form name="user-form" action="go" method="post">
        <div class="main">
            <h1>Список текущих задач</h1>

            <button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
            Результатов: <c:out value="${account.currentTasksFilter.totalCount}"/>
            <%@ include file="/jsp/taskFilter.jsp" %>
            <table class="tasks">
                <tr>
                    <th>номер</th>
                    <th>Дата создания</th>
                    <th>Исполнитель</th>
                    <th>Заголовок</th>
                    <th>Статус</th>
                    <th>Дедлайн</th>
                    <th>Действие</th>
                </tr>
                <c:forEach items="${account.currentTasks}" var="task">
                    <tr id="<c:out value="${task.taskId}"/>">
                        <td><c:out value="${task.taskId}"/></td>
                        <td>
                            <c:out value="${account.dateFormat.format(task.dateCreation)}"/>
                        </td>
                        <td><c:forEach items="${task.userList}" var="user">
                            <c:out value="${user.login}"/> - <c:out value="${user.userId}"/><br/>
                        </c:forEach>
                        </td>
                        <td><c:out value="${task.title}"/></td>
                        <td><c:out value="${task.statusId}"/></td>
                        <td>
                            <c:out value="${account.dateFormat.format(task.deadline)}"/>
                        </td>
                        <td>
                            <button name="TASK_DETAIL" type="submit" value="${task.taskId}">Детали</button>
                            <c:choose><c:when test="${task.statusId == 2}">
                                <button name="PRODUCTION" type="submit" value="${task.taskId}">В работу</button>
                            </c:when>
                                <c:when test="${task.statusId == 4}">
                                    <button name="PRODUCTION" type="submit" value="${task.taskId}">Вернуть на доработку
                                    </button>
                                    <button name="PAY_TASK" type="submit" value="${task.taskId}">Назначить приём
                                    </button>
                                </c:when>
                                <c:when test="${task.statusId == 5}">
                                    <button name="PRODUCTION" type="submit" value="${task.taskId}">Вернуть на доработку
                                    </button>
                                    <button name="TASK_READY" type="submit" value="${task.taskId}">Принять</button>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <%@ include file="/jsp/pagination.jsp" %>
    </form>
</div>
<br/>
<br/>
<!-- end admin.jsp -->
<%@ include file="/jsp/footer.jsp" %>