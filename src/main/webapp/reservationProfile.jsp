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

    <fmt:parseDate value="${currentReservation.requestDate}" var="rDate" pattern="yyyy-MM-dd"/>
    <fmt:formatDate value="${rDate}" pattern="dd.mm.yyyy" var="reqDate"/>

    <fmt:parseDate value="${currentReservation.dateFrom}" var="fDate" pattern="yyyy-MM-dd"/>
    <fmt:formatDate value="${fDate}" pattern="dd.mm.yyyy" var="dateFrom"/>

    <fmt:parseDate value="${currentReservation.dateFrom}" var="dateTo" pattern="yyyy-MM-dd"/>
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
        <h4>General information</h4>
        <form class="form-horizontal" action="./controller" method="post" id="reservationForm">
            <c:if test="${!newReservation}">
                <input type="hidden" name="commandName" value="fillNewReservationCommand">
            </c:if>

            <c:if test="${!newReservation}">
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="requestDate">Request Date:</label>
                    <div class="col-sm-10">

                        <input type="date" class="form-control" id="requestDate" pattern="yyyy-MM-dd"
                               value="<c:out value="${reqDate}"/>">
                        <div>
                            <c:out value="${reqDate}"/>
                        </div>

                        <c:out value="${rDate}"/>

                        <div>
                            <c:out value="${currentReservation.requestDate}"/>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="startDate">Start Date:</label>
                <div class="col-sm-10">
                    <input type="date" class="form-control" id="startDate" pattern="yyyy-MM-dd"
                    <c:if test="${!newReservation}">
                           placeholder="${dateFrom}" readonly
                    </c:if>>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="dateEnd">End Date:</label>
                <div class="col-sm-10">
                    <input type="date" class="form-control" id="dateEnd" pattern="yyyy-MM-dd"
                    <c:if test="${!newReservation}">
                           placeholder="${dateTo}"
                           readonly
                    </c:if>>
                </div>
            </div>

            <c:if test="${!newReservation}">
                <div class="form-group">
                    <label class="col-sm-2 control-label" for="status">Status:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="status" placeholder="${currentReservation.status}"
                               readonly>
                    </div>
                </div>
            </c:if>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="comment">Comment:</label>
                <div class="col-sm-10">
                    <textarea class="form-control" rows="5" id="comment"
                              <c:if test="${!newReservation}">readonly</c:if>>${currentReservation.comment}
                    </textarea>
                </div>
            </div>
        </form>
    </div>
    <div class="col-md-2"></div>
</div>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <h4>Reservation Requirements</h4>
        <form class="form-horizontal">
            <c:choose>
                <c:when test="${newReservation == true}">
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
                </c:when>
                <c:otherwise>
                    <c:forEach items="${currentReservation.requestParameters}" var="parameterValue">
                        <div class="form-group">
                            <label class="col-sm-2 control-label"
                                   for="p${parameterValue.id}">${parameterValue.parameter.paramName}</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="p${parameterValue.id}"
                                       readonly placeholder="${parameterValue.value.value}"
                                />
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </form>
    </div>
    <div class="col-md-2"></div>
</div>

<c:if test="${!newReservation && currentReservation.status.id != 2}">
    <div class="row">
        <div class="col-md-2"></div>
        <div class="col-md-8">
            <ul class="list-group">
                <li class="list-group-item">Room Offer<span class="pull-right">
                    <a class="btn btn-default" href="./controller?commandName=getHotelRoomProfileCommand&hotelRoomId=${currentReservation.hotelRoomID}">Show</a></span>
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
                <a type="button" class="btn btn-default" href="./controller?commandName=getHotelRoomListCommand">Choose
                    room</a>
            </c:when>
            <c:when test="${!newReservation && currentReservation.status.id == 2}">
                <div class="btn-group pull-right" role="group">
                    <a type="button" class="btn btn-success"
                       href="./controller?commandName=submitHotelRoomOfferCommand">Accept</a>
                    <a type="button" class="btn btn-danger" href="./controller?commandName=refuseHotelRoomOfferCommand">Refuse</a>
                </div>
            </c:when>
        </c:choose>
    </div>
    <div class="col-md-2"></div>
</div>
</body>
</html>