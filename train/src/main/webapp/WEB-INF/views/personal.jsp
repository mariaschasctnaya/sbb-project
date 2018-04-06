<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Tickets</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap-responsive.css"/>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap.css"/>" media="all">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.dataTables.css"/>" media="all">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery-ui.min.css"/>" media="all">
    <script type="text/javascript" src="<c:url value="/js/jquery.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/head.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/table.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>" media="all">
    <script type="text/javascript" src="<c:url value="/js/user/personal.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
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
                        <a href="<c:url value = "/train"/>"
                           class="btn br2">TRAINS</a>
                    </div>
                    <div class="col-md-4">
                        <a href="<c:url value ="/logout"/>"
                           class="btn br2">LOG
                            OUT</a>
                    </div>
                </div>
            </div>

            <div class="col-md-3"></div>
        </div>

        <div class="container filter-blur rounded popin inner">
            <h1 class="text-light text-center">Tickets</h1>
            <table id="tickets" class="table table-hover display">
                <thead class="text-light">
                <tr>
                    <th><h5>Train information</h5></th>
                    <th><h5>Passenger Name</h5></th>
                    <th><h5>Passenger Surname</h5></th>
                    <th><h5>Departure Station</h5></th>
                </tr>
                </thead>
                <tbody id="tickets-body" class="table table-hover text-dark">
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
