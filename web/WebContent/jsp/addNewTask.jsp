<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/jsp/header.jsp" %>
<!-- addNewTask.jsp -->
<div class="new-task">
    <h2>Создать новую задачу</h2>

    <form name="newTask" action="" method="post">
        <div>
            <button name="GO_TASK_LIST" type="submit" value="GO_TASK_LIST">Вернуться списку задач</button>
            <h3>Название</h3>

            <p>
                <textarea name="titleTask" cols="70" rows="1" placeholder="Заголовок"></textarea>
            </p>

            <p>
                <b>Срок сдачи (дедлайн):</b> <input type="text" name="taskDeadline" class="tcal" value=""
                                                    placeholder="Выберите дату"/>
            </p>

            <p><b>Исполнитель(не реализовано, закинуть в сессию исписок исполнителей): </b>
                <select name="employee">
                    <option selected="selected">Выберите героя</option>
                    <option value="1">Чебурашка</option>
                    <option value="3">Крокодил Гена</option>
                    <option value="1">Шапокляк</option>
                    <option value="3">Крыса Лариса</option>
                </select>
            </p>
            <h3>Описание</h3>

            <p>
                <textarea name="bodyTask" cols="80" rows="30" placeholder="Детальное описание задачи"></textarea>
            </p>

            <p>
                <button name="TASK_ADD" type="submit" value="TASK_ADD">Добавить задачу</button>
            </p>
        </div>
    </form>
</div>
<!-- addNewTask.jsp -->
<%@ include file="/jsp/footer.jsp" %>