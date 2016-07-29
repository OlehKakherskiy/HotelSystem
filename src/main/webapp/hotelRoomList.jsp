<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="hotelRoomList" type="java.util.List" scope="request"/>
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

<div class="page-header text-center">
    <h1>Room list</h1>
</div>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <c:forEach items="${hotelRoomList}" var="hotelRoom">
            <div class="panel-group">
                <div class="panel panel-default">
                    <div class="panel-heading">Room № ${hotelRoom.id}
                        <label class="pull-right">price</label>
                    </div>
                    <div class="panel-body">
                        <ul class="list-group">
                            <c:forEach items="${hotelRoom.parameters}" var="parameterValue">
                                <li class="list-group-item">${parameterValue.parameter.paramName}<span
                                        class="pull-right">${parameterValue.value.value}</span></li>
                            </c:forEach>
                        </ul>
                        <label class="pull-left">isActive=${hotelRoom.isActiveStatus}</label>

                        <a type="button" class="btn btn-default pull-right"
                           href="./controller?commandName=getHotelRoomProfileCommand&hotelRoomId=${hotelRoom.roomID}">Details</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="col-md-1"></div>
</div>
</body>
</html>
