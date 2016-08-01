<%@ page contentType="text/html; charser=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML>
<html>
<head>
    <link rel="stylesheet"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/> "
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="jumbotron">

        <h1 class="display-3">System exception</h1>
        <p class="lead"><c:out value="${pageContext.exception.message}"/></p>
        <hr class="m-y-2">
        <p>Possibly you want to back to the main page...</p>
        <p class="lead">
            <c:choose>
                <c:when test="${sessionScope['user'] == null}">
                    <a class="btn btn-primary btn-lg" href="${pageContext.servletContext.contextPath}"
                       role="button">Back</a>
                </c:when>
                <c:otherwise>
                    <a class="btn btn-primary btn-lg"
                       href="${pageContext.servletContext.contextPath.concat('/controller?commandName=getReservationListCommand')}"
                       role="button">Main</a>
                </c:otherwise>
            </c:choose>
        </p>
    </div>
</div>
</body>
</html>