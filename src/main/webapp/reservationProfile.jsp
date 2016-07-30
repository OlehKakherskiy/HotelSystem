<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 28.07.2016
  Time: 20:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="newReservation" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="user" scope="session" type="main.java.com.epam.project4.model.entity.User"/>
<c:if test="${newReservation}">
    <jsp:useBean id="requestParameters" scope="request" type="java.util.Map"/>
</c:if>
<c:if test="${!newReservation}">
    <jsp:useBean id="currentReservation" scope="session" type="main.java.com.epam.project4.model.entity.Reservation"/>
</c:if>
<html>
<head>
    <title>HotelSystem</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/> "
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="./controller?commandName=getReservationListCommand">Hotel System</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="./controller?commandName=getReservationListCommand">Home</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="./controller?commandName=logoutCommand"><span class="glyphicon glyphicon-log-out"></span>
                Logout</a></li>
        </ul>
    </div>
</nav>

<div class="page-header text-center">
    <c:choose>
        <c:when test="${newReservation == true}">
            <h1>New Reservation</h1>
        </c:when>
        <c:otherwise>
            <h1>Reservation ${currentReservation.id} details</h1>
        </c:otherwise>
    </c:choose>
</div>

<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">

        <c:choose>
            <c:when test="${newReservation == true}">
                <h4>General information</h4>
                <form class="form-horizontal" action="./controller" method="post" id="reservationForm">
                    <input type="hidden" name="commandName" value="fillNewReservationCommand">

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="startDate">Start Date:</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="startDate" pattern="yyyy-MM-dd"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="dateEnd">End Date:</label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="dateEnd" pattern="yyyy-MM-dd"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="comment">Comment:</label>
                        <div class="col-sm-10">
                            <textarea class="form-control" rows="5" id="comment"></textarea>
                        </div>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="panel panel-default">
                    <div class="panel-heading"><h4>General information</h4></div>
                    <div class="panel-body">
                        <ul class="list-group">
                            <li class="list-group-item">Date From<span
                                    class="pull-right">${currentReservation.dateFrom}</span></li>
                            <li class="list-group-item">Date To<span
                                    class="pull-right">${currentReservation.dateTo}</span></li>
                            <li class="list-group-item">Comment<span
                                    class="pull-right">${currentReservation.comment}</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="col-md-2"></div>
</div>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <c:choose>
            <c:when test="${newReservation == true}">
                <h4>Reservation Requirements</h4>
                <form class="form-horizontal">
                    <c:forEach items="${requestParameters}" var="requestParameter" varStatus="loop">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"
                                   for="p${loop.index}">${requestParameter.key.paramName}</label>
                            <div class="col-sm-10">
                                <select class="form-control" id="p${loop.index}">
                                    <c:forEach items="${requestParameter.value}" var="requestValue">
                                        <option value="${requestValue.id}">${requestValue.value.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:forEach>
                </form>
            </c:when>
            <c:otherwise>
                <div class="panel panel-default">
                    <div class="panel-heading"><h4>Reservation Requirements</h4></div>
                    <div class="panel-body">
                        <ul class="list-group">
                            <c:forEach items="${currentReservation.requestParameters}" var="parameterValue">
                                <li class="list-group-item">${parameterValue.parameter.paramName}<span
                                        class="pull-right">${parameterValue.value.value}</span></li>
                            </c:forEach>
                        </ul>

                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="col-md-2"></div>
</div>

<c:if test="${!newReservation && user.userType.id != 1 && currentReservation.status.id == 2}">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <ul class="list-group">
                <li class="list-group-item">Room Offer<span class="pull-right">
                    <a class="btn btn-default"
                       href="./controller?commandName=getHotelRoomProfileCommand&hotelRoomId=${currentReservation.hotelRoomID}">Show</a></span>
                </li>
            </ul>
        </div>
        <div class="col-md-2"></div>
    </div>
</c:if>

<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <c:choose>
            <c:when test="${newReservation}">
                <a type="submit" form="reservationForm" class="btn btn-success"
                   href="./controller?commandName=fillNewReservationCommand">Add reservation</a>
            </c:when>
            <c:when test="${!newReservation && user.userType.id == 1}">
                <c:choose>
                    <c:when test="${currentReservation.status.id == 1}">
                        <div class="btn-group pull-right" role="group">
                            <a type="button" class="btn btn-default"
                               href="./controller?commandName=getHotelRoomListCommand">Choose room</a>
                            <a type="button" class="btn btn-default"
                               href="./controller?commandName=refuseReservationProcessingCommand">Refuse processing</a>
                        </div>
                    </c:when>
                    <c:when test="${currentReservation.status.id == 3}">
                        <a type="button" class="btn btn-default pull-right"
                           href="./controller?commandName=getHotelRoomListCommand">Choose room</a>
                    </c:when>
                    <c:when test="${currentReservation.status.id == 4}">
                        <a type="button" class="btn btn-default pull-right"
                           href="./controller?commandName=refuseReservationProcessingCommand">Refuse processing</a>
                    </c:when>
                </c:choose>
            </c:when>
            <c:when test="${!newReservation && user.userType.id != 1 && currentReservation.status.id == 2}">
                <div class="btn-group pull-right" role="group">
                    <a type="button" class="btn btn-success"
                       href="./controller?commandName=submitHotelRoomOfferCommand">Accept</a>
                    <a type="button" class="btn btn-danger"
                       href="./controller?commandName=refuseHotelRoomOfferCommand">Refuse</a>
                </div>
            </c:when>
        </c:choose>
    </div>
    <div class="col-md-2"></div>
</div>
</body>
</html>