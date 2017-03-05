<!-- taskFilter.jsp-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<div class="filler">
    <p>
        Сортировать по:
        <select name="sorting_column">
            <option selected="selected" value="<c:out value="${account.currentTasksFilter.sort}"/>">Изменить критерий
            </option>
            <option value="1">Дата создания</option>
            <option value="2">Номер задачи</option>
            <option value="3">Статус</option>
            <option value="4">Логин</option>
            <option value="5">Заголовок</option>
        </select>
    </p>
    <p>
        <label><input type="checkbox" name="ask" value="ask"<c:if
                test="${account.currentTasksFilter.ask == false}"> checked</c:if>>Обратная
            сортировка</label><Br>
    </p>
    <p>
        Включить статусы:<br/>
        <c:forEach items="${statuses}" var="status">
            <label><c:out value="${status.key}"/>: <input type="checkbox" name="include_status"
                                                          value="${status.key}"
                    <c:forEach items="${account.currentTasksFilter.includeStatus}"
                               var="curretnStatus">
                        <c:if test="${status.key == curretnStatus}"> checked</c:if></c:forEach>/><c:out
                    value="${status.value}"/></label><Br>
        </c:forEach>
    </p>
    <p>
        Задач на странице:
        <select name="task_per_page">
            <option selected="selected"><c:out value="${account.currentTasksFilter.tasksPerPage}"/></option>
            <option value="3">3</option>
            <option value="5">5</option>
            <option value="7">7</option>
            <option value="10">10</option>
            <option value="12">12</option>
        </select>
    </p>

    <p>
        <button name="MAIN_FILTER" type="submit" value="MAIN_FILTER">Отфильтровать</button>
    </p>
    </table>
</div>