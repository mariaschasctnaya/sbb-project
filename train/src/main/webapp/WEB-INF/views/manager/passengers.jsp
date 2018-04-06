<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap.css"/>" media="all">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>" media="all">
    <script type="text/javascript" src="<c:url value="/js/jquery-1.12.4.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/topButton.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.cookie.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/manager/passengers.js"/>"></script>

    <link rel="stylesheet" type="text/css" href="<c:url value="/css/dataTables.bootstrap.css"/>" media="all">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery.dataTables.css"/>" media="all">
    <script type="text/javascript" src="<c:url value="/js/jquery.dataTables.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/dataTables.bootstrap.js"/>"></script>
</head>
<body>

<div class="container">
    <a class="navbar-link"><img src="<c:url value="/images/header.jpg"/>" class="img-rounded bot-img"></a>
    <div class="navbar nav-top">
        <div class="navbar-inner">
            <div class="container">
                <ul class="nav pull-left">
                    <li><a class="navbar-link" href="<c:url value="/stations"/>">Stations</a></li>
                    <li><a class="navbar-link" onclick="/trains">Trains</a></li>
                </ul>
                <ul class="nav pull-right">
                    <li><a class="navbar-link" href="<c:url value="/logout"/>">Logout</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <h1>Train <label id="trainId"></label></h1>
    <h3>Passengers</h3>
        <table id="tableSmart" class="table table-hover">
            <thead>
            <tr>
                <th>Name</th>
                <th>Surname</th>
                <th>Birthday</th>
            </tr>
            </thead>
            <tbody id="tablePassengers">
            </tbody>
        </table>
</div>

</body>

<script>
    $(document).ready(function () {
        $("#trainId").text($.cookie("trainIdPass"));
        loadPassengers($.cookie("trainIdPass"));
    });
</script>
</html>