<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<jsp:useBean id="user" type="main.java.com.epam.project4.model.entity.User" scope="session"/>
<jsp:useBean id="reservationList" type="java.util.List" scope="session"/>
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
    <link rel="stylesheet" href="<c:url value="../css/login.css"/>">
</head>
<body>

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
            <div class="reservationTitle pull-left"><h4>Reservations</h4></div>
            <button type="button" class="btn btn-primary pull-right">New reservation</button>
            <div class="dropdown pull-right">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Reservation
                    type
                    <span class="caret"></span></button>
                <ul class="dropdown-menu">
                    <li><a href="#">HTML</a></li>
                    <li><a href="#">CSS</a></li>
                    <li><a href="#">JavaScript</a></li>
                </ul>
            </div>
        </div>

        <div class="panel-group">
            <c:forEach var="reservation" items="reservationList">

                <div class="panel panel-default">
                    <div class="panel-heading">Reservation ${reservation.id}
                        <a href=""><span class="glyphicon glyphicon-remove pull-right"></span> </a>
                        <label class="pull-right">${reservation}</label>
                    </div>
                    <div class="panel-body">
                        <ul class="list-group">
                            <li class="list-group-item">Start Date<span
                                    class="pull-right">${reservation.dateFrom}</span></li>
                            <li class="list-group-item">End Date<span class="pull-right">${reservation.dateTo}</span>
                            </li>
                            <li class="list-group-item">Status<span class="pull-right">${reservation.status}</span></li>
                        </ul>
                        <button type="button" class="btn btn-default pull-right">Details</button>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">Header2</div>
                    <div class="panel-body">Panel Content</div>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="col-md-1"></div>
</div>
</body>
</html>
