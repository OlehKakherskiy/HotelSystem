<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="user" type="main.java.com.epam.project4.model.entity.User" scope="session"/>
<jsp:useBean id="reservationList" type="java.util.List" scope="request"/>
<jsp:useBean id="lang" class="java.lang.String" scope="session"/>
<jsp:useBean id="reservationStatusList" type="java.util.List" scope="application"/>

<fmt:setLocale value="${sessionScope['lang']}" scope="session"/>
<fmt:setBundle basename="index" var="index"/>
<fmt:setBundle basename="main" var="main"/>
<html>
<head>
    <title><fmt:message key="brand" bundle="${main}"/></title>
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
<div class="row">
    <div class="col-md-4"></div>
    <div class="col-md-4" id="userInfo">
        <div id="personalInfo">
            <h4><fmt:message key="personalInfoHeader" bundle="${index}"/></h4>
            <form class="form-horizontal">
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label"><fmt:message key="name" bundle="${index}"/></label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="name" placeholder="${user.name}" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label for="surname" class="col-sm-2 control-label"><fmt:message key="surname"
                                                                                     bundle="${index}"/></label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="surname" placeholder="${user.lastName}" disabled>
                    </div>
                </div>
            </form>
        </div>
        <div id="mobilePhonesInfo">
            <h4><fmt:message key="mobilePhonesHeader" bundle="${index}"/></h4>
            <form class="form-horizontal">
                <c:forEach items="${user.mobilePhoneList}" var="mobilePhone">
                    <div class="form-group">
                        <label for="mobilePhone" class="col-sm-2 control-label"><fmt:message key="mobilePhone"
                                                                                             bundle="${index}"/></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="mobilePhone"
                                   placeholder="${mobilePhone.mobilePhone}"
                                   disabled>
                        </div>
                    </div>
                </c:forEach>
            </form>
        </div>

    </div>
    <div class="col-md-4"></div>
</div>

<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <div class="row">
            <div class="pull-left"><h4><fmt:message key="reservationsHeader" bundle="${index}"/></h4></div>
            <c:if test="${user.userType.id != 1}">
                <a type="button" class="btn btn-primary pull-right"
                   href="./controller?commandName=prepareReservationPageCommand"><fmt:message key="newReservation"
                                                                                              bundle="${index}"/></a>
            </c:if>
            <div class="dropdown pull-right">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><fmt:message
                        key="reservationStatus" bundle="${index}"/>
                    <span class="caret"></span></button>
                <fmt:setBundle basename="reservationTypeBundle" var="reservationType"/>
                <ul class="dropdown-menu">
                    <c:forEach items="${reservationStatusList}" var="status">
                        <li>
                            <a href="./controller?commandName=getReservationListCommand&reservationStatus=${status.id}"><fmt:message
                                    key="${status.name}" bundle="${reservationType}"/></a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

        <div class="panel-group">
            <c:forEach var="reservation" items="${reservationList}">
                <div class="panel panel-default">
                    <div class="panel-heading"><fmt:message key="reservation" bundle="${index}"/> ${reservation.id}
                        <c:if test="${user.userType.id != 1}">
                            <a href="./controller?commandName=deleteReservationCommand&reservationId=${reservation.id}"><span
                                    class="glyphicon glyphicon-remove pull-right"></span></a>
                        </c:if>
                        <label class="pull-right">${reservation.requestDate}</label>
                    </div>
                    <div class="panel-body">
                        <ul class="list-group">
                            <li class="list-group-item"><fmt:message key="startDate"
                                                                     bundle="${index}"/><span
                                    class="pull-right">${reservation.dateFrom}</span></li>
                            <li class="list-group-item"><fmt:message key="endDate"
                                                                     bundle="${index}"/><span
                                    class="pull-right">${reservation.dateTo}</span>
                            </li>
                            <li class="list-group-item"><fmt:message key="reservationStatus"
                                                                     bundle="${index}"/><span
                                    class="pull-right"><fmt:message key="${reservation.status.name}"
                                                                    bundle="${reservationType}"/></span></li>
                        </ul>
                        <a type="button" class="btn btn-default pull-right"
                           href="./controller?commandName=getReservationProfileCommand&currentReservation=${reservation.id}"><fmt:message
                                key="details"
                                bundle="${index}"/></a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="col-md-1"></div>
</div>
</body>
</html>
