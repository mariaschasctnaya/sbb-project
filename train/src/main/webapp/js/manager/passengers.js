var trainId;

function loadPassengerTable(id) {
    openPage('/passengers')
    $.cookie("trainIdPass", id);
}

function loadPassengers(trainId) {
    $.ajax({
        url: "passenger",
        async: false,
        method: 'GET',
        contentType: "application/json",
        data: {
            "train": trainId
        },
        success: function(data) {
            var passengerList = "";
            data.forEach(function (passenger){
                passengerList += "<tr><td>"+passenger.name+"</td><td>" + passenger.surname + "</td><td>" + passenger.birthday +"</td></tr>"
            });
            $('#tablePassengers').html(passengerList);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            showErrorWindow("Unable buy ticket due: " + jqXHR.responseJSON.message);
        }
    });
}