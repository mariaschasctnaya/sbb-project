var Station = {


    init: function () {
        Station.table = Station.createDataTable();
        Station.table.on('click', 'tr.active', function () {
            Station.selectedStation = Station.table.api().row($(this)).data();
            StationTimeTable.load(Station.selectedStation.name);
            if ($(this).hasClass("selected")) {
                $(this).toggleClass("selected");
                Station.selectedStation = null;
                $('#remove').button("disable");
                return;
            }
            $(".selected").each(function () {
                $(this).removeClass("selected");
            });
            $(this).addClass("selected");
            $('#remove').button("enable");
        });

        $("#create").button().on("click", function () {
            Station.openCreateDialog();
        });

        $("#remove").button().on("click", function () {
            Station.openRemoveDialog()
        });

        $("#removeStationButton").button().on("click", function () {
            Station.removeStation();
        });

        $("#createStationButton").button().on("click", function () {
            Station.createStation();
        });

        $("#close_server_message").on("click", function () {
            $(".error-server-block").addClass("invisible");
        });

        $("#closeCreateDialog").on("click", function () {
            Station.closeCreateDialog();
        });

        $("#closeCreateDialogA").on("click", function () {
            Station.closeCreateDialog();
        });

        $("#close_message").on("click", function () {
            $("#block_error").addClass("invisible");
        });

        Station.selectedStation = null;

        $('#timetable_table').on('click', 'tr', function () {
            StationTimeTable.selectedTimetable = $('#timetable_table').dataTable().api().row($(this)).data();
            if ($(this).hasClass("selected")) {
                $(this).toggleClass("selected");
                StationTimeTable.selectedTimetable = null;
                $('#updateStatus').button("disable");
                return;
            }
            $(".selected").each(function () {
                $(this).removeClass("selected");
            });
            $(this).addClass("selected");
            $('#updateStatus').button("enable");
        });

    },

    openCreateDialog: function () {
        $("#block_error").addClass("invisible");
        $("#dialog-form").modal('show');
    },

    openRemoveDialog: function () {
        $("#removeStation").html(Station.selectedStation.name);
        $("#dialog-remove").modal('show');
    },

    createStation: function () {
        // var station = new Station($("#statinonName").val());
        var response = null;
        if (validateStationData()) {
            $.ajax({
                url: "station/",
                async: false,
                method: 'POST',
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    "name": $("#name").val()
                }),
                success: function (data) {
                    response = data;
                    Station.closeCreateDialog();
                    Station.reloadTable();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $("#error_server_message").html(jqXHR.responseJSON.message);
                    $(".error-server-block").removeClass("invisible");
                }
            });
        }

        function validateStationData() {
            var stationName = $("#name").val();
            var exceptionMessage = "";
            var reqExpForName = new RegExp("^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+$");
            if (!reqExpForName.test(stationName)) {
                $("#name").addClass("errorField");
                exceptionMessage += "Station Name;";
            }
            if (exceptionMessage != "") {
                $("#errorMessage").html("Incorrect format of next field: " + exceptionMessage);
                $("#block_error").removeClass("invisible");
                return false;
            }
            return true;
        }
    },
    removeStation: function() {
        var response = null;
        $.ajax({
            type: "DELETE",
            url: "station/" + Station.selectedStation.id,
            async: false,
            success: function (data) {
                response = data;
                Station.reloadTable();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $("#error_server_message").html(jqXHR.responseJSON.message);
                $(".error-server-block").removeClass("invisible");
            }
        });
        Station.closeRemoveDialog();

    },

    closeRemoveDialog: function () {
        $("#dialog-remove").modal('hide');
    },

    closeCreateDialog: function () {
        $("#station_form")[0].reset();
        $("#block_error").addClass("invisible");
        $("#dialog-form").modal('hide');
    },

    reloadTable: function () {
        Station.clearDataTable();
        Station.init();
    },

    clearDataTable: function() {
        $('#stations_table').dataTable().fnDestroy();
        StationTimeTable.clearDataTable();
    },

    createDataTable : function () {
        return $('#stations_table').dataTable({
            "sDom": "<'row'<'span4'l><'span4 search_table'f>r>t<'row'<'span4'i><'span4 offset4'p>>",
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sLengthMenu": "_MENU_ records per page"
            },
            "sAjaxSource": 'station/all',
            "sServerMethod": "GET",
            "sAjaxDataProp" : "",
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
                    "mData": "name",
                    "targets": 0
                },
                {
                    "mData": "status",
                    "targets": 1
                }
            ]
        });
    }
};

var StationTimeTable = {
    load: function (station) {
        StationTimeTable.clearDataTable();
        StationTimeTable.station = station;
        var timetableTable = StationTimeTable.createDataTable(station);
        $("#updateStatus").button().on("click", function () {
            StationTimeTable.openUpdateDialog()
        });

        $("#closeUpdateDialog").on("click", function () {
            StationTimeTable.closeUpdateDialog();
        });

        $("#closeUpdateDialogA").on("click", function () {
            StationTimeTable.closeUpdateDialog();
        });

        $("#updateStatusButton").button().on("click", function () {
            StationTimeTable.updateStatus();
        });
    },

    clearDataTable: function() {
        $('#timetable_table').dataTable().fnDestroy();
        $("#timetable_body").html("");
    },

    openUpdateDialog: function () {
        $("#delay").hide();
        $("#dialog-update").modal('show');
    },

    closeUpdateDialog: function () {
        $("#dialog-update").modal('hide');
    },

    updateStatus: function () {
        var status = $("#selectStatus :selected").val();
        var delayTime = $("#delayTime").val();
        if(status == "DELAYED" && !(/^\d+$/.test(delayTime))) {
            return;
        }
        var station = StationTimeTable.station;
        var trainNumber = StationTimeTable.selectedTimetable.number;
        $.ajax({
            url: "train/status",
            async: false,
            contentType: "application/json",
            dataType: "json",
            method: 'PUT',
            data: JSON.stringify({
                "status" : status,
                "station" : station,
                "trainNumber" : trainNumber,
                "delayTime" : (delayTime ? delayTime : null)
            }),
            success: function (data) {
                StationTimeTable.closeUpdateDialog();
                StationTimeTable.clearDataTable();
                StationTimeTable.station = station;
                var timetableTable = StationTimeTable.createDataTable(station);
            },
            error: function (response) {
                $("#errorMessage").html(response.message);
                $("#block_error").removeClass("invisible");
            }
        });
    },



    createDataTable : function (station) {
        return $('#timetable_table').dataTable({
            "sDom": "<'row'<'span4'l><'span4 search_table'f>r>t<'row'<'span4'i><'span4 offset4'p>>",
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sLengthMenu": "_MENU_ records per page"
            },
            "sAjaxSource": 'train/',
            "sServerMethod": "GET",
            "fnServerData": function ( sSource, aoData, fnCallback ) {
                $.ajax( {
                    "dataType": 'json',
                    "type": "GET",
                    "url": sSource,
                    "data":{
                        "q": station
                    },
                    "success": function(result){
                        fnCallback(result)
                    }
                } );
            },
            "sAjaxDataProp" : "",
            "columnDefs": [
                {
                    "mData": "number",
                    "targets": 0
                },
                {
                    "mData": "stationSchedules",
                    "render": function (data, type, row) {
                        return StationTimeTable.formatDate(data[station].arrive);
                    },
                    "targets": 1
                },
                {
                    "mData": "stationSchedules",
                    "render": function (data, type, row) {
                        return StationTimeTable.formatDate(data[station].departure);
                    },
                    "targets": 2
                },
                {
                    "mData": "stationSchedules",
                    "render": function (data, type, row) {
                        return "<div class='" + data[station].status + "'>" + data[station].status + "</div>";
                    },
                    "targets": 3
                }
            ]
        });
    },

    formatDate: function (stringDate) {
        var date = new Date(stringDate);
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'pm' : 'am';
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0'+minutes : minutes;
        var strTime = hours + ':' + minutes + ' ' + ampm;
        return date.getMonth()+1 + "/" + date.getDate() + "/" + date.getFullYear() + "  " + strTime;
    }

};

function createDataTableStation() {
    Station.init();
    $("#filter-archived").change(function () {
        if($(this).is(":checked")) {
            Station.table.api().search('ACTIVE').draw();
            Station.table.api().search('')
        }
        else {
            Station.table.api().search('').draw()
        }
    });
    $("#selectStatus").change(function () {
        if($(this).val() == "DELAYED") {
            $("#delay").show();
        }
        else {
            $("#delay").hide();
        }
    });
}