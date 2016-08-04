<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="hotelRoomList" type="java.util.List" scope="session"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="pvList" uri="/WEB-INF/paramValueList.tld" %>
<fmt:setBundle basename="roomList" var="rList"/>
<fmt:setBundle basename="main" var="main"/>
<fmt:setBundle basename="valueListBundle" var="valueList"/>
<fmt:setBundle basename="parameterListResource" var="paramList"/>
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


<div class="page-header text-center">
    <h1><fmt:message key="roomListHeader" bundle="${rList}"/></h1>
</div>
<div class="row">
    <div class="col-md-1"></div>
    <div class="col-md-10">
        <c:forEach items="${hotelRoomList}" var="hotelRoom">
            <div class="panel-group">
                <div class="panel panel-default">
                    <div class="panel-heading"><fmt:message key="room" bundle="${rList}"/> № ${hotelRoom.roomName}
                        <label class="pull-right">${hotelRoom.price}</label>
                    </div>
                    <div class="panel-body">
                        <pvList:paramValueList list="${hotelRoom.parameters}" parameterBundle="${paramList}"
                                               valueBundle="${valueList}"/>
                        <label class="pull-left"><fmt:message key="isActive" bundle="${rList}"/>
                            <fmt:message key="${hotelRoom.activationStatus}" bundle="${rList}"/>
                        </label>

                        <a type="button" class="btn btn-default pull-right"
                           href="./controller?commandName=getHotelRoomProfileCommand&hotelRoomId=${hotelRoom.roomID}">
                            <fmt:message key="details" bundle="${rList}"/></a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="col-md-1"></div>
</div>
</body>
</html>
