<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="<c:url value="/css/bootstrap-responsive.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap.css"/>" media="all">
    <link rel="stylesheet" href="<c:url value="/css/bootstrap-datetimepicker.min.css"/>" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>" media="all">
    <script type="text/javascript" src="<c:url value="/js/user/objects.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/user/tickets.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/modalWindows.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/user/objects.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.cookie.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.dataTables.css"/>" media="all">
    <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.js"/>"></script>

</head>
<body>
<div class="container block-center-div">
    <div class="row align-items-end">
        <div class="col-md-3"></div>

        <div class="col-md-6">
            <div class="row">
                <div class="col-md-4">
                    <a href="<c:url value = "/timetable"/>"
                       class="btn br2">TIMETABLE</a>
                </div>
                <div class="col-md-4">
                    <a href="<c:url value = "/"/>"
                       class="btn br2 ">TRAINS</a>
                </div>

                <c:choose>
                    <c:when test="${pageContext.request.isUserInRole('ROLE_USER')}">
                        <div class="col-md-4">
                            <a href="<c:url value ="/personal"/>"
                               class="btn br2">MY TICKETS</a>
                        </div>
                    </c:when>
                    <c:when test="${pageContext.request.isUserInRole('ROLE_MANAGER')}">
                        <div class="col-md-4">
                            <a href="<c:url value ="/logout"/>"
                               class="btn br2">LOG
                                OUT</a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="col-md-4">
                            <a href="<c:url value ="/login"/>"
                               class="btn br2">LOG
                                IN</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="col-md-3"></div>
    </div>
    <div class="container text-center text-light">
        <h1>&nbsp;</h1>
        <h1>Buy ticket!</h1>
    </div>


    <div class="alert alert-success alert-dismissable" id = "successAlertDiv">
        <a href="<c:url value="#"/>" class="close" aria-label="close" onclick="hideModalWindow('successAlertDiv')">&times;</a>
        <strong id = "successAlertMessage"></strong>
    </div>

    <div class="alert alert-danger alert-dismissable" id = "errorAlertDiv">
        <a href="<c:url value="#"/>" class="close" aria-label="close" onclick="hideModalWindow('errorAlertDiv')">&times;</a>
        <strong id ="errorMessage"></strong>
    </div>



    <div class="container filter-blur rounded popin vcenter">
        <h1>Ticket information</h1>
        <table class="table table-hover text-light">
            <tr>
                <th>Train number</th>
                <th>Departure station</th>
                <th>Departure time</th>
                <th>Arrive station</th>
                <th>Arrive time</th>
            </tr>
            <tr>
                <td id="trainId"></td>
                <td id ="depStation"></td>
                <td id ="depTime"></td>
                <td id ="arrStation"></td>
                <td id ="arrTime"></td>
            </tr>
        </table >
    </div>
    <hr>
    <div class="container filter-blur rounded popin">
        <h1>Passenger information</h1>
        <table class="table table-hover">
            <tr>
                <th>Surname</th>
                <th>Name</th>
                <th>Birthday</th>
            </tr>
            <tr>
                <td><input class="input-block-level inputField" id = "userSurname"/></td>
                <td><input class="input-block-level inputField" id = "userName"/></td>
                <td><input type="date" class="form-control inputField" id ="birthDate"/></td>
            </tr>
        </table>
        <div class="container">
            <div class="box box_right">
                <button class="btn btn-large" onclick ="buyTicket()">Buy ticket</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>