<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="base"/>
<html>
<head>
    <title>图片上传</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <style>
        #preview {
            max-width: 100%;
        }
    </style>
</head>
<body>
<form enctype="multipart/form-data" action="upload.do" method="post" id="form" >
    <p>
        name:<input type="text" name="brand" id="brand">
    </p>
    <p>
        age:<input type="text" name="country" id="country">
    </p>
    <p>
        图片: <input type="file" name="image" id="image">
        <img id="preview"/>
    </p>
    <input type="button" id="button1" value="提交">
</form>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<script src="${base}/js/jquery.form.js"></script>
<script>
    /*$("input[type='file']").change(function () {
        if ($(this).get(0).files.length == 0) return;
        var This = $(this);
        var file = $(this).get(0).files[0];
        if ([".bmp", ".jpg", ".jpeg", ".gif", ".png"].indexOf(file.name.substring(file.name.lastIndexOf(".")).toLowerCase()) == -1) {
            alert("不支持的文件格式,请选择图片文件");
            This.val('');
            return false;
        }
        if (file.size > 2 * 1024 * 1024) {
            alert("请上传不超过2M的图片");
            This.val('');
            return false;
        }
        var filePath = window.URL.createObjectURL(file);
        var xhr = new XMLHttpRequest();
        var formData = new FormData();
        formData.append("file", file);
        xhr.open("Post", "/Grids/ImageUpload");
        xhr.send(formData);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    var response = eval("(" + xhr.responseText + ")");
                    //alert(response.Data)
                    if (response.Success) {
                        This.siblings(".thumbnail").show().attr('src', filePath);

                        This.siblings("input[type='hidden']").val(response.Data);
                        This.siblings("input[type='text']").val(response.Data);
                    } else {
                        alert(response.ErrorMessage);
                        This.val('');
                    }
                }
                if (xhr.status != 200 || xhr.response.Success == false) {
                    alert("图片上传失败，请检查文件格式和文件大小，请上传小于2M的图片！");
                    This.val('');
                }
            }
        }
        xhr.onprogress = function (e) {
            //document.title = e.loaded +"==="+e.total
        }
    });*/
    $("input[type='file']").change(function () {
        if ($(this).get(0).files.length == 0) return;
        var This = $(this);
        var file = $(this).get(0).files[0];
        if ([".bmp", ".jpg", ".jpeg", ".gif", ".png"].indexOf(file.name.substring(file.name.lastIndexOf(".")).toLowerCase()) == -1) {
            alert("不支持的文件格式,请选择图片文件");
            This.val('');
            return false;
        }
        if (file.size > 5 * 1024 * 1024) {
            alert("请上传不超过2M的图片");
            This.val('');
            return false;
        }
        var filePath = window.URL.createObjectURL(file);
        $("#preview")[0].src = filePath;
    });

    $("#button1").on('click', function () {
        var xhr = new XMLHttpRequest();
        var formData = new FormData();

        formData.append("image", $("#image")[0].files[0]);
        formData.append("name",$('#brand').val());
        formData.append("age",$('#county').val());

        xhr.open("Post", "${base}/image/upload.do");
        xhr.send(formData);

        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    var response = JSON.parse(xhr.responseText);
                    //alert(response.Data)
                    if (response.success) {
                        alert('成功');
                    } else {
                        alert(response.ErrorMessage);
                    }
                }
                if (xhr.status != 200 || xhr.response.Success == false) {
                    alert("图片上传失败，请检查文件格式和文件大小，请上传小于2M的图片！");
                }
            }
        };
        xhr.onprogress = function (e) {
            //document.title = e.loaded +"==="+e.total
        };
    });
</script>

<c:if test="${not empty path}">文件上传成功:${path}</c:if>

<c:if test="${not empty errorMsg}">
    <c:forEach items="${errorMsg}" var="msg">
        <li>${msg}</li>
    </c:forEach>
</c:if>
</body>
</html>
