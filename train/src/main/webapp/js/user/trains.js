var table;

function buyTicketToTrain(button) {
    var row = table.api().row($(button).closest('tr')).data();
    $.cookie('trainId', row.number);
    $.cookie('depTime', row.stationSchedules[$("#depStation").val()]["departure"]);
    $.cookie('arrTime', row.stationSchedules[$("#arrStation").val()]["arrive"]);
    $.cookie('depStation', $("#depStation").val());
    $.cookie('arrStation', $("#arrStation").val());
    location.href = 'tickets';
}

function findTrains() {
    hideModalWindow("errorAlertDiv");
    var depStation = $("#depStation").val();
    var arrStation = $("#arrStation").val();
    var dateDeparture = $("#dateDeparture").val();
    var val0 = $("#time-ranger").slider("values", 0);
    var val1 = $("#time-ranger").slider("values", 1);
    if (validateTrainDataData(depStation, arrStation, dateDeparture)) {
        searchTrain(depStation, arrStation, dateDeparture, val0, val1);
    }
}

function formatTime(time) {
    var hours = parseInt(time / 60 % 24, 10);
    if (hours.toString().length == 1) {
        hours = "0" + hours;
    }
    var minutes = parseInt(time % 60, 10);
    if (minutes.toString().length == 1) {
        minutes = "0" + minutes;
    }
    return hours + ":" + minutes;

}

function searchTrain (depStation,arrStation,dateDeparture,val0,val1) {
    $('#tableSmart').dataTable().fnDestroy();
    $("#trainTableBody").html("");
    table = $('#tableSmart').dataTable({
        "sDom": "<'row'<'span4'l><'span4 search_table'f>r>t<'row'<'span4'i><'span4 offset4'p>>",
        "oLanguage": {
            "sLengthMenu": "_MENU_ records per page"
        },
        "sAjaxSource": "train/search",
        "sAjaxDataProp": "",
        "fnServerData": function ( sSource, aoData, fnCallback ) {
            $.ajax( {
                "dataType": 'json',
                "type": "GET",
                "url": sSource,
                "data":{
                    "departure": depStation,
                    "arrived": arrStation,
                    "startDate": dateDeparture + "T" + formatTime(val0),
                    "endDate": dateDeparture + "T" + formatTime(val1)
                },
                "success": function(result){
                    fnCallback(result)
                }
            } );
        },
        "columnDefs": [
            {

                // "mData": "number",
                "render": function (data, type, row) {
                    var stations = Object.keys(row.stationSchedules);
                    return row.number;
                },
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
                "render": function (data, type, row) {
                    var places;
                    $.ajax( {
                        "dataType": 'json',
                        "type": "GET",
                        "async": false,
                        "url": "ticket/" + row.number,
                        "success": function(result){
                            places = row.places - result;
                        }
                    } );

                    return places;
                },
                "targets": 3
            },
            {
                "mData": "id",
                "render": function (data, type, row) {
                    return "<button class='btn btn-primary' onclick ='buyTicketToTrain(this)'>Buy ticket</button>"
                },
                "targets": 4
            }
        ]
    });
}

function validateTrainDataData(depStation, arrStation, date) {
    var exceptionMessage = "";
    var reqExpForStation = new RegExp("^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+$");
    if (!reqExpForStation.test(depStation)) {
        $("#depStation").addClass("errorField");
        exceptionMessage += "Departure station;";
    }
    if (!reqExpForStation.test(arrStation)) {
        $("#arrStation").addClass("errorField");
        exceptionMessage += "Arrival station;";
    }
    if (!isDateValid(date)) {
        $("#dateDeparture").addClass("errorField");
        exceptionMessage += "Date";
    }
    if (exceptionMessage != "") {
        showErrorWindow("Incorrect format of next field: " + exceptionMessage);
        return false;
    }
    return true;
}

function isDateValid(date) {
    var reqExpForDate = new RegExp("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$");
    return reqExpForDate.test(date)
}

function slideTime(event, ui){
    var val0 = $("#time-ranger").slider("values", 0),
        val1 = $("#time-ranger").slider("values", 1),
        minutes0 = parseInt(val0 % 60, 10),
        hours0 = parseInt(val0 / 60 % 24, 10),
        minutes1 = parseInt(val1 % 60, 10),
        hours1 = parseInt(val1 / 60 % 24, 10);
    startTime = getTime(hours0, minutes0);
    endTime = getTime(hours1, minutes1);
    $("#slider-time").html(startTime + ' - ' + endTime);
}
function getTime(hours, minutes) {
    if (hours.toString().length == 1) {
        hours = "0" + hours;
    }
    if (minutes.toString().length == 1) {
        minutes = "0" + minutes;
    }
    return hours + ":" + minutes;
}
slideTime();

$(document).ready(function () {
        hideModalWindow("errorAlertDiv");
        $("#time-range").hide();
        $("#dateDeparture").change(function () {
            if(isDateValid($("#dateDeparture").val())) {
                $("#slider-time").html("10:00 - 12:00");
                $("#time-range").show();
                $("#time-ranger").slider({
                    range: true,
                    min: 0,
                    max: 1439,
                    step: 1,
                    values: [600, 720],
                    slide: slideTime
                });
            }
            else {
                $("#time-ranger").html("");
                $("#slider-time").html("10:00 AM - 12:00 PM");
                $("#time-range").hide();
            }
        });
        removeClassFromElementOnChange("depStation", "errorField");
        removeClassFromElementOnChange("arrStation", "errorField");
        removeClassFromElementOnChange("dateDeparture", "birthDate");

        var stations = [];
        $.ajax({
            url: "station/",
            dataType: 'json',
            async: false,
            contentType: "application/json",
            method: 'GET',
            success: function(data) {
                $.map(data, function (value, key) {
                    stations.push({"label": value.name, "value": value.name});
                });
            }
        });

        $("#depStation").autocomplete({
            source: stations
        });
        $("#arrStation").autocomplete({
            source: stations
        })
    }
);
