<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="base"/>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
    <script src="${base}/js/jquery.form.js"></script>
    <style>
        #preview {
            max-width: 90%;
        }
    </style>
    <!-- bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <!-- custom -->
    <link rel="stylesheet" href="css/style.min.css">

    <!-- vue -->
    <script src="https://cdn.bootcss.com/vue/2.4.2/vue.min.js"></script>
    <title>我的大学，我的青春</title>
    <style>
        .bg-model {
            position: fixed;
            width: 100%;
            height: 100%;
            left: 0;
            top: 0;
            background: #000;
            opacity: 0.7;
            z-index: 190;
            display: none;
        }

        .loading {
            position: fixed;
            width: 100%;
            left: 0;
            height: 50px;
            top: 50%;
            margin-top: -25px;
            z-index: 200;
            color: #fff;
            text-align: center;
            display: none;
        }

        .loading p {
            text-align: center;
        }
    </style>
</head>
<body>
<!-- 导航 -->
<div id="app">
    <header><img src="img/hd.jpg" alt=""></header>
    <nav>
        <router-link to="/">活动说明</router-link>
        <router-link to="/upload">参加活动</router-link>
    </nav>
    <router-view></router-view>
</div>

<div class="bg-model">

</div>
<div class="loading">
    <p>图片上传中，请勿关闭页面...</p>
    <p class="percent">0%</p>
</div>

<!-- index -->
<script id="index" type="text/x-template">
    <div class="wrap intro">
        <!-- <h4>活动主题</h4>
        <p>我的大学，我的青春</p> -->
        <h4><b>活动时间</b><img src="img/tt.png" alt=""></h4>
        <div class="p-box">
            <p class="center">征稿时间：2018年9月10日——10月10日<br>投票时间：2018年10月12日——10月22日<br>颁奖时间：2018年10月25日</p>
        </div>
        <h4><b>作品形式</b><img src="img/tt.png" alt=""></h4>
        <div class="p-box">
            <p>以班级或团体方队为单位，广泛收集高校大一新生军训风采、成果，拍摄下你认为军训最值得纪念的一瞬间，或感动，欢笑，泪水，喜悦，均可，传递青春正能量。</p>
        </div>
        <h4><b>参赛对象</b><img src="img/tt.png" alt=""></h4>
        <div class="p-box">
            <p class="center">芜湖市各大高校学生</p>
        </div>
        <h4><b>作品要求</b><img src="img/tt.png" alt=""></h4>
        <div class="p-box">
            <p>1、积极向上，思想健康，反映芜湖各大高校军训生活的人事物均可。必须是原创作品，涉及他人权利一律取消参赛资格作者投稿时务必附上自己的姓名、学校、联系方式等信息，作者可以给自己的作品命名并注明。</p>
            <p>2、参赛照片格式为JPG，大小不超过5M </p>
            <p>3、允许对照片进行简单的后期处理，但必须保证作品的真实性</p>
            <p>4、可自拍，可合影，可拍好友；可卖萌，可搞笑，可严肃</p>
            <p>5、参赛者须为在校师生及教官参赛照片</p>
            <p>6、上传照片必须经过本人同意</p>
        </div>
        <h4><b>活动流程</b><img src="img/tt.png" alt=""></h4>
        <div class="p-box">
            <p>1、报名方式<br>在“今日芜湖”手机客户端活动页面上传最美的瞬间的军训照片，附上10至50字的说明，并在照片后注明自己的姓名，学校，班级，联系方式。</p>
            <p>2、评奖方式<br>10月22号开始，根据投票排名、活动组委会综合考量，选出最优秀的60张作品在“今日芜湖”手机客户端活动页面上公示，于10月25日进行颁奖。</p>
            <p>3、奖项设置<br>&nbsp;&nbsp;&nbsp;&nbsp;一等奖 3名<br>&nbsp;&nbsp;&nbsp;&nbsp;二等奖6名<br>&nbsp;&nbsp;&nbsp;&nbsp;三等奖10名<br>&nbsp;&nbsp;&nbsp;&nbsp;优秀奖20名
            </p>
            <p>4、最终解释权归芜湖新媒体中心所有。</p>
        </div>
    </div>
</script>
<!-- upload -->
<script id="upload" type="text/x-template">
    <div class="wrap upload">
        <form enctype="multipart/form-data" action="contribution.do" method="post" id="form" name="form">
            <label>作品标题
                <small> / TITLE</small>
            </label>
            <input type="text" name="photoName" id="photoName">
            <label>作者姓名
                <small> / NAME</small>
            </label>
            <input type="text" name="name" id="name">
            <label>学校
                <small> / COLLEGE</small>
            </label>
            <input type="text" name="school" id="school">
            <label>联系电话
                <small> / CONTACT</small>
            </label>
            <input type="text" name="phone" id="phone" value="${user.phone}">
            <input type="file" name="image" id="image" onchange="onFileChange(event,this);" style="display: none;">
            <button class="btn upload-btn" type="button" onclick="image.click();">
                <%--<input type="file" name="file" accept="image/*">--%>
                + 上传图片
            </button>

            <img class="sample" id="preview">
            <label>照片说明（10至50字）
                <small> / DESCIPTION</small>
            </label>
            <textarea name="profile" id="profile" maxlength="50"></textarea>
            <button type="button" class="btn submit-btn" id="button1">提交表单</button>
        </form>


    </div>
</script>

<%--<c:if test="${empty user}">--%>
    <%--<script>--%>
        <%--window.addEventListener('hashchange', function () {--%>
            <%--if (location.hash == '#/upload') {--%>
                <%--location.hash = '#/';--%>
                <%--alert('请在今日芜湖客户端内参加活动');--%>
            <%--}--%>
        <%--});--%>
    <%--</script>--%>
<%--</c:if>--%>


<!-- router -->
<script src="https://cdn.bootcss.com/vue-router/2.7.0/vue-router.min.js"></script>
<script src="https://unpkg.com/imagesloaded@4/imagesloaded.pkgd.min.js"></script>
<script src="https://unpkg.com/masonry-layout@4.2.0/dist/masonry.pkgd.min.js"></script>
<script src="js/global.min.js"></script>

<script>
    function showLoading() {
        $(".bg-model,.loading").show();
    }

    function hideLoading() {
        $(".bg-model,.loading").hide();
    }

    function onFileChange(event, e) {
        var files = e.files;
        if (files.length == 0) return;

        var This = $(e);
        var file = files[0];
        var preview = $('#preview');

        if ([".bmp", ".jpg", ".jpeg", ".gif", ".png"].indexOf(file.name.substring(file.name.lastIndexOf(".")).toLowerCase()) == -1) {
            alert("不支持的文件格式,请选择图片文件");
            This.val('');
            preview.attr('src', '').hide();
            return false;
        }
        if (file.size > 5 * 1024 * 1024) {
            alert("请上传不超过5M的图片");
            This.val('');
            preview.attr('src', '').hide();
            return false;
        }

        var filePath = window.URL.createObjectURL(file);
        preview[0].src = filePath;
        preview.show();
    }


    $(document).on('click', '#button1', function () {

        if ($("#image")[0].files.length == 0) {
            alert('上传图片不能为空！');
            return false;
        }
        if ($("#name").val() == '') {
            alert('作者姓名不能为空！');
            return false;
        }
        if ($("#phone").val() == '') {
            alert('联系电话不能为空！');
            return false;
        }
        if ($("#school").val() == '') {
            alert('学校不能为空！');
            return false;
        }
        if ($("#photoName").val() == '') {
            alert('作品标题不能为空！');
            return false;
        }
        if ($("#profile").val() == '') {
            alert('照片说明不能为空！');
            return false;
        }

        var re = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
        if (!re.test($("#phone").val())) {
            alert('请输入正确的手机号码。');
            return false;
        }


        var xhr = new XMLHttpRequest();
        var formData = new FormData();

        formData.append("image", $("#image")[0].files[0]);
        formData.append("name", $('#name').val());
        formData.append("phone", $('#phone').val());
        formData.append("school", $('#school').val());
        formData.append("photoName", $('#photoName').val());
        formData.append("profile", $('#profile').val());

        xhr.open("Post", "${base}/contribution.do");
        xhr.upload.onprogress = function (e) {
            //document.title = e.loaded +"==="+e.total
            console.log(e.loaded + "===" + e.total);
            $('.percent').html((e.loaded / e.total * 100).toFixed(2) + '%');
        };

        xhr.send(formData);

        showLoading();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    hideLoading();
                    var response = JSON.parse(xhr.responseText);
                    //alert(response.Data)
                    if (response.success) {
                        setTimeout(function () {
                            alert('投稿成功，可以关闭页面！请勿重复提交！');
                            window.location.href = "${base}/index.do";
                        }, 100);
                    } else {
                        alert(response.ErrorMessage);
                    }
                }
                if (xhr.status != 200 || xhr.response.success == false) {
                    hideLoading();
                    alert("图片上传失败，请检查文件格式和文件大小，请上传小于5M的图片！");
                }
            }
        };

    });
</script>
</body>
</html>
