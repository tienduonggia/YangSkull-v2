//Khi click vào buttonCacel > trở về trang nãy
$(document).ready(function () {
    $("#buttonCancel").on("click", function () {
        window.location = moduleURL;
    });

    $("#fileImage").change(function (){
        var fileSize = this.files[0].size;
        // alert("File size : "+fileSize);
        //1MB
        if(fileSize > 1048576){
            //này là sẽ hiển thị 1 thông báo do html hiện ra
            this.setCustomValidity("You must choose an image less than 1MB!")
            this.reportValidity();
        }else{
            showImageThumbnail(this);
        }

    });
});

//Khi chọn hình => thì hình sẽ đc hiển thị thumbnail
function showImageThumbnail(fileInput){
    //đọc file đầu tiền
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e){
        $("#thumbnail").attr("src",e.target.result);
    };

    reader.readAsDataURL(file);
}

//show dialog
function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

function showWaringDialog(message){
    showModalDialog("Warning", message)
}

function showErrorDialog(message){
    showModalDialog("Error", message)
}

