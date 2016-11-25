<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/header.jsp" %>
<!-- user.jsp -->
<div class="content">
    <div class="main">
        <h1>Список текущих задач</h1>

        <form name="user-form" action="go" method="post">
            <button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
            <table class="tasks">
                <tr>
                    <th>номер</th>
                    <th>Дата создания</th>
                    <th>Заголовок</th>
                    <th>Статус</th>
                    <th>Дедлайн</th>
                    <th>Действие</th>
                </tr>
                <c:forEach items="${account.currentTasks}" var="task">
                    <tr id="<c:out value="${task.key}" />">
                        <td><c:out value="${task.value.id}"/></td>
                        <td><c:out value="${account.dateFormat.format(task.value.dateCreation.getTime())}"/></td>
                        <td><c:out value="${task.value.title}"/></td>
                        <td><c:out value="${account.tasksMeta[task.key].statusId}"/></td>
                        <td><c:out value="${account.dateFormat.format(task.value.deadline.getTime())}"/></td>
                        <td>
                            <button name="TASK_DETAIL" type="submit" value="${task.key}">Детали</button>
                            <c:choose><c:when test="${task.value.statusId == 1}">
                                <button name="APPROVE_TASK" type="submit" value="${task.key}">На утверждение
                                </button>
                            </c:when>
                                <c:when test="${task.value.statusId == 3}">
                                    <button name="FOR_CHECKING" type="submit" value="${task.key}">На проверку</button>
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
<!-- end main.jsp -->
<%@ include file="/jsp/footer.jsp" %>