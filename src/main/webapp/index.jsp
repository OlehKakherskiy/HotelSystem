<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<jsp:useBean id="user" type="main.java.com.epam.project4.model.entity.User" scope="session"/>
<jsp:useBean id="reservationList" type="java.util.List" scope="request"/>
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
            <li><a href="./controller?commandName=getReservationListCommand">Home</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="./controller?commandName=logoutCommand"><span class="glyphicon glyphicon-log-out"></span>
                Logout</a></li>
        </ul>
    </div>
</nav>
<div class="row">
    <div class="col-md-4"></div>
    <div class="col-md-4" id="userInfo">
        <div id="personalInfo">
            <h4>Personal information</h4>
            <form class="form-horizontal">
                <div class="form-group">
                    <label for="name" class="col-sm-2 control-label">Name</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="name" placeholder="${user.name}" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label for="surname" class="col-sm-2 control-label">Surname</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="surname" placeholder="${user.lastName}" disableud>
                    </div>
                </div>
            </form>
        </div>
        <div id="mobilePhonesInfo">
            <h4>Mobile phones:</h4>
            <form class="form-horizontal">
                <c:forEach items="${user.mobilePhoneList}" var="mobilePhone">
                    <div class="form-group">
                        <label for="mobilePhone" class="col-sm-2 control-label">Phone</label>
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
            <div class="pull-left"><h4>Reservations</h4></div>
            <c:if test="${user.userType.id != 1}">
                <a type="button" class="btn btn-primary pull-right"
                   href="./controller?commandName=prepareReservationPageCommand">New reservation</a>
            </c:if>
            <div class="dropdown pull-right">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Reservation type
                    <span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <li>
                        <a href="./controller?commandName=getReservationListCommand&reservationStatus=1">processing</a>
                    </li>
                    <li>
                        <a href="./controller?commandName=getReservationListCommand&reservationStatus=2">answered</a>
                    </li>
                    <li>
                        <a href="./controller?commandName=getReservationListCommand&reservationStatus=3">refused</a>
                    </li>
                    <li>
                        <a href="./controller?commandName=getReservationListCommand&reservationStatus=4">submitted</a>
                    </li>
                    <li>
                        <a href="./controller?commandName=getReservationListCommand&reservationStatus=-1">all</a>
                    </li>

                </ul>
            </div>
        </div>

        <div class="panel-group">
            <c:forEach var="reservation" items="${reservationList}">
                <div class="panel panel-default">
                    <div class="panel-heading">Reservation ${reservation.id}
                        <c:if test="${user.userType.id != 1}">
                            <a href=""><span class="glyphicon glyphicon-remove pull-right"></span> </a>
                        </c:if>
                        <label class="pull-right">${reservation.requestDate}</label>
                    </div>
                    <div class="panel-body">
                        <ul class="list-group">
                            <li class="list-group-item">Start Date<span
                                    class="pull-right">${reservation.dateFrom}</span></li>
                            <li class="list-group-item">End Date<span class="pull-right">${reservation.dateTo}</span>
                            </li>
                            <li class="list-group-item">Status<span class="pull-right">${reservation.status}</span></li>
                        </ul>
                        <a type="button" class="btn btn-default pull-right"
                           href="./controller?commandName=getReservationProfileCommand&currentReservation=${reservation.id}">Details</a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="col-md-1"></div>
</div>
</body>
</html>
