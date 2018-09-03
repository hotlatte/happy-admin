<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${pageContext.request.contextPath}" var="base"/>
<html>
<head>
    <title>文章管理</title>
    <style>
        table {
            border-collapse: collapse;
            table-layout: fixed;
            width: 100%;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 5px;
            font-size: 14px;
        }

        thead tr {
            background: #069;
            color: #fff;
        }

        tbody tr:nth-child(2n) {
            background-color: #eee;
        }

        tbody tr:hover {
            background-color: #ffc;
        }

        .pages {
            text-align: center;
            padding: 20px;
        }

        .pages a {
            padding: 5px 10px;
            border: 1px solid #069;
            color: #069;
            text-decoration: none;
        }

        .pages em {
            padding: 5px 8px;
        }

        img {
            max-width: 100px;
        }

        input[type=text] {
            padding: 3px 5px;
        }

        textarea {
            padding: 5px;
        }

        .form-table th {
            background-color: #eee;
            width: 130px;
        }

    </style>
</head>
<body>
<form method="get" id="searchForm">
    <table class="form-table">
        <tbody>
        <tr>
            <th>类型</th>
            <td>
                <label><input type="radio" name="type" value="" ${empty pas.type ? 'checked' : ''}> 全部</label>
                <label><input type="radio" name="type" value="0" ${pas.type eq 0 ? 'checked' : ''}> 广告</label>
                <label><input type="radio" name="type" value="1" ${pas.type eq 1 ? 'checked' : ''}> 段子</label>
                <label><input type="radio" name="type" value="2" ${pas.type eq 2 ? 'checked' : ''}> 趣图</label>
                <label><input type="radio" name="type" value="3" ${pas.type eq 3 ? 'checked' : ''}> 视频</label>
            </td>
            <th>状态</th>
            <td>
                <label><input type="radio" name="status" value="" ${empty pas.status ? 'checked' : ''}> 全部</label>
                <label><input type="radio" name="status" value="0" ${pas.status eq 0 ? 'checked' : ''}> 审核中</label>
                <label><input type="radio" name="status" value="1" ${pas.status eq 1 ? 'checked' : ''}> 审核通过</label>
                <label><input type="radio" name="status" value="2" ${pas.status eq 2 ? 'checked' : ''}> 审核拒绝</label>
            </td>
            <th>内容</th>
            <td><input name="content" type="text" value="<c:out value="${pas.content}"/>"/></td>
            <td>
                <button id="search">查询</button>
            </td>
        </tr>
        </tbody>
    </table>
</form>

<div style="text-align: right;padding:10px 20px;">
    <button id="remove">删除</button>
    <button id="pass">通过</button>
    <button id="notpass">不通过</button>
</div>
<table>
    <colgroup>
        <col width="50">
        <col width="70">
        <col width="">
        <col width="100">
        <col width="120">
        <col width="70">
        <col width="70">
        <col width="160">
        <col width="160">
        <col width="160">
    </colgroup>
    <thead>
    <tr>
        <th>
            <input type="checkbox" onclick="$('[name=id]').prop('checked',this.checked);">
        </th>
        <th>类型</th>
        <th>内容</th>
        <th>来源</th>
        <th>图片/视频</th>
        <th>mark</th>
        <th>状态</th>
        <th>更新时间</th>
        <th>采集时间</th>
        <th>操作</th>
    </tr>
    </thead>
    <c:forEach items="${page}" var="article">
        <tr>
            <td style="text-align: center;">
                <input type="checkbox" value="${article.id}" name="id">
            </td>
            <td style="text-align: center;">
                <c:if test="${article.type eq 0}">广告</c:if>
                <c:if test="${article.type eq 1}">段子</c:if>
                <c:if test="${article.type eq 2}">趣图</c:if>
                <c:if test="${article.type eq 3}">视频</c:if>
            </td>
            <td>${article.content}</td>
            <td>${article.source}</td>
            <td>
                <c:if test="${article.type eq 2 or article.type eq 3}">
                    <a href="${article.type eq 3 ? article.url : article.image}" target="_blank">
                        <img src="${article.image}"/>
                    </a>
                </c:if>
            </td>
            <td>${article.mark}</td>
            <td>
                <c:if test="${article.status eq 0}">审核中</c:if>
                <c:if test="${article.status eq 1}">通过</c:if>
                <c:if test="${article.status eq 2}">拒绝</c:if>
            </td>
            <td class="update-time">${article.updateTime}</td>
            <td class="create-time">${article.createTime}</td>
            <td>
                <c:if test="${article.status eq 0}">
                    <a href="pass.do?id=${article.id}&amp;pass=true" class="pass">通过</a>
                    <a href="pass.do?id=${article.id}&amp;pass=false" class="pass">拒绝</a>
                </c:if>
                <a href="get.do?id=${article.id}" class="edit">修改</a>
            </td>
        </tr>
    </c:forEach>
</table>
<div class="pages">
    ${pageInfo.pageHtml}
</div>

<form method="post" action="save.do" enctype="multipart/form-data" id="add_form">
    <table>
        <caption style="border:1px solid #ccc;text-align:left;padding:5px;">添加</caption>
        <tr>
            <th>类型</th>
            <td>
                <label><input type="radio" name="type" value="0"> 广告</label>
                <label><input type="radio" name="type" value="1" checked> 段子</label>
                <label><input type="radio" name="type" value="2"> 趣图</label>
                <label><input type="radio" name="type" value="3"> 视频</label>
            </td>
            <th>来源</th>
            <td>
                <input type="text" name="source">
            </td>
            <th>mark</th>
            <td>
                <input type="text" name="mark">
            </td>
        </tr>
        <tr>
            <th>图片</th>
            <td colspan="5">
                <input type="text" name="image" style="width:100%;" placeholder="图片地址">
                <input type="file" name="imageFile">
            </td>
        </tr>
        <tr>
            <th>url</th>
            <td colspan="5">
                <input type="text" name="url" style="width:100%;" placeholder="地址/视频地址">
            </td>
        </tr>
        <tr>
            <th>内容</th>
            <td colspan="5">
                <textarea style="width:100%;min-height:200px;" name="content"></textarea>
            </td>
        </tr>
        <tr>
            <th colspan="6">
                <input type="hidden" name="id">
                <button type="button" id="add_btn">提交</button>
                <button type="reset" id="reset_btn">重置</button>
            </th>
        </tr>
    </table>
</form>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<script src="${base}/js/jquery.form.js"></script>
<script>
    function formatNumber(n) {
        return n < 10 ? "0" + n : "" + n;
    }

    function formatDate(date) {
        var y = date.getFullYear();
        var M = date.getMonth() + 1;
        var d = date.getDate();
        var H = date.getHours();
        var m = date.getMinutes();
        var s = date.getSeconds();
        return [y, M, d].map(formatNumber).join('/') + ' ' + [H, m, s].map(formatNumber).join(':');
    }

    var items = document.getElementsByClassName("update-time");
    var len = items.length;
    for (var i = 0; i < len; i++) {
        var item = items.item(i);
        var date = new Date(parseInt(item.innerHTML * 1000));
        item.innerHTML = formatDate(date);
    }

    var items = document.getElementsByClassName("create-time");
    var len = items.length;
    for (var i = 0; i < len; i++) {
        var item = items.item(i);
        var date = new Date(parseInt(item.innerHTML));
        item.innerHTML = formatDate(date);
    }
    $("#searchForm [name=type], #searchForm [name=status]").change(function () {
        $("#search").trigger('click');
    });
    $("#remove").click(function () {
        var ids = $("[name=id]:checked");
        if (ids.length) {
            if (confirm("确定删除？")) {
                var id = [];
                ids.each(function () {
                    id.push($(this).val());
                });
                var href = "remove.do?id=" + id.join("&id=");

                $.post(href, function (data) {
                    if (data === "true") {
                        alert("删除成功");
                        location.reload();
                    }
                });
            }
        }
    });
    $("#pass").click(function () {
        var ids = $("[name=id]:checked");
        if (ids.length) {
            if (confirm("确定通过？")) {
                var id = [];
                ids.each(function () {
                    id.push($(this).val());
                });
                var href = "pass.do?pass=true&id=" + id.join("&id=");

                $.post(href, function (data) {
                    if (data === "true") {
                        alert("操作成功");
                        location.reload();
                    }
                });
            }
        }
    });
    $("#notpass").click(function () {
        var ids = $("[name=id]:checked");
        if (ids.length) {
            if (confirm("确定不通过？")) {
                var id = [];
                ids.each(function () {
                    id.push($(this).val());
                });
                var href = "pass.do?pass=false&id=" + id.join("&id=");

                $.post(href, function (data) {
                    if (data === "true") {
                        alert("操作成功");
                        location.reload();
                    }
                });
            }
        }
    });
    $(".pass").click(function () {
        var href = $(this).attr("href");
        if (confirm("确定吗？")) {
            $.post(href, function (data) {
                if (data === "true") {
                    alert("操作成功");
                    location.reload();
                }
            });
        }
        return false;
    });
    $(".edit").click(function () {
        var href = $(this).attr("href");
        $.get(href, function (article) {
            $('#add_form [name="type"][value=' + article.type + ']').prop('checked', true);
            $('[name="source"]').val(article.source);
            $('[name="mark"]').val(article.mark);
            $('[name="image"]').val(article.image);
            $('[name="url"]').val(article.url);
            $('#add_form [name="content"]').val(article.content).focus();
            $('[name="id"]').val(article.id);
        });
        return false;
    });
    $("#add_btn").click(function () {
        var type = $('[name="article.type"]:checked').val();
        if (type == 1) {
            if ($('[name="article.content"]').val().length < 10) {
                alert("内容字数太少了");
                return false;
            }
        }
        if (type == 2) {
            if ($('[name="article.image"]').val().length < 10) {
                alert("请填写图片地址");
                return false;
            }
        }
        if (type == 3) {
            if ($('[name="article.url"]').val().length < 10) {
                alert("请填写视频地址");
                return false;
            }
        }
        $("#add_form").ajaxSubmit({
            success: function (data) {
                if (data === "true") {
                    alert("保存成功");
                    location.reload();
                } else {
                    alert(data);
                }
            }
        })
    });
</script>
</body>
</html>
