var Route = {


    init: function () {
        Route.table = Route.createDataTable();
        Route.table.on('click', 'tr.active', function () {
            Route.selectedRoute = Route.table.api().row($(this).closest('tr')).data();
            if ($(this).hasClass("selected")) {
                $(this).toggleClass("selected");
                Route.selectedRoute = null;
                $('#remove').button("disable");
                return;
            }
            $(".selected").each(function () {
                $(this).removeClass("selected");
            });
            $(this).addClass("selected");
            $('#remove').button("enable");
        }).on('click', 'td.details-control', function () {
            var tr = $(this).closest('tr');
            var row = Route.table.api().row(tr);

            if ( row.child.isShown() ) {
                row.child.hide();
                tr.removeClass('shown');
            }
            else {
                row.child( Route.formatExpandedRow(row.data()) ).show();
                tr.addClass('shown');
            }
        } );

        $("#create").button().on("click", function () {
            Route.openCreateDialog();
        });

        $("#remove").button().on("click", function () {
            Route.openRemoveDialog()
        });

        $("#removeRouteButton").button().on("click", function () {
            Route.removeRoute();
        });

        $("#createRouteButton").button().on("click", function () {
            Route.createRoute();
        });

        $("#close_server_message").on("click", function () {
            $(".error-server-block").addClass("invisible");
        });

        $("#closeCreateDialog").on("click", function () {
            Route.closeCreateDialog();
        });

        $("#closeCreateDialogA").on("click", function () {
            Route.closeCreateDialog();
        });

        $("#close_message").on("click", function () {
            $("#block_error").addClass("invisible");
        });

        Route.selectedRoute = null;

    },

    formatExpandedRow: function (row) {
        var table =
            "<table>" +
            "<thead>" +
            "<tr>" +
            "<th>Station</th>" +
            "<th>Time arrive</th>" +
            "<th>Time departure</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody>";
        Object.keys(row.stationSchedules).forEach(function(station, index) {
            table+="<tr>";
            table+="<td>" + station + "</td>";
            var arrive = new Date(this[station].arrive);
            table+="<td>" + Route.formatDate(arrive) + "</td>";
            var departure = new Date(this[station].departure);
            table+="<td>" + Route.formatDate(departure) + "</td>";
            table+="</tr>";
        }, row.stationSchedules);
        table+=
            "</tbody>" +
            "</table>";
        return table;
    },

    formatDate: function (date) {
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'pm' : 'am';
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0'+minutes : minutes;
        var strTime = hours + ':' + minutes + ' ' + ampm;
        return date.getMonth()+1 + "/" + date.getDate() + "/" + date.getFullYear() + "  " + strTime;
    },

    openCreateDialog: function () {
        var dialog = $("#dialog-form").modal('show');
        CreationRouteDialog.init(dialog);
    },

    openRemoveDialog: function () {
        $("#dialog-remove").modal('show');
    },

    createRoute: function () {
        var stationSchedules = {};
        CreationRouteDialog.table.fnGetData().forEach(function (item, index, array) {
            stationSchedules[item.station] = {arrive: item.arrive, departure: item.departure};
        });
        var response = null;
        $.ajax({
            url: "route",
            async: false,
            method: 'POST',
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                "stationSchedules": stationSchedules
            }),
            success: function (data) {
                response = data;
                Route.closeCreateDialog();
                Route.reloadTable();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $("#error_server_message").html(jqXHR.responseJSON.message);
                $(".error-server-block").removeClass("invisible");
            }
        });
    },

    removeRoute: function () {
        var response = null;
        $.ajax({
            type: "DELETE",
            url: "route/" + Route.selectedRoute.id,
            async: false,
            success: function (data) {
                response = data;
                Route.reloadTable();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $("#error_server_message").html(jqXHR.responseJSON.message);
                $(".error-server-block").removeClass("invisible");
            }

        });
        Route.closeRemoveDialog();

    },

    closeRemoveDialog: function () {
        $("#dialog-remove").modal('hide');
    },

    closeCreateDialog: function () {
        $("#block_error").addClass("invisible");
        $("#dialog-form").modal('hide');
        CreationRouteDialog.destroy();
    },

    reloadTable: function () {
        Route.clearDataTable();
        Route.createDataTable();
    },

    clearDataTable: function () {
        $('#routes_table').dataTable().fnDestroy();
        $("#routes_body").html("");
    },

    createDataTable : function () {
        return $('#routes_table').dataTable({
            "sDom": "<'row'<'span4'l><'span4 search_table'f>r>t<'row'<'span4'i><'span4 offset4'p>>",
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sLengthMenu": "_MENU_ records per page"
            },
            "sAjaxSource": 'route/all',
            "sServerMethod": "GET",
            "fnServerData": function ( sSource, aoData, fnCallback ) {
                $.ajax( {
                    "dataType": 'json',
                    "type": "GET",
                    "url": sSource,
                    "success": function(result){
                        fnCallback(result)
                    }
                } );
            },
            "sAjaxDataProp" : "",
            "fnCreatedRow": function( nRow, aData, iDataIndex ) {
                if ( aData.status == "ACTIVE" )
                {
                    $(nRow).addClass('active');
                }
                else
                {
                    $(nRow).find('td').addClass('inactive');
                }
            },

            "columnDefs": [
                {
                    "className":      'details-control',
                    "orderable":      false,
                    "data":           null,
                    "defaultContent": '',
                    "targets": 0
                },
                {
                    "className": "not-active",
                    "mData": "stationSchedules",
                    "render": function ( data, type, row ) {
                        var stations = Object.keys(data);
                        return stations[0] + '-' + stations[stations.length-1];
                    },
                    "targets": 1
                },
                {
                    "mData": "status",
                    "targets": 2
                }

            ]
        });
    }
};

var CreationRouteDialog = {

    RowEntry: function (station, arrive, departure) {
        this.station = station;
        this.arrive = arrive;
        this.departure = departure;
    },

    init: function (dialog) {
        CreationRouteDialog.table = CreationRouteDialog.createDataTable();
        CreationRouteDialog.dialog = dialog;
        CreationRouteDialog.addEmptyRow();
        $("#add-timetable-button").click(function (event) {
            event.stopPropagation();
            CreationRouteDialog.addEmptyRow();
        });
    },

    destroy: function () {
        $('#route_creation_table').dataTable().fnDestroy();
        $("#routes_creation_body").html("");
    },

    addEmptyRow: function () {
        CreationRouteDialog.table.api().row.add(new CreationRouteDialog.RowEntry('','','')).draw(false);
    },

    createDataTable : function () {
        var stations = [];
        $.ajax({
            url: "station/",
            async: false,
            contentType: "application/json",
            dataType: "json",
            method: 'GET',
            success: function(data) {
                $.map(data, function (value, key) {
                    stations.push({"label": value.name, "value": value.name});
                });
            }
        });
        return $('#route_creation_table').dataTable({
            "sDom": "<'row'<'span4'l><'span4 search_table'>r>t<'row'<'span4'i><'span4 offset4'p>>",
            "oLanguage": {
                "sEmptyTable": "Add at least one timetable",
                "sLengthMenu": "_MENU_ records per page",
                "sInfo": ""
            },
            "bPaginate": false,
            "bFilter": "false",
            "info": false,
            "columnDefs": [
                {
                    "orderable": false,
                    "mData": "station",
                    "render": function (data, type, row) {
                        return "<input class='form-field text ui-widget-content ui-corner-all' type='text'/>"
                    },
                    "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).find('input').autocomplete({
                            source: stations,
                            appendTo : CreationRouteDialog.dialog
                        }).focusout(function() {
                            rowData.station = $(this).val();
                        });
                    },
                    "targets": 0
                },
                {
                    "orderable": false,
                    "mData": "arrive",
                    "render": function (data, type, row) {
                        return "<input class='form-field text ui-widget-content ui-corner-all' type='datetime-local'/>"
                    },
                    "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).find('input').focusout(function() {
                            rowData.arrive = $(this).val();
                        });
                    },
                    "targets": 1
                },
                {
                    "orderable": false,
                    "mData": "departure",
                    "render": function (data, type, row) {
                        return "<input class='form-field text ui-widget-content ui-corner-all' type='datetime-local'/>"
                    },
                    "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).find('input').focusout(function() {
                            rowData.departure = $(this).val();
                        });
                    },
                    "targets": 2
                },
                {
                    "className" : 'remove-row',
                    "orderable": false,
                    "data": null,
                    "defaultContent": '',
                    "createdCell": function (td, cellData, rowData, row, col) {
                        $(td).click(function () {
                            var row = $(this).closest("tr");
                            CreationRouteDialog.table.fnDeleteRow(row);
                        });
                    },
                    "targets": 3
                }
            ]
        });
    }
};

function createDataTableRoute() {
    Route.init();
    $("#filter-archived").change(function () {
        if($(this).is(":checked")) {
            Route.table.api().search('ACTIVE').draw();
            Route.table.api().search('')
        }
        else {
            Route.table.api().search('').draw()
        }
    });
}