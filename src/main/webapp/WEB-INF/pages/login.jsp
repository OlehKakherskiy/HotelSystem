<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>HotelSystem</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/> "
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value="main/WEB-INF/css/login.css"/>">
</head>
<body>
<div class="card card-container text-center">
    <h3 class="card-header">Вход в систему</h3>
    <div class="card-block">
        <form action="../../../../web" name="sign" class="form-signin" method="post" novalidate>
            <input type="hidden" name="command" value="loginCommand"/>
            <div class="form-group">
                <input type="text" class="form-control" id="usr" name="login" placeholder="Name">
            </div>
            <div class="form-group">
                <input type="password" class="form-control" id="password" name="password" placeholder="Password">
            </div>
            <button type="submit" class="btn btn-default btn-block">Login</button>
        </form>
    </div>
</div>
</body>
</html>