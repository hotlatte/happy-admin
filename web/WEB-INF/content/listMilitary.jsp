<%--
  Created by IntelliJ IDEA.
  User: Lijing
  Date: 2018/8/28
  Time: 21:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${pageContext.request.contextPath}" var="base"/>
<html>
<head>
    <title>投稿列表</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <!-- bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <style>
        #preview {
            max-width: 90%;
        }
    </style>
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
    <script src="${base}/js/jquery.form.js"></script>
</head>
<body class="container">
<br/>
<h1>投稿列表</h1>
<br/><br/>
<div class="with:80%">
    <table class="table table-hover" name="table">
        <thead>
        <tr>
            <th>序号</th>
            <th>参加编号</th>

            <th>姓名</th>
            <th>学校</th>
            <th>作品标题</th>
            <th>作品图片</th>
            <th>简介</th>
            <th>是否审核</th>
            <th>审核时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <select name="military">
            <c:forEach var="military" items="${military}" varStatus="st">
                <tr>
                    <th scope="row">${(pageInfo.pageNum-1) * pageInfo.pageSize + st.index + 1}</th>
                    <td>${military.number}</td>
                    <td>${military.name}</td>
                    <td>${military.school}</td>
                    <td>${military.photoName}</td>
                    <td>
                        <a href="${military.photoUrl}" target="_blank">
                            <img src="${military.photoUrl}" style="max-height:100px;max-width:100px;"/>
                        </a>
                    </td>
                    <td>${military.profile}</td>
                    <td>${military.isCheck eq '0' ?'未审核':'已审核'}</td>
                    <td><fmt:formatDate value="${military.checkTime}"  pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <c:if test="${military.isCheck eq '0'}">
                            <button id="check" military-id="${military.id}">通过</button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </select>
        </tbody>
    </table>
</div>
<div class="pagination pagination-lg">
    <%--<div class="page">--%>
        ${pageInfo.pageHtml}
    <%--</div>--%>
</div>
<script>
    $("#check").on('click', function () {
        this.disable = true;
        var id = $(this).attr('military-id');
        if (id) {
            if (confirm('确认吗?')) {
                $.post('check.do', {id: id}, function (res) {
                    if (res == 'ok') {
                        alert('成功');
                        location.reload();
                    } else {
                        alert(res);
                    }
                });
            }
        }
    });
</script>
</body>
</html>
