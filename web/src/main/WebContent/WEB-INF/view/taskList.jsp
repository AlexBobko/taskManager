<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- taskList.jsp -->
<div class="task-list">
    Всего на прием: <c:out value="${account.currentTasksFilter.totalCount}"/>
    <div class="tasks">
        <form name="user-form" action="${domen}/superior/tasks" method="post">
            <table>
                <tr>
                    <th>номер</th>
                    <th>Дата приема</th>
                    <%--<th>Время приема</th>--%>
                    <th>Дата создания</th>
                    <th>Дедлайн</th>
                    <th>Заголовок</th>
                    <th>Исполнитель</th>
                    <th>Действие</th>
                </tr>
                <c:forEach items="${account.currentTasks}" var="task">
                    <tr id="<c:out value="${task.taskId}"/>">
                        <td><c:out value="${task.taskId}"/></td>
                        <td> дата + время:</td>
                            <%--<td> дата + время: <c:out value="${account.dateFormat.format(task.dateReporting)}"/></td>--%>
                        <td>
                            <c:out value="${account.dateFormat.format(task.dateCreation)}"/>
                        </td>
                        <td>
                            <c:out value="${account.dateFormat.format(task.deadline)}"/>
                        </td>
                        <td><a href="${domen}/superior/tasks/task/${task.taskId}"><c:out value="${task.title}"/></a></td>
                        <td><c:forEach items="${task.userList}" var="user">
                            <c:out value="${user.login}"/> - <c:out value="${user.userId}"/><br/>
                        </c:forEach>
                        </td>
                        <td>
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
                                    <a href="${domen}/superior/tasks/assignment/${task.taskId}">Перенести приём</a>
                                    <%--<button name="PAY_TASK" type="submit" value="${task.taskId}">Перенести приём</button>--%>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </form>
    </div>
</div>
сделать нормальный лист с тасками на прием
<%@ include file="/WEB-INF/view/pagination.jsp" %>
<!-- end taskList.jsp -->
<%@ include file="/WEB-INF/view/footer.jsp" %>