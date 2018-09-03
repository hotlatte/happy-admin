<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="base"/>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui">
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
</head>
<body>
<!-- 导航 -->
<div id="app">
    <header><img src="img/hd.jpg" alt=""></header>
    <nav>
        <router-link  to="/">活动说明</router-link>
        <router-link  to="/upload">参加活动</router-link>
    </nav>
    <router-view></router-view>
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
            <p>3、奖项设置<br>&nbsp;&nbsp;&nbsp;&nbsp;一等奖 3名<br>&nbsp;&nbsp;&nbsp;&nbsp;二等奖6名<br>&nbsp;&nbsp;&nbsp;&nbsp;三等奖10名<br>&nbsp;&nbsp;&nbsp;&nbsp;优秀奖20名</p>
            <p>4、最终解释权归芜湖新媒体中心所有。</p>
        </div>
    </div>
</script>
<!-- vote -->
<script id="vote" type="text/x-template">
    <div class="wrap">
        <div class="input-group">
            <input type="text" class="form-control" placeholder="搜索id">
            <span class="input-group-btn">
            <button class="btn btn-default" type="button"><i class="glyphicon glyphicon-search"></i></button>
          </span>
        </div>
        <div class="grid">
            <div class="grid-item">
                <div class="pic">
                    <p class="counter">票数: 999</p>
                    <img src="img/0.jpg">
                </div>
                <div class="content">
                    <b>ID:001</b>
                    <h4>《标题》</h4>
                    <p>大鼻子章鱼，容易愤怒且势利眼，相当自恋，自以为拥有艺术才能。目是光头，但曾有过黄色长卷发，并希望头发再长回来。</p>
                    <button class="btn">投TA一票</button>
                </div>
            </div>
            <div class="grid-item">
                <div class="pic">
                    <p class="counter">票数: 999</p>
                    <img src="img/1.jpg">
                </div>
                <div class="content">
                    <b>ID:002</b>
                    <h4>《很长很长很长的标题》</h4>
                    <p>红色螃蟹，本名蟹阿金，在海军服役的外号为“铁甲金钟罩”。视金钱如生命，经常为了一块钱而去冒生命危险，极为自私。快餐店"蟹堡王餐厅的经营者，多次向员工宣导要有时间观念，因为"时间就是金钱"。</p>
                    <button class="btn">投TA一票</button>
                </div>
            </div>
            <div class="grid-item">
                <div class="pic">
                    <p class="counter">票数: 999</p>
                    <img src="img/2.jpg">
                </div>
                <div class="content">
                    <b>ID:003</b>
                    <h4>《一小段标题》</h4>
                    <p>来自德克萨斯州的雌性松鼠，身兼科学家、探险家和发明家。热爱运动（尤其空手道和滑沙）和科学，也是牛仔竞技的冠军。由于松鼠属於陆地生物，需要呼吸空气，在海底须穿著太空衣和头盔。</p>
                    <button class="btn">投TA一票</button>
                </div>
            </div>
            <div class="grid-item">
                <div class="pic">
                    <p class="counter">票数: 999</p>
                    <img src="img/0.jpg">
                </div>
                <div class="content">
                    <b>ID:004</b>
                    <h4>《我也是标题》</h4>
                    <p>红色螃蟹，本名蟹阿金，在海军服役的外号为“铁甲金钟罩”。视金钱如生命，经常为了一块钱而去冒生命危险，极为自私。快餐店"蟹堡王餐厅的经营者，多次向员工宣导要有时间观念，因为"时间就是金钱"。</p>
                    <button class="btn">投TA一票</button>
                </div>
            </div>
        </div>
    </div>
</script>

<!-- router -->
<script src="https://cdn.bootcss.com/vue-router/2.7.0/vue-router.min.js"></script>
<script src="https://unpkg.com/imagesloaded@4/imagesloaded.pkgd.min.js"></script>
<script src="https://unpkg.com/masonry-layout@4.2.0/dist/masonry.pkgd.min.js"></script>
<script src="js/global.min.js"></script>

</body>
</html>
