<%@ page contentType="text/html; charser=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>HotelSystem</title>
    <link rel="stylesheet"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/> "
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<div class="row">
    <div class="col-md-4"></div>
    <div class="col-md-4">
        <div class="card card-container text-center">
            <h3 class="card-header">Recovery password</h3>
            <div class="card-block">
                <form action="./controller" name="sign" method="post" class="form-signin">
                    <input type="hidden" name="commandName" value="passwordRecoveryCommand"/>
                    <div class="form-group">
                        <input type="text" class="form-control" id="usr" name="login"
                               placeholder="Login" required>
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" id="password" name="newPassword"
                               placeholder="Password" required>
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" id="passwordConfirmation"
                               name="passwordConfirmation"
                               placeholder="Confirm password" required>
                    </div>
                    <button type="submit" class="btn btn-default btn-block" name="submitBtn">Recovery password</button>
                </form>
            </div>
        </div>
    </div>
    <div class="col-md-4"></div>
</div>
</body>
</html>
