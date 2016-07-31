<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>HotelSystem</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/> "
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<div class="row">
    <div class="col-md-4"></div>
    <div class="col-md-4">
        <div class="card card-container text-center">
            <h3 class="card-header">Вход в систему</h3>
            <div class="card-block">
                <c:catch var="error">
                    <form action="./controller" name="sign" method="post" class="form-signin">
                        <input type="hidden" name="commandName" value="loginCommand"/>
                        <div class="form-group">
                            <input type="text" class="form-control" id="usr" name="login" placeholder="Name" required>
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control" id="password" name="password"
                                   placeholder="Password" required>
                        </div>
                        <c:if test="${param.submitBtn && param.login == null && param.login.length() == 0}">
                            <p>exception</p>
                        </c:if>
                        <button type="submit" class="btn btn-default btn-block" name="submitBtn">Login</button>
                    </form>
                </c:catch>
                <c:if test="${error != null}">
                    <p>There has been an exception raised in the above
                        arithmetic operation. Please fix the error.
                        Exception is: ${error}</p>
                </c:if>
            </div>
        </div>
    </div>
    <div class="col-md-4"></div>
</div>
</body>
</html>