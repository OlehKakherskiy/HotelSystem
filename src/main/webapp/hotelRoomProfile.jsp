<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="hotelRoom" type="main.java.com.epam.project4.model.entity.HotelRoom" scope="request"/>
<jsp:useBean id="user" type="main.java.com.epam.project4.model.entity.User" scope="session"/>
<c:set var="isAdmin" value="${user.userType.id == 1}" scope="page"/>
<c:if test="${isAdmin == true}">
    <jsp:useBean id="reservationMonth" type="java.time.Month" scope="request"/>
    <jsp:useBean id="reservationYear" type="java.time.Year" scope="request"/>

    <%-- building previous month reservations link--%>
    <c:choose>
        <c:when test="${reservationMonth.value == 1}">
            <c:set var="prevMonth" value="12" scope="page"/>
            <c:set var="prevYear" value="${reservationYear.value-1}" scope="page"/>
        </c:when>
        <c:otherwise>
            <c:set var="prevMonth" value="${reservationMonth.value-1}" scope="page"/>
            <c:set var="prevYear" value="${reservationYear.value}" scope="page"/>
        </c:otherwise>
    </c:choose>
    <c:url value="./controller" var="prevMonthUrl">
        <c:param name="commandName" value="getHotelRoomProfileCommand"/>
        <c:param name="reservationMonth" value="${prevMonth}"/>
        <c:param name="reservationYear" value="${prevYear}"/>
        <c:param name="hotelRoomId" value="${hotelRoom.roomID}"/>
    </c:url>

    <%-- building next month reservations link --%>
    <c:choose>
        <c:when test="${reservationMonth.value == 12}">
            <c:set var="nextMonth" value="1" scope="page"/>
            <c:set var="nextYear" value="${reservationYear.value+1}" scope="page"/>
        </c:when>
        <c:otherwise>
            <c:set var="nextMonth" value="${reservationMonth.value+1}" scope="page"/>
            <c:set var="nextYear" value="${reservationYear.value}" scope="page"/>
        </c:otherwise>
    </c:choose>
    <c:url value="./controller" var="nextMonthUrl">
        <c:param name="commandName" value="getHotelRoomProfileCommand"/>
        <c:param name="reservationMonth" value="${nextMonth}"/>
        <c:param name="reservationYear" value="${nextYear}"/>
        <c:param name="hotelRoomId" value="${hotelRoom.roomID}"/>
    </c:url>
</c:if>

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

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="./controller?commandName=getReservationListCommand">Hotel System</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="./controller?commandName=getReservationListCommand">Home</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="./controller?commandName=logoutCommand"><span class="glyphicon glyphicon-log-out"></span>
                Logout</a></li>
        </ul>
    </div>
</nav>

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

<c:if test="${isAdmin == true}">
    <div class="text-center">Reservation details for ${reservationMonth} ${reservationYear}</div>
    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <div class="panel-group">
                <c:forEach var="reservation" items="${hotelRoom.reservationList}">
                    <div class="panel panel-default">
                        <div class="panel-heading">Reservation ${reservation.id}
                            <label class="pull-right">${reservation.requestDate}</label>
                        </div>
                        <div class="panel-body">
                            <ul class="list-group">
                                <li class="list-group-item">Start Date<span
                                        class="pull-right">${reservation.dateFrom}</span></li>
                                <li class="list-group-item">End Date<span
                                        class="pull-right">${reservation.dateTo}</span>
                                </li>
                                <li class="list-group-item">Status<span class="pull-right">${reservation.status}</span>
                                </li>
                            </ul>
                                <%--<a type="button" class="btn btn-default pull-right"--%>
                                <%--href="./controller?commandName=getReservationProfileCommand&currentReservation=${reservation.id}">Details</a>--%>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div class="col-md-1"></div>
    </div>

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <a type="button" class="btn btn-default pull-left" href="${prevMonthUrl}">Prev</a>
            <a type="button" class="btn btn-default pull-right" href="${nextMonthUrl}">Next</a>
        </div>
        <div class="col-md-1"></div>
    </div>
</c:if>

<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <c:choose>
            <c:when test="${isAdmin == true}">
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