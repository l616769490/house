(function($) {

    var loadMenu = {
        loadFromJson: function(menuObj) {
            // 添加年份选择
            let years = menuObj.year;
            $('#year').empty();
            $('#year').append(years[0]);
            $('#year-menu').empty();
            for (let i = 0; i < years.length; i++) {
                $('<li><a onclick="onclickYear($(this).text())">' + years[i] + '</a></li>').appendTo($('#year-menu'));
            }


            // 清除以前的菜单数据
            $('#left-menu').empty();

            // 加载菜单
            for (let i = 0; i < menuObj.group.length; i++) {
                let group = menuObj.group[i];
                let groupLi = $('<li></li>').appendTo($('#left-menu'));
                // 组名
                let groupName = $('<a href="#group' + i + '" class="active-menu" data-toggle="collapse"></a>')
                    .append($('<i class="fa fa-bar-chart-o"></i>'))
                    .append(group.name)
                    .append($('<span class="fa arrow"></span>'))
                    .appendTo(groupLi);
                // 报表列表
                let groupUl = $('<ul id="group' + i + '" class="nav nav-second-level"></ul>')
                    .appendTo(groupLi);
                for (let j = 0; j < group.report.length; j++) {
                    let report = group.report[j];
                    let reportName = report.name;
                    let reportURL = report.url;
                    let report_a_id = reportURL.substring(1);
                    let reportLi = $('<li></li>').append($('<a id="' + report_a_id + '" onclick="toReport(\'' + report_a_id + '\',\'' + reportName + '\')"></a>').append(reportName));
                    groupUl.append(reportLi);
                }
            }
            $('#left-menu').metisMenu();

            $('.dropdown-toggle').dropdown();
        }
    }

    $(document).ready(function() {
        // let menuStr = '{"year":[2011,2013],"group":[{"name":"基础分析","report":[{"name":"空置状态","url":"/basic-vacancy"},{"name":"公平市场租金","url":"/rent"},{"name":"市场价","url":"/zxl_value"}]},{"name":"城市规模","report":[{"name":"独栋建筑比例","url":"/city-singleBuilding"},{"name":"房产税","url":"/city-houseDuty"},{"name":"家庭人数","url":"/zxl_person"},{"name":"家庭收入","url":"/zxl_income"}]},{"name":"户主年龄","report":[{"name":"房间数统计","url":"/roomsAnalysisByAge"},{"name":"水电费统计","url":"/utilityAnalysisByAge"}]}]}';
        // let menuObj = JSON.parse(menuStr);
        $.ajax({
            url: "/menu",
            type: "POST",
            success: function (result) {
                loadMenu.loadFromJson(result);
            }
        });
    });
}(jQuery));