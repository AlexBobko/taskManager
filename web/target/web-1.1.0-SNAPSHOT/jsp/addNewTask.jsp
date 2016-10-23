<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--  addNewTask.jspx -->
<h2>Создать новую задачу</h2>
<form name="newNask" action="" method="post">
	<div>
		<h3>Название</h3>
		<p>
			<textarea name="titleTask" cols="80" rows="1" placeholder="Название задачи"></textarea>
		</p>
		<h3>Описание</h3>
		<p>
			<textarea name="bodyTask" cols="80" rows="30" placeholder="Детальное описание задачи"></textarea>
		</p>
		<p>
			<b>Крайний срок сдачи (дедлайн): </b><input type="text" name="taskDeadline" class="tcal" value="" placeholder="Выберите дату" />
			<button name="TASK_ADD" type="submit" value="TASK_ADD">Добавить задачу</button>
		</p>
	</div>
</form>
<!-- addNewTask.jspx  -->