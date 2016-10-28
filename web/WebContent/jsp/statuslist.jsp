<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="status-list">
    <h3>Help:</h3>
    <h4>Список статусов:</h4>
    1(1) - Создано (новый) - еще можно редактировать. <br/>
    2(1) - На утверждении - ждет утвеждения начальника, для начала работы<br/>
    3(2) - В работе, подчиненный выполняет задание<br/>
    4(1) - На проверкe - задание передано на проверку руководителю для назначения времени приема<br/>
    5(2) - К сдаче - назначено время приема<br/>
    6(2) - Выполнено - задача закрывается и уходит в архив<br/>
    Задумки: <br/>
    - Запросить отчет - подбодрить работника, чтобы почесался, если долго не делает<br/>
    </br>
    ps: В скобках, привелегии для выполнения.
    <h3>Роли пользователей:</h3>
    <p>
        1 - исполнитель (user)</br>
        2 - руководитель (superior)</br>
    </p>

    </br>
    </br>Используемые кнопки:
    <button name="VIEW_HISTORY" type="submit" value="${task.key}">История</button>
    <button name="APPROVE_TASK" type="submit" value="${task.key}">На утверждение</button>
    <button name="PRODUCTION" type="submit" value="${task.key}">В работу</button>
    <button name="FOR_CHECKING" type="submit" value="${task.key}">На проверку</button>
    <button name="PAY_TASK" type="submit" value="${task.key}">Назначить приём</button>
    <button name="TASK_READY" type="submit" value="${task.key}">Принять</button>
    <button name="SEND_APPROVE" type="submit" value="${task.key}">Отправить на утверждение</button>
</div>