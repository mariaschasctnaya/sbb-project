var Train = {
    init: function () {
        Train.table = Train.createDataTable();
        Train.table.on('click', 'tr.active', function () {
            Train.selectedTrain = Train.table.api().row($(this)).data();
            TrainPassengers.load(Train.selectedTrain.number);
            if ($(this).hasClass("selected")) {
                $(this).toggleClass("selected");
                Train.selectedTrain = null;
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
            Train.openCreateDialog();
        });

        $("#remove").button().on("click", function () {
            Train.openRemoveDialog()
        });

        $("#removeTrainButton").button().on("click", function () {
            Train.removeTrain();
        });

        $("#createTrainButton").button().on("click", function () {
            Train.createTrain();
        });

        $("#close_server_message").on("click", function () {
            $(".error-server-block").addClass("invisible");
        });

        $("#closeCreateDialog").on("click", function () {
            Train.closeCreateDialog();
        });

        $("#closeCreateDialogA").on("click", function () {
            Train.closeCreateDialog();
        });

        $("#close_message").on("click", function () {
            $("#block_error").addClass("invisible");
        });

        Train.selectedTrain = null;
    },

    openCreateDialog: function () {
        $.ajax({
            url: "route",
            contentType: "application/json",
            dataType: "json",
            async: false,
            method: 'GET',
            success: function(data) {
                var options='';
                data.forEach(function (item, index, arr) {
                    var stations = Object.keys(item.stationSchedules);
                    options+='<option data-value=' + item.id + '>' +
                        stations[0] +' (' + Train.formatDate(item.stationSchedules[stations[0]].departure) + ') - ' +
                        stations[stations.length - 1] + ' (' + Train.formatDate(item.stationSchedules[stations[stations.length - 1]].arrive)  + ')' +
                        '</option>'
                });
                $("#routes-selector").html(options);
            }
        });
        $("#dialog-form").modal('show');
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
    },

    openRemoveDialog: function () {
        $("#removeTrain").html(Train.selectedTrain.number);
        $("#dialog-remove").modal('show');
    },

    createTrain: function () {
        var response = null;
        $.ajax({
            url: "train/",
            async: false,
            method: 'POST',
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                "number" : $("#number").val(),
                "routeId" : $("#routes-selector").find(":selected").data("value"),
                "places": $("#places").val()
            }),
            success: function (data) {
                response = data;
                Train.closeCreateDialog();
                Train.reloadTable();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $("#error_server_message").html(jqXHR.responseJSON.message);
                $(".error-server-block").removeClass("invisible");
            }
        });
    },

    removeTrain: function () {
        var response = null;
        $.ajax({
            type: "DELETE",
            url: "train/" + Train.selectedTrain.id,
            async: false,
            success: function (data) {
                response = data;
                Train.closeCreateDialog();
                Train.reloadTable();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $("#error_server_message").html(jqXHR.responseJSON.message);
                $(".error-server-block").removeClass("invisible");
            }
        });
        Train.closeRemoveDialog();

    },

    closeRemoveDialog: function () {
        $("#dialog-remove").modal('hide');
    },

    closeCreateDialog: function () {
        $("#train_form")[0].reset();
        $("#block_error").addClass("invisible");
        $("#dialog-form").modal('hide');
    },

    reloadTable: function () {
        Train.clearDataTable();
        Train.init();
    },

    clearDataTable: function() {
        $('#trains_table').dataTable().fnDestroy();
        $("#trains_body").html("");
        TrainPassengers.clearDataTable();
    },

    createDataTable : function () {
        return $('#trains_table').dataTable({
            "sDom": "<'row'<'span4'l><'span4 search_table'f>r>t<'row'<'span4'i><'span4 offset4'p>>",
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sLengthMenu": "_MENU_ records per page"
            },
            "sAjaxSource": 'train/all',
            "sServerMethod": "GET",
            "sAjaxDataProp" : "",
            "fnServerData": function ( sSource, aoData, fnCallback ) {
                $.ajax( {
                    "dataType": 'json',
                    "type": "GET",
                    "contentType": "application/json",
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
                    "mData": "number",
                    "targets": 0
                },
                {
                    "mData": "stationSchedules",
                    "render": function (data, type, row) {
                        return Object.keys(data)[0];
                    },
                    "targets": 1
                },
                {
                    "mData": "stationSchedules",
                    "render": function (data, type, row) {
                        var stations = Object.keys(data);
                        return stations[stations.length - 1];
                    },
                    "targets": 2
                },
                {
                    "mData": "places",
                    "targets": 3
                },
                {
                    "mData": "status",
                    "targets": 4
                }
            ]
        });
    }
};

var TrainPassengers = {
    load: function (train) {
        TrainPassengers.clearDataTable();
        TrainPassengers.createDataTable(train);
    },

    clearDataTable: function() {
        $('#passengers_table').dataTable().fnDestroy();
        $("#passengers_body").html("");
    },

    createDataTable : function (train) {
        return $('#passengers_table').dataTable({
            "sDom": "<'row'<'span4'l><'span4 search_table'f>r>t<'row'<'span4'i><'span4 offset4'p>>",
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sLengthMenu": "_MENU_ records per page"
            },
            "sAjaxSource": 'passenger',
            "fnServerData": function ( sSource, aoData, fnCallback ) {
                $.ajax( {
                    "dataType": 'json',
                    "type": "GET",
                    "url": sSource,
                    "data":{
                        "train": train
                    },
                    "success": function(result){
                        fnCallback(result)
                    }
                } );
            },
            "sAjaxDataProp" : "",
            "aoColumns": [
                { "mData": "name" },
                { "mData": "surname" },
                { "mData": "birthday" }
            ]
        });
    }
};

function createDataTableTrain() {
    Train.init();
    $("#filter-archived").change(function () {
        if($(this).is(":checked")) {
            Train.table.api().search('ACTIVE').draw();
            Train.table.api().search('')
        }
        else {
            Train.table.api().search('').draw()
        }
    });
}