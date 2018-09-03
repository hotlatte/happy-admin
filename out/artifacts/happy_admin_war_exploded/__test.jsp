<%--
  Created by IntelliJ IDEA.
  User: sylar
  Date: 2017-12-28
  Time: 22:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
    <style>
        table {
            border-collapse: collapse;
            table-layout: fixed;
            width: 720px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 5px;
        }

        th {
            width: 130px;
        }
    </style>
</head>
<body>
<table id="testTable">
    <tr>
        <th>mod</th>
        <td>
            <input id="mod" name="mod"/>
        </td>
    </tr>
    <tr>
        <th>act</th>
        <td>
            <input id="act" name="act"/>
        </td>
    </tr>
    <tr>
        <th>参数</th>
        <td>
            <div class="par">
                key: <input class="key" name="key"/>
                value: <input class="value" name="value"/>
                <a class="add" href="javascript:;">添加</a>
                <a class="del" href="javascript:;">删除</a>
            </div>
        </td>
    </tr>
    <tr>
        <th>结果</th>
        <td>
            <textarea style="width:100%;min-height:100px;"></textarea>
        </td>
    </tr>
    <tr>
        <th></th>
        <td>
            <button type="button" id="test">测试</button>
        </td>
    </tr>
</table>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<script>
    $("#testTable").on("click", ".add", function () {
        var div = $(this).parent();
        div.after(div.clone());
        div.next().find("input").val('');
    }).on('click', ".del", function () {
        var div = $(this).parent();
        if (div.siblings().length > 0) {
            div.remove();
        }
    });
    $("#test").on("click", function () {
        var par = {};
        $(".par").each(function (i) {
            var key = $(this).find(".key").val();
            var value = $(this).find(".value").val();
            par[key] = value;
        });
        $.get("api.do", {
            mod: $("#mod").val(),
            act: $("#act").val(),
            pars: JSON.stringify(par)
        }, function (res) {
            console.log(res);
            $("textarea").val(JSON.stringify(res));
        }, "json");
    });
</script>
</body>
</html>
