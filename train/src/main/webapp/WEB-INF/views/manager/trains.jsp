<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
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
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>" media="all">
    <script type="text/javascript" src="<c:url value="/js/table.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/manager/train.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/topButton.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.cookie.js"/>"></script>

</head>
<body onload="createDataTableTrain()">
<div class="container block-center-div">
    <div class="row align-items-end">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <div class="row">
                <div class="col-md-3">
                    <a href="<c:url value ="/stations"/>"
                       class="btn br2 ">STATIONS</a>
                </div>
                <div class="col-md-3">
                    <a href="<c:url value = "/routes"/>"
                       class="btn br2 ">ROUTES</a>
                </div>
                <div class="col-md-3">
                    <a href="<c:url value ="/trains"/>"
                       class="btn br2 disabled">TRAINS </a>
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
        <h1>Trains information &nbsp; &nbsp;</h1>
    </div>

    <div class="container filter-blur rounded popin vcenter">
        <div class="container-fluid">
            <table id="trains_table" class="display" cellspacing="0" width="100%">
                <button id="create" class="btn br2">New</button>
                <button id="remove" class="btn br2" disabled>Archive</button>
                <div>
                    <input class="button-tools" type="checkbox" id="filter-archived">
                    Show only active</input>
                </div>
                <thead>
                <tr>
                    <th>Train number</th>
                    <th>Start station</th>
                    <th>End station</th>
                    <th>Places</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody id="trains_body" class="table table-hover text-dark">

                </tbody>
            </table>
        </div>
    </div>
    <hr>

    <div class="container filter-blur rounded popin" id="trainTimetable">
        <div class="container-fluid">
            <h4>Passengers registered on the train</h4>
            <table id="passengers_table" class="table table-hover display" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Surname</th>
                    <th>Date of Birth</th>
                </tr>
                </thead>
                <tbody id="passengers_body" class="table table-hover text-dark">

                </tbody>
            </table>
        </div>
    </div>
</div>


<%--////////////////////////////////////////////Modal create/////////////////////////////////////////////--%>
<div id="dialog-form" class="modal fade" role="dialog">
    <div class="modal-dialog popap-routes modal-lg">
        <div class="modal-content text-light">
            <div class="modal-header">
                <h4 class="modal-title">New Train</h4>
                <button id="closeCreateDialog" type="button" class="close">&times;</button>
            </div>
            <div class="modal-body">
                <div id="block_error" class="alert alert-danger invisible">
                    <a id="close_message" class="close" href="<c:url value="#"/>">×</a><span id="errorMessage"></span>
                </div>
                <form id="train_form">
                    <fieldset>
                        <label style='float:left; margin-right: 20px' for="number" class="form-label">Number:
                            <input type="text" name="number" id="number"
                                   class="form-field text ui-widget-content ui-corner-all"></label>
                        <label style='float:left; margin-right: 20px' for="routes-selector" class="form-label">Route:&nbsp;&nbsp;&nbsp;&nbsp;
                            <select id="routes-selector" name="routes-selector"
                                    class="form-field text ui-widget-content ui-corner-all" style="width: 500px; text-align: center"></select></label>
                        <label style='float:left; margin-right: 20px' for="places" class="form-label">Places:&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="text" name="places" id="places"
                                   class="form-field text ui-widget-content ui-corner-all"></label>
                    </fieldset>
                </form>
            </div>
            <div class="modal-footer">
                <button id="closeCreateDialogA" type="button" class="btn br2">Close</button>
                <button type="button" id="createTrainButton" class="btn br2">Create</button>
            </div>
        </div>
    </div>
</div>


<%--/////////////////////////////////////Modal archive////////////////////////////////////////////////////--%>
<div id="dialog-remove" class="modal fade">
    <div class="modal-dialog popap modal-lg">
        <div class="modal-content text-light">
            <div class="modal-header">
                <h4 class="modal-title">Archive Train</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div id="delete_dialog_message">Do you really want archive train with number :<span
                        id="removeTrain"></span> ?
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn br2" data-dismiss="modal">Cancel</button>
                <button id="removeTrainButton" type="button" class="btn br2">Archive</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>