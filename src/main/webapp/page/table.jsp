<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/12/7
  Time: 10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>表格显示页面</title>
    <meta charset="UTF-8"> <!-- for HTML5 -->

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%
        pageContext.setAttribute("APP_PATH", request.getContextPath());
    %>

    <script type="text/javascript"
            src="${APP_PATH}/js/jquery-1.12.4.min.js"></script>
    <link href="${APP_PATH}/css/my.amin.css" rel="stylesheet"/>
    <link href="${APP_PATH}/bootstrap-3.3.7-dist/css/bootstrap.min.css"
          rel="stylesheet"/>
    <script src="${APP_PATH}/bootstrap-3.3.7-dist/js/bootstrap.min.js">

    </script>

    <!-- 引入 ECharts 文件 -->
    <script src="${APP_PATH}/js/echarts.js">

    </script>
</head>
<body>
<div class="row">
    <div id="showTable" style="width: 800px;height: 600px" class="col-lg-12 col-md-offset-2">

    </div>

</div>
<div class="row">
    <div id="btns" class="col-lg-8 col-md-offset-2">
        <button id="btn-1" class="btn btn-success" onclick="getTable1()">基础-房间数分析</button>
        <button id="btn-2" class="btn btn-success col-md-offset-2" onclick="getTable2()">按区域-家庭收入分析
        </button>
        <button id="btn-3" class="btn btn-success col-md-offset-2" onclick="getTable3()">按区域-房产税分析
        </button>
    </div>
</div>
<script>
    function getTable1() {
        $.ajax({
            url: "/basics_rooms_num_table",
            type: "POST",
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({
                'year': 2013,
                'page': 2,
                'searches': [{'title': '房间数', 'values': ['1']}, {'title': '卧室数', 'values': ['1']}]
            }),
            success: function (result) {
                showTable(result);
            }
        });
    };

    function getTable2() {
        $.ajax({
            url: "/region_zinc2_num_table",
            type: "POST",
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({
                'year': 2013,
                'page': 2,
                'searches': [
                    {'title': '1', 'values': ['L30']},
                    {'title': '2', 'values': ['L30']},
                    {'title': '3', 'values': ['L30']},
                    {'title': '4', 'values': ['L30']}]
            }),
            success: function (result) {
                showTable(result);
            }
        });
    };

    function getTable3() {
        $.ajax({
            url: "/region_zsmhc_num_table",
            type: "POST",
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({
                'year': 2013,
                'page': 2,
                'searches': [
                    {'title': '1', 'values': ['1500-3000']},
                    {'title': '2', 'values': ['1500-3000']},
                    {'title': '3', 'values': ['1500-3000']},
                    {'title': '4', 'values': ['1500-3000']}]
            }),
            success: function (result) {
                showTable(result);
            }
        });
    };

    function showTable(result) {

    };
</script>
</body>
</html>
