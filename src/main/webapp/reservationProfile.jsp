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
    <link rel="stylesheet" href="<c:url value="/main/webapp/WEB-INF/css/login.css"/>">
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

                        <input type="date" class="form-control" id="requestDate"
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
                    <input type="date" class="form-control" id="startDate"
                    <c:if test="${!newReservation}">
                           placeholder="${dateFrom}" readonly
                    </c:if>>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="dateEnd">End Date:</label>
                <div class="col-sm-10">
                    <input type="date" class="form-control" id="dateEnd"
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
            <div class="form-group">
                <label class="col-sm-2 control-label" for="sel1">Select list:</label>
                <div class="col-sm-10">
                    <select class="form-control" id="sel1">
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                        <option>4</option>
                    </select>
                </div>
            </div>
        </form>
    </div>
    <div class="col-md-2"></div>
</div>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <c:choose>
            <c:when test="${newReservation}">
                <button type="submit" form="reservationForm" class="btn btn-success">Add reservation</button>
            </c:when>
            <c:when test="${!newReservation && user.userType.id == 1}">
                <a type="button" class="btn btn-default" href="./controller?commandName=getHotelRoomListCommand">Choose
                    room</a>
            </c:when>
            <c:otherwise>
                <div class="btn-group pull-right" role="group">
                    <a type="button" class="btn btn-success"
                       href="./controller?commandName=submitHotelRoomOfferCommand">Accept</a>
                    <a type="button" class="btn btn-danger" href="./controller?commandName=refuseHotelRoomOfferCommand">Refuse</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="col-md-2"></div>
</div>
</body>
</html>
