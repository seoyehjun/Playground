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
    <h1>로그인 페이지</h1>

    <hr>

    <form action="/loginProc" method="post">
        <input type="text" name="username" placeholder="Username"/><br>
        <input type="password" name="userpw" placeholder="Password"/><br>

        <input type="submit" value="login">
    </form>
    <a href="/oauth2/authorization/google">구글 로그인</a>
    <a href="${cpath}/email_verification">회원가입을 아직 하지 않으셨나요?</a>

</body>

</html>