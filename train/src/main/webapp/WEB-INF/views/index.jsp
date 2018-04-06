<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

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
    <script type="text/javascript" src="<c:url value="/js/modalWindows.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/user/objects.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/user/trains.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/user/tickets.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>" media="all">
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
                    <a href="<c:url value = "/train"/>"
                       class="btn br2 disabled">TRAINS</a>
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
        <h1>Find you train! &nbsp;</h1>
    </div>

    <div class="alert alert-danger alert-dismissable" id = "errorAlertDiv">
        <a href="<c:url value="#"/>" class="close" aria-label="close" onclick="hideModalWindow('errorAlertDiv')">&times;</a>
        <strong id ="errorMessage"></strong>
    </div>

    <div class="container font-weight-bold center vcenter">

        <form role="form" class="form-horizontal">
            <div class="row text-light  filter-blur rounded popin">
                <div class="col-xs-1"></div>
                <div class="form-group col-md-3">
                    <label class="control-label col-md-2"><h4>FROM</h4></label>
                    <input type="text" class="input-block-level" id="depStation"/>
                </div>

                <div class="form-group col-md-3">
                    <label class="control-label col-md-1 text-right"><h4>TO</h4></label>
                    <input class="input-block-level" type="text" id="arrStation"/>
                </div>

                <div class="form-group col-md-4">
                    <label class="control-label text-right col-md-2"><h4>DATE</h4></label>
                    <input class="input-block-level form-text" id="dateDeparture" type="date"/>

                    <div class="sliders_step1">
                        <div id="time-ranger">
                        </div>
                    </div>

                    <p><span id="slider-time"></span></p>
                </div>

                <div class="col-md-1">
                    <input type="button" value="SEARCH"
                           class="btn br2-search vcenter"
                           onclick="findTrains()"/>
                </div>
            </div>
        </form>
    </div>

    <div class="container filter-blur rounded popin inner">
        <h3 class="text-light text-center">Train information:</h3>
        <table class="table" id="tableSmart" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>Train number:</th>
                <th>Departure:</th>
                <th>Arrival:</th>
                <th>Free places:</th>
                <th></th>
            </tr>
            </thead>
            <tbody id="trainTableBody" class="table table-hover text-dark">
            </tbody>
        </table>
    </div>
</div>
</body>
</html>