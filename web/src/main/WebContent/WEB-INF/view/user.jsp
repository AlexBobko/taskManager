<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/view/header.jsp" %>
<!-- user.jsp -->
<div class="content">
    <a href="${domen}/user/tasks/newtask">Новая задача</a>

    <form name="task-list" action="${domen}/user/tasks" method="post">
        <div class="main">
            <h1>Список текущих задач</h1>
            Результатов: <c:out value="${account.currentTasksFilter.totalCount}"/>
            <%@ include file="/WEB-INF/view/taskFilter.jsp" %>
            <div class="tasks">
                <table>
                    <tr>
                        <th>номер</th>
                        <th>Дата создания</th>
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
                            <td><a href="${domen}/user/tasks/task/${task.taskId}"><c:out value="${task.title}"/></a></td>
                            <td><c:out value="${task.statusId}"/></td>
                            <td>
                                <c:out value="${account.dateFormat.format(task.deadline)}"/>
                            </td>
                            <td>
                                <c:choose><c:when test="${task.statusId == 1}">
                                    <button name="APPROVE_TASK" type="submit" value="${task.taskId}">На утверждение
                                    </button>
                                </c:when>
                                    <c:when test="${task.statusId == 3}">
                                        <button name="FOR_CHECKING" type="submit" value="${task.taskId}">На проверку
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
        <%@ include file="/WEB-INF/view/pagination.jsp" %>
    </form>
    <form name="new-task" action="${domen}/user/tasks/newtask" method="get">
        <button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
    </form>
</div>
<br/>
<br/>
<!-- end user.jsp -->
<%@ include file="/WEB-INF/view/footer.jsp" %>