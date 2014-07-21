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

    /* filter page */
    $(".pagination span").click(function(){
        var page=$(this).data('page')
        $("#J_currentPage").val(page)
        $("#J_filterForm").submit()
    })


        $(".form_datetime").datetimepicker({
            format: "yyyy-MM-dd HH:mm:ss",
            autoclose: true,
            todayBtn: true,
            minuteStep: 10
        });
    $('.datetimepicker').datetimepicker({
        lang:'ch',
        format:'Y-m-d H:i'

    });


    });


