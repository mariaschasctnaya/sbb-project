function showErrorWindow(errorMessage) {
    hideModalWindow("successAlertDiv");
    $("#errorMessage").text(errorMessage);
    showModalWindow("errorAlertDiv");
    openModal();
}

function hideModalWindow(windowId) {
    $("#" + windowId).hide()
}

function showModalWindow(windowId) {
    $("#" + windowId).show();
}

function removeClassFromElementOnChange(elementId, className) {
    $("#" + elementId).change(function () {
        $("#" + elementId).removeClass(className);
    });
}

function openModal(modalId) {
    $('#'+modalId).modal('show')
}

function closeModal(modalId) {
    $('#'+modalId).modal('hide')
}
