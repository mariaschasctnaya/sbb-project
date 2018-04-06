<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
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
    <script type="text/javascript" src="<c:url value="/js/topButton.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/manager/route.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.cookie.js"/>"></script>
</head>
<body onload="createDataTableRoute()">
<div class="container block-center-div">
    <div class="row align-items-end">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <div class="row">
                <div class="col-md-3">
                    <a href="<c:url value ="/stations"/>"
                       class="btn br2">STATIONS</a>
                </div>
                <div class="col-md-3">
                    <a href="<c:url value = "/routes"/>"
                       class="btn br2 disabled">ROUTES</a>
                </div>
                <div class="col-md-3">
                    <a href="<c:url value ="/trains"/>"
                       class="btn br2">TRAINS </a>
                </div>
                <div class="col-md-3">
                    <a href="<c:url value ="/logout"/>"
                       class="btn br2">LOG OUT </a>
                </div>
            </div>
        </div>
        <div class="col-md-3"></div>
    </div>
    <div class="container text-center text-light">
        <h1>&nbsp;</h1>
        <h1>Routes &nbsp; &nbsp; &nbsp;</h1>
    </div>
    <div class="container filter-blur rounded popin vcenter">
        <table id="routes_table" class="display" cellspacing="0" width="100%">
            <button id="create" class="btn br2">New</button>
            <button id="remove" class="btn br2" disabled>Archive</button>
            <div><input class="button-tools" type="checkbox" id="filter-archived">Show only active</input></div>
            <thead>
            <tr>
                <th style="width: 20px; height: 20px"></th>
                <th>Stations</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody id="routes_body" class="table table-hover text-dark">

            </tbody>
        </table>
    </div>
    <hr>

    <%--////////////////////////Modal Windows///////////////////////////////--%>

    <%--////////////////////////Create Routes////////////////////////////////--%>
    <div id="dialog-form" class="modal fade" role="dialog">
        <div class="modal-dialog popap-routes modal-lg">
            <div class="modal-content text-light">
                <div class="modal-header">
                    <h4 class="modal-title">New Route</h4>
                    <button id="closeCreateDialog" type="button" class="close">&times;</button>
                </div>
                <div class="modal-body" style="overflow:hidden">
                    <div id="block_error" class="alert alert-danger invisible">
                        <a id="close_message" class="close" href="<c:url value="#"/>">×</a><span
                            id="errorMessage"></span>
                    </div>
                    <div id="route_form">
                        <table id="route_creation_table" class="display" cellspacing="0" width="100%">
                            <button id="add-timetable-button" class="button-tools plus"></button>
                            <thead>
                            <tr>
                                <th>Station</th>
                                <th>Arrive time</th>
                                <th>Departure time</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody id="routes_creation_body" class="table table-hover">

                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button id="closeCreateDialogA" type="button" class="btn br2">Close</button>
                    <button type="button" id="createRouteButton" class="btn br2">Create</button>
                </div>
            </div>
        </div>
    </div>

    <%--////////////////////////Archive Routes////////////////////////////////--%>
    <div id="dialog-remove" class="modal fade">
        <div class="modal-dialog popap modal-lg">
            <div class="modal-content text-light">
                <div class="modal-header">
                    <h4 class="modal-title">Archive Route</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div class="modal-body">
                    <div id="delete_dialog_message">Do you really want archive this route<span
                            id="removeRoute"></span> ?
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn br2" data-dismiss="modal">Cancel</button>
                    <button id="removeRouteButton" type="button" class="btn br2">Archive</button>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
