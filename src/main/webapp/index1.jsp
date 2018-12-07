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
    <style type="text/css">

        #show2{
            position: relative;
            bottom: 570px;
        }
    </style>

    <style type="text/css">

        #show1{
            position: relative;
            bottom: -28px;
            left: 180px;
        }
    </style>

    <style type="text/css">

        #show3{
            position: relative;
            bottom:600px;

        }
    </style>

    <style type="text/css">

        #show4{
            position: relative;
            bottom: 1200px;
            left: 500px;
        }
    </style>




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
        <div id="show1" style="width: 800px;height: 600px" class="col-lg-6">

        </div>

        <div id="show2" style="width: 800px;height: 600px" class="col-lg-6 col-md-offset-6">

            <div class="cent_right">中部居右内容</div>
        </div>

        <div id="show3" style="width: 800px;height: 600px" class="col-lg-12 col-md-offset-2">

        </div>

        <div id="show4" style="width: 800px;height: 600px" class="col-lg-12 col-md-offset-2">

        </div>


    </div>



    <div class="row">
        <div id="btns" class="col-lg-6 col-md-offset-2">
            <button id="btn-1" class="btn btn-success" onclick="show('/cost')">房屋费用</button>
            <button id="btn-2" class="btn btn-success col-md-offset-1" onclick="show('/income')">家庭收入</button>
            <button id="btn-3" class="btn btn-success col-md-offset-1" onclick="show('/price')">饼状图</button>
            <button id="btn-4" class="btn btn-success col-md-offset-1" onclick="getTable()">表格测试</button>
            <button id="btn-5" class="btn btn-success col-md-offset-1" onclick="toFile()">文件上传</button>
        </div>
    </div>
</div>


<script type="text/javascript">

    // 基于准备好的dom，初始化echarts实例
    var myChart1 = echarts.init(document.getElementById('show1'));
    var myChart2 = echarts.init(document.getElementById('show2'));
    var myChart3 = echarts.init(document.getElementById('show3'));
    var myChart4 = echarts.init(document.getElementById('show4'));
    var myChart5 = echarts.init(document.getElementById('show4'));

    function show(show_url) {
        $.ajax({
            url: "/cost",
            type: "POST",
            data:"year=2011",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart1.setOption(result);
            }
        });
        $.ajax({
            url: "/shuidian",
            type: "POST",
            data:"year=2011",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart2.setOption(result);
            }
        });

        $.ajax({
            url: "/other",
            type: "POST",
            data:"year=2011",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart3.setOption(result);
            }
        });


        $.ajax({
            url: "/total",
            type: "POST",
            data:"year=2011",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart4.setOption(result);
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
