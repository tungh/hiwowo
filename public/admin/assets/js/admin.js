$(function() {

    $('#side-menu').metisMenu();
    $(" th input:checkbox").click(function() {
        var checkedStatus = this.checked;
        var checkbox = $(this).parents('.table').find('tr td:first-child input:checkbox');
        checkbox.each(function() {
            this.checked = checkedStatus;
            if (checkedStatus == this.checked) {
                $(this).closest('.checker > span').removeClass('checked');
            }
            if (this.checked) {
                $(this).closest('.checker > span').addClass('checked');
            }
        });
    });


    /* 批量处理*/
    $("#J_batch").click(function(){
        var url =window.location.href
        $("#J_url").val(url)
        $("#J_batchForm").submit()
    })
});


