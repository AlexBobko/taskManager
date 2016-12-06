<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pagination">
    <%--For displaying Page numbers.
    The when condition does not display a link for the current page--%>
    <table border="1" cellpadding="5" cellspacing="5">
        <tr><%--For displaying Previous link except for the 1st page --%>
            <c:if test="${account.currentTasksFilter.page != 1}">
                <%--<c:if test="${account.currentTasksFilter.countPage != 1}">--%>
                <%--<c:if test="${currentPage != 1}">--%>
                <td>
                    <button name="PAGE" type="submit" value="${account.currentTasksFilter.page - 1}">Previous
                    </button>
                </td>
            </c:if>
            <%--<c:forEach begin="1" end="${noOfPages}" var="i">--%>
            <c:forEach begin="1" end="${account.currentTasksFilter.countPage}" var="i">
                <c:choose>
                    <c:when test="${account.currentTasksFilter.page eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td>
                            <button name="PAGE" type="submit" value="${i}">${i}</button>
                        </td>
                    </c:otherwise>
                </c:choose>
            </c:forEach> <%--For displaying Next link --%>
            <c:if test="${account.currentTasksFilter.page lt account.currentTasksFilter.countPage}">
                <td>
                    <button name="PAGE" type="submit" value="${account.currentTasksFilter.page + 1}">Next
                    </button>
                </td>
            </c:if>
        </tr>
    </table>
</div>