// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('house-chart'));

/**
 * [改变下拉列表显示的文字]
 * @param  selectId [下拉列表的id]
 * @param  text     [需要显示的文字]
 */
function showSelect(selectId, text) {
    $('#' + selectId).empty();
    $('#' + selectId).append(text);
}

/**
 * [点击年份]
 * @param text 年份
 */
function onclickYear(text) {
    // 更改显示的年份
    showSelect("year", text);
    // 获取菜单列表
    let report = $('#left-menu').find('li').find('ul').find('li').find('a');
    // 查找当前选中的列表
    for (let i = 0; i < report.length; i++) {
        let r = report[i];
        if (r.classList.value == "active-menu") {
            let reportUrl = r.id;
            let reportName = r.text;
            toReport(reportUrl, reportName);
        }
    }
}

/**
 * [跳转报表]
 * @param  thisMenuId [当前点击的菜单Id，在此处id前面加上‘/’，就是请求地址]
 * @param  text       [当前菜单显示的内容]
 */
function toReport(thisMenuId, text) {
    // 设置标题
    $('#report-title').empty();
    $('#report-title').append(text);
    updateClass(thisMenuId);
    toChart('/' + thisMenuId);
    toTable('/' + thisMenuId + '_table');
}

/**
 * [更新菜单栏样式]
 * @param  thisMenuId [当前点击的菜单Id，在此处id前面加上‘/’，就是请求地址]
 */
function updateClass(thisMenuId) {
    // 清除上一个选中的菜单
    $('#left-menu').find('li').find('ul').find('li').find('a').removeClass();
    // 当前选择的菜单变色
    $('#' + thisMenuId + '').addClass('active-menu');
}

//
/**
 * [获取图表]
 * @param  url [请求地址]
 */
function toChart(url) {
    // 获取年份
    let year = $('#year').text();
    $.ajax({
        url: url,
        type: "POST",
        data: "year=" + year,
        success: function (result) {
            showChart(result);
        }
    });
}

/**
 * [获取表格]
 * @param  url [请求地址，已经加上了“_table”]
 */
function toTable(url) {
    // 获取年份
    let year = $('#year').text();
    // 获取页码
    let thisPage = $('#thisPage').text() || 1;

    let tablePost = '{"year": '+year+', "page": '+thisPage;

    // 获取搜索条件
    let selects = $('.btn.btn-success.my-css');
    let labels = $('label.my-css');
    let searches = "";
    for (let i = 0; i < labels.length; i++) {
        let labelValue =  $('#'+labels[i].id+'').text();
        let selectValue = $('#'+selects[i].id+'').text();
        if(selectValue == "全部") {
            // selectValue = "";
        }
        searches += '{"title": "' + labelValue + '", "values": ["' + selectValue + '"]}';
        if(i != labels.length - 1) {
            searches += ",";
        }
    }

    if(searches != "") {
        tablePost += ',"searches": [' + searches + ']';
    }

    tablePost += "}";

    $.ajax({
        url: url,
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
}

/**
 * [显示表格]
 * @param result    [服务器传来的数据]
 */
function showTable(result) {
    console.log(result);
}

/**
 * [显示图表]
 * @param  option [图表json]
 */
function showChart(option) {
    myChart.setOption(option, true);
}