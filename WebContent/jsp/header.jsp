<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TaskManager | <c:out value="${title}" /></title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<!-- Kalendar <link href="css/style.css" rel="stylesheet" type="text/css" /> 
-->
<link href="js/simple-calendar/tcal.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/simple-calendar/tcal.js"></script>
<!--  
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
 -->
</head>
<body>
<div class="user-panel">
	<c:choose>
		<c:when test="${not empty account.user.id}">
			<%@ include file="/jsp/userPanel.jsp"%>
		</c:when>
		<c:otherwise>
			<div class="current-user">Требуется аунтификация</div>
		</c:otherwise>
	</c:choose>
</div>
<!-- end header.jsp -->