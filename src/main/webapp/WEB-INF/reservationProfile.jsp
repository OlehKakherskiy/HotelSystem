<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="newReservation" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="user" scope="session" type="main.java.com.epam.project4.model.entity.User"/>
<c:if test="${newReservation}">
    <jsp:useBean id="requestParameters" scope="request" type="java.util.Map"/>
</c:if>
<c:if test="${!newReservation}">
    <jsp:useBean id="currentReservation" scope="session" type="main.java.com.epam.project4.model.entity.Reservation"/>
</c:if>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="reservationProfile" var="reservBundle"/>
<fmt:setBundle basename="main" var="main"/>
<fmt:setBundle basename="valueListBundle" var="valueList"/>
<fmt:setBundle basename="parameterListResource" var="paramList"/>
<html>
<head>
    <title><fmt:message key="brand" bundle="${main}"/></title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="<c:url value="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/> "
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
</head>
<body>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="./controller?commandName=getReservationListCommand"><fmt:message key="brand"
                                                                                                           bundle="${main}"/></a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="./controller?commandName=getReservationListCommand"><fmt:message key="homeLink"
                                                                                          bundle="${main}"/></a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="./controller?commandName=logoutCommand"><span class="glyphicon glyphicon-log-out"></span>
                <fmt:message key="logoutLink" bundle="${main}"/></a></li>
        </ul>
    </div>
</nav>

<div class="page-header text-center">
    <c:choose>
        <c:when test="${newReservation == true}">
            <h1><fmt:message key="newOne" bundle="${reservBundle}"/></h1>
        </c:when>
        <c:otherwise>
            <h1><fmt:message key="reservationDetails" bundle="${reservBundle}"/> ${currentReservation.id}</h1>
        </c:otherwise>
    </c:choose>
</div>

<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <c:choose>
            <c:when test="${newReservation == true}">
                <form class="form-horizontal" action="./controller" method="post" id="reservationForm"
                      accept-charset="UTF-8">
                    <input type="hidden" name="commandName" value="fillNewReservationCommand">
                    <h4><fmt:message key="generalInfo" bundle="${reservBundle}"/></h4>
                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="startDate"><fmt:message key="startDate"
                                                                                           bundle="${reservBundle}"/></label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="startDate" name="dateFrom" pattern="yyyy-MM-dd"
                                   required/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="dateEnd"><fmt:message key="endDate"
                                                                                         bundle="${reservBundle}"/></label>
                        <div class="col-sm-10">
                            <input type="date" class="form-control" id="dateEnd" name="dateTo" pattern="yyyy-MM-dd"
                                   required/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label" for="comment"><fmt:message key="comment"
                                                                                         bundle="${reservBundle}"/></label>
                        <div class="col-sm-10">
                            <textarea class="form-control" rows="5" id="comment" name="comment"></textarea>
                        </div>
                    </div>
                    <h4><fmt:message key="requirements" bundle="${reservBundle}"/></h4>
                    <c:forEach items="${requestParameters}" var="requestParameter" varStatus="loop">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"
                                   for="${requestParameter.key.paramName}"><fmt:message
                                    key="${requestParameter.key.paramName}" bundle="${paramList}"/> </label>
                            <div class="col-sm-10">
                                <select class="form-control" id="${requestParameter.key.paramName}"
                                        name="${requestParameter.key.paramName}" required>
                                    <c:forEach items="${requestParameter.value}" var="requestValue">
                                        <option value="${requestValue.id}"><fmt:message
                                                key="${requestValue.value.value}" bundle="${valueList}"/></option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </c:forEach>
                </form>
            </c:when>
            <c:otherwise>
                <div class="panel panel-default">
                    <div class="panel-heading"><h4><fmt:message key="generalInfo" bundle="${reservBundle}"/></h4></div>
                    <div class="panel-body">
                        <ul class="list-group">
                            <li class="list-group-item"><fmt:message key="startDate"
                                                                     bundle="${reservBundle}"/><span
                                    class="pull-right">${currentReservation.dateFrom}</span></li>
                            <li class="list-group-item"><fmt:message key="endDate"
                                                                     bundle="${reservBundle}"/><span
                                    class="pull-right">${currentReservation.dateTo}</span></li>
                            <li class="list-group-item"><fmt:message key="comment"
                                                                     bundle="${reservBundle}"/><span
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
        <c:if test="${!newReservation}">
            <div class="panel panel-default">
                <div class="panel-heading"><h4><fmt:message key="requirements" bundle="${reservBundle}"/></h4></div>
                <div class="panel-body">
                    <ul class="list-group">
                        <c:forEach items="${currentReservation.requestParameters}" var="parameterValue">
                            <li class="list-group-item"><fmt:message key="${parameterValue.parameter.paramName}"
                                                                     bundle="${paramList}"/> <span
                                    class="pull-right"><fmt:message key="${parameterValue.value.value}"
                                                                    bundle="${valueList}"/> </span></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </c:if>
    </div>
    <div class="col-md-2"></div>
</div>

<c:if test="${!newReservation && user.userType.id != 1 && currentReservation.status.id == 2}">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <ul class="list-group">
                <li class="list-group-item"><fmt:message key="roomOffer" bundle="${reservBundle}"/>
                    <a class="btn btn-default pull-right"
                       href="./controller?commandName=getHotelRoomProfileCommand&hotelRoomId=${currentReservation.hotelRoomID}">
                        <fmt:message key="roomDetails" bundle="${reservBundle}"/></a>
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
                <button type="submit" form="reservationForm" class="btn btn-success pull-right">
                    <fmt:message key="submitReservation" bundle="${reservBundle}"/></button>
            </c:when>
            <c:when test="${!newReservation && user.userType.id == 1}">
                <c:choose>
                    <c:when test="${currentReservation.status.id == 1}">
                        <div class="btn-group pull-right" role="group">
                            <a type="button" class="btn btn-default"
                               href="./controller?commandName=getHotelRoomListCommand">
                                <fmt:message key="chooseRoom" bundle="${reservBundle}"/></a>
                            <a type="button" class="btn btn-default"
                               href="./controller?commandName=refuseReservationProcessingCommand">
                                <fmt:message key="refuseProcessing" bundle="${reservBundle}"/></a>
                        </div>
                    </c:when>
                    <c:when test="${currentReservation.status.id == 3}">
                        <a type="button" class="btn btn-default pull-right"
                           href="./controller?commandName=getHotelRoomListCommand"><fmt:message key="chooseRoom"
                                                                                                bundle="${reservBundle}"/></a>
                    </c:when>
                    <c:when test="${currentReservation.status.id == 4}">
                        <a type="button" class="btn btn-default pull-right"
                           href="./controller?commandName=refuseReservationProcessingCommand"><fmt:message
                                key="refuseProcessing" bundle="${reservBundle}"/></a>
                    </c:when>
                </c:choose>
            </c:when>
            <c:when test="${!newReservation && user.userType.id != 1 && currentReservation.status.id == 2}">
                <div class="btn-group pull-right" role="group">
                    <a type="button" class="btn btn-success"
                       href="./controller?commandName=submitHotelRoomOfferCommand"><fmt:message key="acceptOffer"
                                                                                                bundle="${reservBundle}"/></a>
                    <a type="button" class="btn btn-danger"
                       href="./controller?commandName=refuseHotelRoomOfferCommand"><fmt:message key="refuseOffer"
                                                                                                bundle="${reservBundle}"/></a>
                </div>
            </c:when>
        </c:choose>
    </div>
    <div class="col-md-2"></div>
</div>
</body>
</html>