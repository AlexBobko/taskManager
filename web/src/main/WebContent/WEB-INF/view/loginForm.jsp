<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--  login-form  -->
<form name="login-form" class="login-form" action="login" method="post">
	<div class="header">
		<h1>Авторизация</h1>
		<span>Введите ваши регистрационные данные для входа</span>
	</div>
	<div class="content">
		<input name="username" type="text" class="input username" value="Логин" /> <input name="password" type="password" class="input password"
			value="Пароль" />
	</div>
	<div class="footer">
		<input type="submit" name="LOGIN" value="LOGIN" class="button" />
		<!-- <input type="submit" name="submit" value="ДОБАВИТЬ" class="register" />	 -->
	</div>
</form>