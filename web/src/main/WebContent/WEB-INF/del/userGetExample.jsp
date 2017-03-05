<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/view/header.jsp" %>
<!-- user.jsp -->
<div class="content">
    <div class="main">
        <h1>Список текущих задач</h1>
        /user/tasks/newtask
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
                        <td><c:out value="${task.value.taskId}"/></td>
                        <td>
                            <c:out value="${account.dateFormat.format(task.value.dateCreation)}"/>
                        </td>
                        <td><c:out value="${task.value.title}"/></td>
                        <td><c:out value="${task.value.statusId}"/></td>
                        <td>
                            <c:out value="${account.dateFormat.format(task.value.deadline)}"/>
                        </td>
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
    <div class="pagination">


        <%--For displaying Page numbers.
        The when condition does not display a link for the current page--%>
        <table border="1" cellpadding="5" cellspacing="5">
            <tr><%--For displaying Previous link except for the 1st page --%>
                <c:if test="${account.currentTasksFilter.page != 1}">
                    <%--<c:if test="${account.currentTasksFilter.countPage != 1}">--%>
                    <%--<c:if test="${currentPage != 1}">--%>
                    <td><a href="employee.do?page=${account.currentTasksFilter.page - 1}">Previous</a></td>
                </c:if>
                <%--<c:forEach begin="1" end="${noOfPages}" var="i">--%>
                <c:forEach begin="1" end="${account.currentTasksFilter.countPage}" var="i">
                    <c:choose>
                        <c:when test="${account.currentTasksFilter.page eq i}">
                            <td>${i}</td>
                        </c:when>
                        <c:otherwise>
                            <td><a href="employee.do?page=${i}">${i}</a></td>
                        </c:otherwise>
                    </c:choose>
                </c:forEach> <%--For displaying Next link --%>
                <c:if test="${account.currentTasksFilter.page lt account.currentTasksFilter.countPage}">
                    <td><a href="employee.do?page=${account.currentTasksFilter.page + 1}">Next</a></td>
                </c:if>
            </tr>
        </table>
    </div>
</div>
<br/>
<br/>
<!-- end main.jsp -->
<%@ include file="/WEB-INF/view/footer.jsp" %>