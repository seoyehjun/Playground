<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="cpath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>

<body>
<h1>회원가입 페이지</h1>

<hr>

<form action="/joinProc" method="post">
    <input type="text" name="username" placeholder="Username"/><br>
    <input type="password" name="userpw" placeholder="Password"/><br>
    <input type="email" name="useremail" placeholder="Email"/><br>
    <button>회원가입</button>
</form>
</body>
</html>