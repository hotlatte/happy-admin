<%--
  Created by IntelliJ IDEA.
  User: Lijing
  Date: 2018/8/28
  Time: 21:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="base"/>
<html>
<head>
    <title>我的大学我的青春投稿</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <style>
        #preview {
            max-width: 90%;
        }
    </style>
</head>
<body>
<form enctype="multipart/form-data" action="contributionAdd.do" method="post" id="form">
    <p>
        姓名:<input type="text" name="name" id="name">
    </p>
    <p>
        手机号:<input type="number" name="phone" id="phone">
    </p>
    <p>
        学校:<input type="text" name="school" id="school">
    </p>
    <p>
        作品名:<input type="text" name="photoName" id="photoName">
    </p>
    <p>
        简介:<input type="text" name="profile" id="profile">
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
            alert("请上传不超过5M的图片");
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
        formData.append("name",$('#name').val());
        formData.append("phone",$('#phone').val());
        formData.append("school",$('#school').val());
        formData.append("photoName",$('#photoName').val());
        formData.append("profile",$('#profile').val());

        xhr.open("Post", "${base}/contribution.do");
        xhr.send(formData);

        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    var response = JSON.parse(xhr.responseText);
                    alert(response.Data)
                    if (response.success) {
                        alert('投稿成功，可以关闭页面！请勿重复提交！');
                    } else {
                        alert(response.ErrorMessage);
                    }
                }
                if (xhr.status != 200 || xhr.response.Success == false) {
                    alert("图片上传失败，请检查文件格式和文件大小，请上传小于5M的图片！");
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
