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

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
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
<div class="container-fluid">
    <div class="row">
        <div id="btns" class="col-lg-8 col-md-offset-2">
            <button id="btn-1" class="btn btn-success" onclick="getTable1()">空置状态</button>
            <button id="btn-2" class="btn btn-success col-md-offset-2" onclick="getTable2()">按城市-独栋建筑
            </button>
            <button id="btn-3" class="btn btn-success col-md-offset-2" onclick="getTable3()">按城市-房产税
            </button>
        </div>
    </div>
    <div class="row" id="house_search">
        <select id="basic" class="col-lg-1 col-md-offset-1">
            <option>cow</option>
            <option>bull1</option>
            <option>bull2</option>
            <option>bull3</option>
        </select>
        <select id="basic2" class="col-lg-1 col-md-offset-1">
            <option>cow</option>
            <option>bull1</option>
            <option>bull2</option>
            <option>bull3</option>
        </select>
    </div>

    <!-- 显示表格数据 -->
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="house_table">
                <thead>
                <tr>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>

    <!-- 显示分页信息 -->
    <div class="row" id="house_page">
        <ul class="pagination">
        </ul>
    </div>


</div>
<script>
    function getTable1() {
        $.ajax({
            url: "/basic_vacancy_table",
            type: "POST",
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({
                'year': 2013,
                'page': 2,
                'searches': [{'title': '空置状态', 'values': ['空置','居住','全部']}]
            }),
            success: function (result) {
                showTable(result);
            }
        });
    }

    function getTable2() {
        $.ajax({
            url: "/city_singleBuilding_table",
            type: "POST",
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({
                'year': 2013,
                'page': 4,
                'searches': [
                    {'title': '城市规模', 'values': ['全部','一线城市','二线城市','三线城市','四线城市','五线城市']},
                    {'title': '建筑结构类型', 'values': ['独栋','其他','全部']}]
            }),
            success: function (result) {
                showTable(result);
            }
        });
    }

    function getTable3() {
        $.ajax({
            url: "/city_houseDuty_table",
            type: "POST",
            dataType: "json",
            contentType: 'application/json',
            data: JSON.stringify({
                'year': 2013,
                'page': 5,
                'searches': [
                    {'title': '城市规模', 'values': ['全部','一线城市','二线城市','三线城市','四线城市','五线城市']},
                    {'title': '建筑结构类型', 'values': ['独栋','其他','全部']}]
            }),
            success: function (result) {
                showTable(result);
            }
        });
    }

    function showTable(result) {
        // 表格数据
        build_house_table(result);
        // 分页
        build_house_page_info(result);
        // 搜索
        build_house_select(result);
    }

    // 搜索框
    function build_house_select(result) {
        $("#house_search").empty();

        // 所有搜索条件
        let searchs = result.search;
        for (let i = 0; i < searchs.length; i++) {
            let title = searchs[i].title;
            $("#house_search").append($("<label></label>").append(title)).addClass("col-lg-1  col-md-offset-1");
            let values = searchs[i].values;
            let search = $("<select></select>").addClass("col-lg-1 col-md-offset-2");
            for (let j = 0; j < values.length; j++) {
                search.append($("<option></option>").append(values[j]));
            }
            $("#house_search").append(search)
        }
    }

    // 显示表格数据
    function build_house_table(result) {
        // 添加表头
        $("#house_table thead tr").empty();
        let tops = result.top;
        $.each(tops, function (index, top) {
            $("<th></th>").append(top).appendTo("#house_table thead tr");
        });

        // 添加表格数据
        $("#house_table tbody").empty();
        let dataValue = result.data;

        //遍历data
        for (let i = 0; i < dataValue.length; i++) {
            let rowArray = dataValue[i].row;
            let rowTr = $("<tr></tr>");
            for (let j = 0; j < rowArray.length; j++) {
                $("<td></td>").append(rowArray[j]).appendTo(rowTr);
            }
            rowTr.appendTo("#house_table tbody");
        }
    }

    // 显示分页数据
    function build_house_page_info(result) {
        $("#page_info_area").empty();
        $("#page_info_area").append("当前第" + result.page.thisPage + "页");
        build_house_page_nav(result);
    }

    //解析显示分页条
    function build_house_page_nav(result) {
        $("#house_page ul").empty();
        // 首页
        let firstPageLi = $("<li></li>").append($("<a></a>").append("首页").attr("href", "#"));
        // 上一页
        let prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
        if (result.page.thisPage == "1") {
            firstPageLi.addClass("disabled");
            prePageLi.addClass("disabled");
        }

        // 添加首页和前一页
        $("#house_page ul").append(firstPageLi).append(prePageLi);
        // 遍历给ul中添加页码提示
        $.each(result.page.data, function (index, item) {

            let numLi = $("<li></li>").append($("<a></a>").append(item));
            $("#house_page ul").append(numLi);
        });

        // 下一页
        let nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
        //添加下一页
        $("#house_page ul").append(nextPageLi);
    }


</script>
</body>
</html>
