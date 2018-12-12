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
 * 点击了搜索按钮
 */
function searchBtn() {
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
    let thisPage = $('.this-page').text() || 1;

    let tablePost = '{"year": ' + year + ', "page": ' + thisPage;

    // 获取搜索条件
    let selects = $('.btn.btn-success.my-css');
    let labels = $('label.my-css');
    let searches = "";
    for (let i = 0; i < labels.length; i++) {
        let labelValue = $('#' + labels[i].id + '').text();
        let selectValue = $('#' + selects[i].id + '').text();
        if (selectValue == "全部") {
            selectValue = "";
        }
        searches += '{"title": "' + labelValue + '", "values": ["' + selectValue + '"]}';
        if (i != labels.length - 1) {
            searches += ",";
        }
    }

    if (searches != "") {
        tablePost += ',"searches": [' + searches + ']';
    }

    tablePost += "}";

    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        contentType: 'application/json',
        data: tablePost,
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
    let tableDiv = $('#house-table-div');
    tableDiv.empty();
    showSearch(result);
    showHousetable(result);
    showHousePage(result);
}

/**
 * 显示搜索栏
 * @param result Table对象
 */
function showSearch(result) {
    let searchList = result.search;
    let tableDiv = $('#house-table-div');

    for (let i = 0; i < searchList.length; i++) {
        let searchItem = searchList[i];
        // 搜索面板
        let searchDiv = $('<div class="col-md-3"></div>');
        tableDiv.append(searchDiv);
        let searchPanel = $('<div class="panel-heading"></div>');
        searchDiv.append(searchPanel);

        // 搜索标题
        let searchLabel = $('<label class="my-css" id="label' + i + '"></label>').append(searchItem.title);
        searchLabel.appendTo(searchPanel);
        // 搜索按钮组
        let btnGroup = $('<div class="btn-group"></div>');
        btnGroup.appendTo(searchPanel);
        btnGroup.append($('<button class="btn btn-success my-css" id="select' + i + '"></button>').append('全部'));
        btnGroup.append($('<button data-toggle="dropdown" class="btn btn-success dropdown-toggle"><span class="caret"></span></button>'));
        let searchUl = $('<ul class="dropdown-menu"></ul>');
        searchUl.appendTo(btnGroup);
        // 搜索值
        let searchData = searchItem.values;
        for (let j = 0; j < searchData.length; j++) {
            searchUl.append($('<li><a onclick="showSelect(\'select' + i + '\', $(this).text())">' + searchData[j] + '</a></li>'));
        }
    }

    // 添加一个搜索按钮
    let searchBtn = $('<div class="col-md-2"></div>').append($('<div class="panel-heading"></div>'));
    searchBtn.append($('<button class="btn btn-success" onclick="searchBtn()">检索</button>'));
    tableDiv.append(searchBtn);
}

/**
 * 显示表格数据
 * @param result
 */
function showHousetable(result) {
    let topData = result.top;
    let tableData = result.data;
    let tableDiv = $('#house-table-div');
    let panelBody = $('<div class="panel-body"></div>');
    tableDiv.append(panelBody);
    let tableResponsive = $('<div class="table-responsive"></div>');
    panelBody.append(tableResponsive);
    let tableView = $('<table class="table table-striped table-bordered table-hover"></table>');
    tableResponsive.append(tableView);

    // 添加表头
    let tHead = $('<thead></thead>');
    tableView.append(tHead);
    let tHeadTr = $('<tr></tr>');
    tHead.append(tHeadTr);
    for (let i = 0; i < topData.length; i++) {
        tHeadTr.append($('<th>' + topData[i] + '</th>'));
    }

    // 添加数据
    let tBody = $('<tbody></tbody>');
    tableView.append(tBody);
    for (let i = 0; i < tableData.length; i++) {
        let rowData = tableData[i].row;
        let tBodyTr = $('<tr></tr>');
        tBody.append(tBodyTr);
        for (let j = 0; j < rowData.length; j++) {
            tBodyTr.append($('<td>' + rowData[j] + '</td>'));
        }
    }
}

/**
 * 显示分页
 * @param result
 */
function showHousePage(result) {
    let tableDiv = $('#house-table-div');
    let pageData = result.page;
    $("#house_page ul").empty();

    let pageDiv = $('<div class="panel-body" id="house_page"></div>');
    tableDiv.append(pageDiv);
    let pageUl = $('<ul class="pagination"></ul>');
    pageDiv.append(pageUl);

    // 首页
    let firstPageLi = $("<li></li>").append($("<a onclick='onclickPage($(this))'></a>").append("首页").attr("href", "#"));
    // 上一页
    let prePageLi = $("<li></li>").append($("<a onclick='onclickPage($(this))'></a>").append("&laquo;"));
    if (pageData.thisPage == "1") {
        firstPageLi.addClass("disabled");
        prePageLi.addClass("disabled");
    }

    // 添加首页和前一页
    pageUl.append(firstPageLi).append(prePageLi);
    // 遍历给ul中添加页码提示
    $.each(result.page.data, function (index, item) {
        let numLi = $("<li id='page"+index+"'></li>");
        let numA = $('<a onclick=\'onclickPage($(this))\'></a>').append(item);
        numLi.append(numA);
        if(item == pageData.thisPage) {
            numLi.addClass('active');
            numA.addClass('this-page');
        }
        pageUl.append(numLi);
    });

    // 下一页
    let nextPageLi = $("<li></li>").append($("<a onclick='onclickPage($(this))'></a>").append("&raquo;"));
    //添加下一页
    pageUl.append(nextPageLi);
}

/**
 * 点击了页码
 * @param page  点击的页码
 */
function onclickPage(page) {
    let pageText = page.text();
    let thisPage = page;
    if(pageText == '首页') {
        thisPage = $('#page1');
    } else if(pageText == "&laquo;") {

    } else if (pageText == "&raquo;") {

    }

    $('.this-page').removeClass('this-page');
    let numLi = thisPage.parent();
    console.log(numLi);
    numLi.addClass('active');
    thisPage.addClass('this-page');

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
 * [显示图表]
 * @param  option [图表json]
 */
function showChart(option) {
    myChart.setOption(option, true);
}