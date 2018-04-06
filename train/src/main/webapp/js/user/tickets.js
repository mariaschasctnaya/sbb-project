function buyTicket() {
    hideModalWindow("successAlertDiv");
    hideModalWindow("errorAlertDiv");
    var user = new User($("#userSurname").val(), $("#userName").val(), $("#birthDate").val());
    var ticket = new Ticket($("#trainId").text(),$("#depStation").text(), user);
    if (validateUserData()) {
        $.ajax({
            url: "ticket/",
            async: false,
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify(ticket),
            success: function(data) {
                $("#successAlertMessage").text("Congratulations!You buy ticket successfully!");
                showModalWindow("successAlertDiv");
            },
            error: function(jqXHR, textStatus, errorThrown) {
                showErrorWindow("Unable buy ticket due: " + jqXHR.responseXML.getElementsByTagName("message")[0].innerHTML);
            }
        });
    }
}

function validateUserData() {
    var userSurname = $("#userSurname").val();
    var userName = $("#userName").val();
    var birthDate = $("#birthDate").val();
    var exceptionMessage = "";
    var reqExpForName = new RegExp("^[a-zA-Z]'?([a-zA-Z]|\\.| |-)+$");
    var reqExpForDate = new RegExp("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$");
    if (!reqExpForName.test(userSurname)) {
        $("#userSurname").addClass("errorField");
        exceptionMessage += "Surname;";
    }
    if (!reqExpForName.test(userName)) {
        $("#userName").addClass("errorField");
        exceptionMessage += "Name;";
    }
    if (!reqExpForDate.test(birthDate)) {
        $("#birthDate").addClass("errorField");
        exceptionMessage += "Birthday";
    }
    if (exceptionMessage != "") {
        showErrorWindow("Incorrect format of next field: " + exceptionMessage);
        return false;
    }
    return true;
}

function loadTrainFromCookie(){
    $("#trainId").text($.cookie('trainId'));
    $("#depStation").text($.cookie('depStation'))
    $("#depTime").text($.cookie('depTime'))
    $("#arrStation").text($.cookie('arrStation'))
    $("#arrTime").text($.cookie('arrTime'))

}


$(document).ready(function () {
        hideModalWindow("successAlertDiv");
        hideModalWindow("errorAlertDiv");
        removeClassFromElementOnChange("userSurname", "errorField");
        removeClassFromElementOnChange("userName", "errorField");
        removeClassFromElementOnChange("userName", "birthDate");
        loadTrainFromCookie();
    }
);