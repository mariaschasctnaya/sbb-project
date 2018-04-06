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
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>" media="all">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.dataTables.css"/>" media="all">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery-ui.min.css"/>" media="all">
    <script type="text/javascript" src="<c:url value="/js/jquery.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/head.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/topButton.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/user/timetable.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.cookie.js"/>"></script>
</head>
<body onload="createDataTableStation()">
<div class="container block-center-div">
    <div class="row align-items-end">
        <div class="col-md-3"></div>

        <div class="col-md-6">
            <div class="row">
                <div class="col-md-4">
                    <a href="<c:url value = "/timetable"/>"
                       class="btn br2 disabled">TIMETABLE</a>
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
        <h1>Stations&nbsp; &nbsp;</h1>
    </div>

    <div class="container  filter-blur rounded popin vcenter">
        &nbsp;
        <div class="container">
            <table id="stations_table" class="display text-dark" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th><h3 class="text-light">Station name</h3></th>
                </tr>
                </thead>
                <tbody id="stations_body" class="table table-hover">

                </tbody>
            </table>
        </div>
    </div>
    &nbsp;
    <div class="container  filter-blur rounded popin ">
        <div class="container" id="stationTimetable">
            <h3>Station's timetable</h3>
            <table id="timetable_table" class="table table-hover display" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>Train</th>
                    <th>Arrival</th>
                    <th>Departure</th>
                </tr>
                </thead>
                <tbody id="timetable_body" class="table table-hover text-dark">

                </tbody>
            </table>
        </div>
        <div id="toTop">^</div>
    </div>
</div>
</body>
</html>
