<%@ page contentType="text/html; charser=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${param['lang']}" scope="page"/>
<fmt:setBundle basename="login" var="login"/>
<html>
<head>
    <title>HotelSystem</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/> "
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>
<body>
<div class="row">
    <div class="col-md-4"></div>
    <div class="col-md-4">
        <div class="card card-container text-center">

            <h3 class="card-header"><fmt:message key="header" bundle="${login}"/></h3>
            <div class="card-block">
                <form action="./controller" name="sign" method="post" class="form-signin">
                    <input type="hidden" name="commandName" value="loginCommand"/>
                    <div class="form-group">
                        <input type="text" class="form-control" id="usr" name="login"
                               placeholder="<fmt:message key="login" bundle="${login}"/>" required>
                    </div>
                    <div class="form-group">
                        <input type="password" class="form-control" id="password" name="password"
                               placeholder="<fmt:message key="password" bundle="${login}"/>" required>
                    </div>

                    <button type="submit" class="btn btn-default btn-block" name="submitBtn"><fmt:message
                            key="loginBtn" bundle="${login}"/></button>
                    <div class="form-group"></div>
                    <div class="form-group">
                        <a href="./controller?commandName=getRegistrationFormCommand&language=ru_RU" type="button"
                           id="regLink">Register</a>
                    </div>
                    <div class="form-group">
                        <a href="./controller?commandName=getPasswordRecoveryFormCommand&language=ru_RU" type="button"
                           id="recoveryPasswordLink">Forgot password?</a>
                    </div>
                    <div class="form-group">
                        <label for="language"><fmt:message key="interfaceLanguage" bundle="${login}"/></label>
                        <select name="language" id="language">
                            <option value="en_EN">English</option>
                            <option value="ru_RU" selected>Русский</option>
                        </select>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="col-md-4"></div>
</div>
<script>
    $(document).ready(function () {
        $("#language").change(function () {
            lang = $("#language option:selected").val();
            $("#regLink").attr("href", $("#regLink").attr("href").substring(0, $("#regLink").attr("href").lastIndexOf("=") + 1) + lang);
            $("#recoveryPasswordLink").attr("href", $("#recoveryPasswordLink").attr("href").substring(0, $("#recoveryPasswordLink").attr("href").lastIndexOf("=") + 1) + lang);
        })
    })
</script>
</body>
</html>