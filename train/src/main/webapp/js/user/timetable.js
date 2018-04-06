var Station = {


    init: function () {
        var table = Station.createDataTable();
        table.on('click', 'tr', function () {
            Station.selectedStation = table.api().row($(this)).data();
            StationTimeTable.load(Station.selectedStation.name);
            if ($(this).hasClass("selected")) {
                $(this).toggleClass("selected");
                Station.selectedStation = null;
                return;
            }
            $(".selected").each(function () {
                $(this).removeClass("selected");
            });
            $(this).addClass("selected");
        });

        Station.selectedStation = null;

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
            "sAjaxSource": 'station/',
            "sServerMethod": "GET",
            "sAjaxDataProp" : "",
            "aoColumns": [
                { "mData": "name" }
            ]
        });
    }
};

var StationTimeTable = {
    load: function (station) {
        StationTimeTable.clearDataTable();
        StationTimeTable.createDataTable(station);
    },

    clearDataTable: function() {
        $('#timetable_table').dataTable().fnDestroy();
        $("#timetable_body").html("");
    },

    createDataTable : function (station) {
        return $('#timetable_table').dataTable({
            "sDom": "<'row'<'span4'l><'span4 search_table'f>r>t<'row'<'span4'i><'span4 offset4'p>>",
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sLengthMenu": "_MENU_ records per page"
            },
            "sAjaxSource": "train?q=" + station,
            "sServerMethod": "GET",
            "sAjaxDataProp" : "",
            "columnDefs": [
                {
                    "render": function (data, type, row) {
                        var stations = Object.keys(row.stationSchedules);
                        return   row.number ;
                    },
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
}
