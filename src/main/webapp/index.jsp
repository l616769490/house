<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/16
  Time: 10:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>主页</title>
    <%
        pageContext.setAttribute("APP_PATH", request.getContextPath());
    %>

    <script type="text/javascript"
            src="${APP_PATH}/js/jquery-1.12.4.min.js"></script>

    <link href="${APP_PATH}/bootstrap-3.3.7-dist/css/bootstrap.min.css"
          rel="stylesheet"/>
    <script src="${APP_PATH}/bootstrap-3.3.7-dist/js/bootstrap.min.js">

    </script>

    <!-- 引入 ECharts 文件 -->
    <script src="${APP_PATH}/js/echarts.js">

    </script>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div id="show" style="width: 600px;height: 400px" class="col-lg-12 col-md-offset-2">

        </div>

    </div>
    <div class="row">
        <div id="btns" class="col-lg-6 col-md-offset-2">
            <button id="btn-1" class="btn btn-success" onclick="show('/zxl-control')">导入数据</button>
            <button id="btn-7" class="btn btn-success" onclick="show('/zxl-pie2')">家庭人数</button>
            <button id="btn-2" class="btn btn-success col-md-offset-1" onclick="show('/zxl-bar')">按城市统计收入</button>
            <button id="btn-9" class="btn btn-success col-md-offset-1" onclick="show('/zxl-incometable')">家庭收入表</button>
            <button id="btn-3" class="btn btn-success col-md-offset-1" onclick="show('/zxl-pie')">市场价</button>
            <button id="btn-6" class="btn btn-success col-md-offset-1" onclick="show('http://166.166.0.5/test-pie')">空置状态</button>
            <button id="btn-4" class="btn btn-success col-md-offset-1" onclick="getTable()">表格测试</button>
            <button id="btn-5" class="btn btn-success col-md-offset-1" onclick="toFile()">文件上传</button>
        </div>
    </div>
</div>


<script type="text/javascript">

    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('show'));

    function show(show_url) {
        $.ajax({
            url: show_url,
            type: "POST",
            data:"year=2011",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(result);
            }
        });
    };

    function getTable() {
        $.ajax({
            url: "/test-table",
            type: "POST",
            data: '{"year":2011,"page":5,"searches":[{"title":"按城市等级","values":["一线城市"]},{"title":"按收入范围","values":["0-10000"]}]}',
            success: function (result) {
                alert(result);
            }
        });
    };

    function toFile() {
        window.location.href = "/file-page"
    }
</script>
</body>
</html>
