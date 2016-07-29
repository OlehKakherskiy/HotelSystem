<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="hotelRoom" type="main.java.com.epam.project4.model.entity.HotelRoom" scope="request"/>
<jsp:useBean id="user" type="main.java.com.epam.project4.model.entity.User" scope="session"/>
<html>
<head>
    <title>HotelSystem</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/> "
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="<c:url value="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"/>"></script>
    <script src="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"/>"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
</head>
<body>
<div class="page-header text-center"><h1>Room № ${hotelRoom.roomName} profile</h1></div>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <div class="panel-group">
            <div class="panel panel-default">
                <div class="panel-body">
                    <ul class="list-group">
                        <c:forEach items="${hotelRoom.parameters}" var="parameterValue">
                            <li class="list-group-item">${parameterValue.parameter.paramName}<span
                                    class="pull-right">${parameterValue.value.value}</span></li>
                        </c:forEach>
                    </ul>
                    <label class="pull-left">isActive=${hotelRoom.activationStatus}</label>
                    <label class="pull-right">price =${hotelRoom.price}</label>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-1"></div>
</div>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <c:choose>
            <c:when test="${user.userType.id == 1}">
                <c:set var="backLink" scope="page" value="./controller?commandName=getHotelRoomListCommand"/>
                <c:set var="offerLink" scope="page" value="./controller?commandName=offerHotelRoomCommand"/>
                <a type="button" class="btn btn-default pull-right" href="${offerLink}">Offer</a>
            </c:when>
            <c:otherwise>
                <c:set var="backLink" scope="page" value="./controller?commandName=getReservationProfileCommand"/>
            </c:otherwise>
        </c:choose>
        <a type="button" class="btn btn-default pull-right" href="${backLink}">Back</a>
    </div>
    <div class="col-md-1"></div>
</div>
</body>
</html>