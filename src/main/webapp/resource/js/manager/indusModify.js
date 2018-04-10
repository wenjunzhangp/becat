layui.use(['form','layer','layedit','upload'],function(){
    var form = layui.form
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,layedit = layui.layedit,upload = layui.upload;

    $.ajax({
        url : "/console/initIndusAllCategory.shtml",
        type : "get",
        cache:true,
        dataType : "json",
        success : function(data){
            if(data.status==200){
                var categoryHtml = '<option value="-1">全部</option>';
                $.each(data.data,function(i,v){
                    categoryHtml += '<option value="'+v.id+'">'+v.title+'</option>';
                });
                $("#category").append(categoryHtml);
                form.render('select');
            }else{
                layer.msg(data.msg);
            }
        }
    })

    layedit.set({
        uploadImage: {
            url: '/console/uploadUserFaceImg.shtml',
            type: 'post'
        }
    });
    layedit.build('content'),{
        width:600,
        height: 180
    };

    var uploadInst = upload.render({
        elem: '.userFaceBtn',
        url: '/console/uploadUserFaceImg.shtml',
        method : "post",
        ext: 'jpg|png|gif',
        before: function(obj){
            obj.preview(function(index, file, result){
                $('#userFace').attr('src', result);
            });
        },
        done: function(res, index, upload){
            if (res.status==200) {
                layer.msg("缩略图上传成功!");
                $('#userFace').attr('src',res.data);
            } else {
                layer.msg("缩略图上传失败请稍后再试!");
            }
        },
        error: function(){
            var tryagain = $('#tryagain');
            tryagain.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
            tryagain.find('.demo-reload').on('click', function(){
                uploadInst.upload();
            });
        }
    });

    form.on("submit(modifyIndus)",function(data){
        var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            top.layer.close(index);
            top.layer.msg("操作成功！");
            layer.closeAll("iframe");
            //刷新父页面
            parent.location.reload();
        },2000);
        return false;
    })

    //格式化时间
    function filterTime(val){
        if(val < 10){
            return "0" + val;
        }else{
            return val;
        }
    }
    //定时发布
    var time = new Date();
    var submitTime = time.getFullYear()+'-'+filterTime(time.getMonth()+1)+'-'+filterTime(time.getDate())+' '+filterTime(time.getHours())+':'+filterTime(time.getMinutes())+':'+filterTime(time.getSeconds());

})