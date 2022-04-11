function clearFilter(){
    window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName){
    entityID = link.attr("entityID");

    $("#yesButton").attr("href", link.attr("href"));
    $("#confirmText").text("Are you sure you want delete this " + entityName +" ID " + entityID + " ?");
    $("#comfirmModal").modal();
}