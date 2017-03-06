<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ include file="/WEB-INF/view/header.jsp" %>
<!-- assignment.jsp -->
<div class="content">
    <p>

    <form name="new-task" action="${domen}/superior/tasks/newtask" method="get">
        <button name="GO_ADD" type="submit" value="GO_ADD">Новая задача</button>
    </form>
    <form name="task-list" action="${domen}/superior/tasks" method="get">
        <button name="GO_TASK_LIST" type="submit" value="GO_TASK_LIST">К списку задач</button>
    </form>
    </p>

    <div class="main">
        <h1>Назначение даты приема</h1>

        <p>
            Задача: <a href="${domen}/superior/tasks/task/${curTask.taskId}"><c:out value="${curTask.title}"/> (<c:out
                value="${curTask.taskId}"/>)</a>
            <br/>Исполнители: <c:forEach items="${curTask.userList}" var="user">
            <c:out value="${user.login}"/> - <c:out value="${user.userId}"/><br/>
        </c:forEach>
            Дата создания: <c:out value="${account.dateFormat.format(curTask.dateCreation)}"/>
            <br/> Дедлайн: <c:out value="${account.dateFormat.format(curTask.deadline)}"/>
            <br/> Статус:
            <%--<c:out value="${status.(curTask.statusId)}"/>--%>
            <c:out value="${statuses[(curTask.statusId).intValue()]}"/>
            <%--<c:out value="${status.key}"/>--%>
            <%--<fmt:bundle basename="i18n.status">--%>
            <%--<fmt:message key="1"/><br/>--%>
            <%--<fmt:message key="2"/><br/>--%>
            <%--</fmt:bundle>--%>
        </p>
        <p>
        <form name="assignment-form" action="" method="post">
            Выберите дату и время приема <input type="text" name="taskAssignmentData" class="tcal" value=""
                                                placeholder="Выберите дату"/> чч:<select name="hour">
            <c:forEach items="${hourList}" var="hour">
                <option value="<c:out value="${hour}"/>"><c:out value="${hour}"/></option>
            </c:forEach></select> мин.<select name="minute"><c:forEach items="${minuteList}" var="minute">
            <option value="<c:out value="${minute}"/>"><c:out value="${minute}"/></option>
        </c:forEach></select>
            <button name="PAY_TASK" type="submit" value="${task.taskId}">Назначить</button>
        </form>
        </p>
        <%@ include file="/WEB-INF/view/taskList.jsp" %>
    </div>

</div>
<br/>
<br/>
<!-- end assignment.jsp -->
<%@ include file="/WEB-INF/view/footer.jsp" %>