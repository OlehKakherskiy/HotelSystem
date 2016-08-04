<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setBundle basename="main" var="main"/>
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