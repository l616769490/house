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
        <div id="show1" style="display:none;width: 800px;height: 600px" class="col-lg-12 col-md-offset-2">

        </div>
        <div id="show2" style="display:none; width: 800px;height: 600px;float: left" class="col-lg-12 col-md-offset-2">

        </div>

        <div id="show3" style="display:none; width: 400px;height: 550px;float: left" class="col-lg-12 col-md-offset-2">

        </div>
        <div id="show4" style="display:none;  width: 400px;height: 550px;float: left" class="col-lg-12 col-md-offset-2">

        </div>

        <div id="show5" style="display:none; width: 400px;height: 550px;float: left" class="col-lg-12 col-md-offset-2">

        </div>
        <div id="show6" style="display:none; width: 400px;height: 550px;float: left" class="col-lg-12 col-md-offset-2">

        </div>

    </div>
    <div class="row">
        <div id="btns" class="col-lg-6 col-md-offset-2">

            <button id="btn-1" class="btn btn-success col-md-offset-1" onclick="show('/per')">家庭人数图</button>
            <button id="btn-2" class="btn btn-success col-md-offset-1" onclick="show('/rate')">年份房产税图</button>
            <button id="btn-3" class="btn btn-success col-md-offset-1" onclick="show('/single')">区域独栋比例图</button>
            <button id="btn-4" class="btn btn-success col-md-offset-1" onclick="getTable()">表格测试</button>
            <button id="btn-5" class="btn btn-success col-md-offset-1" onclick="toFile()">文件上传</button>
        </div>
    </div>
</div>


<script type="text/javascript">
        //家庭人数
        $("#btn-1").click(function(){
            $("#show1").show();
            $("#show2").hide();
            $("#show3").hide();
            $("#show4").hide();
            $("#show5").hide();
            $("#show6").hide();

        });
    //年份_房产税
    $("#btn-2").click(function(){
        $("#show2").show();
        $("#show1").hide();
        $("#show5").hide();
        $("#show3").hide();
        $("#show4").hide();
        $("#show6").hide();

    });


    //区域_独栋比例
    $("#btn-3").click(function(){
        $("#show1").hide();
        $("#show2").hide();
        $("#show3").show();
        $("#show4").show();
        $("#show5").show();
        $("#show6").show();

    });


    // 基于准备好的dom，初始化echarts实例
        var myChart1 = echarts.init(document.getElementById('show1'));
        var myChart2 = echarts.init(document.getElementById('show2'));
        var myChart3 = echarts.init(document.getElementById('show3'));
        var myChart4 = echarts.init(document.getElementById('show4'));
        var myChart5 = echarts.init(document.getElementById('show5'));
        var myChart6 = echarts.init(document.getElementById('show6'));

    function show(show_url) {
        // 家庭人数
        $.ajax({
            url: "/per",
            type: "POST",
            data:"year=2013",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart1.setOption(result);
            }
        });
        // 年份_房产税
        $.ajax({
            url: "/rate",
            type: "POST",
            data:"year=2013",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart2.setOption(result);
            }
        });
        $.ajax({
            url: "/first",
            type: "POST",
            data:"year=2013",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart3.setOption(result);
            }
        });
        $.ajax({
            url: "/second",
            type: "POST",
            data:"year=2013",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart4.setOption(result);
            }
        });
        $.ajax({
            url: "/third",
            type: "POST",
            data:"year=2013",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart5.setOption(result);
            }
        });
        $.ajax({
            url: "/forth",
            type: "POST",
            data:"year=2013",
            success: function (result) {
                // 使用刚指定的配置项和数据显示图表。
                myChart6.setOption(result);
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
