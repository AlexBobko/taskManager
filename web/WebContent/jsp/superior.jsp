<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/header.jsp" %>
<!-- superior.jsp -->
<div class="content">
    <div class="main">
        <h1>Список текущих задач</h1>

        <form name="user-form" action="go" method="post">
            <button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
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
                    <tr id="<c:out value="${task.key}" />">
                        <td><c:out value="${task.value.id}"/></td>
                        <td><c:out value="${account.dateFormat.format(task.value.dateCreation.getTime())}"/></td>
                        <td><c:out value="${account.tasksMeta[task.key].userId}"/></td>
                        <td><c:out value="${task.value.title}"/></td>
                        <td><c:out value="${account.tasksMeta[task.key].statusId}"/></td>
                        <td><c:out value="${account.dateFormat.format(task.value.deadline.getTime())}"/></td>
                        <td>
                            <button name="TASK_DETAIL" type="submit" value="${task.key}">Детали</button>
                            <c:choose><c:when test="${account.tasksMeta[task.key].statusId == 2}">
                                <button name="PRODUCTION" type="submit" value="${task.key}">В работу</button>
                            </c:when>
                                <c:when test="${account.tasksMeta[task.key].statusId == 4}">
                                    <button name="PRODUCTION" type="submit" value="${task.key}">Вернуть на доработку
                                    </button>
                                    <button name="PAY_TASK" type="submit" value="${task.key}">Назначить приём</button>
                                </c:when>
                                <c:when test="${account.tasksMeta[task.key].statusId == 5}">
                                    <button name="PRODUCTION" type="submit" value="${task.key}">Вернуть на доработку
                                    </button>
                                    <button name="TASK_READY" type="submit" value="${task.key}">Принять</button>
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
<br/>
<br/>
<!-- end admin.jsp -->
<%@ include file="/jsp/footer.jsp" %>