package com.tecode.echarts;

import com.tecode.echarts.enums.Trigger;

/**
 * 提示框组件
 * 详见：http://www.echartsjs.com/option.html#tooltip
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public class Tooltip {
    /**
     * 是否显示提示框浮层，默认显示。只需tooltip触发事件或显示axisPointer而不需要显示内容时可配置该项为false。
     */
    private Boolean showContent  = true;
    /**
     * 触发类型
     */
    private Trigger trigger;

    /**
     * 格式化
     */
    private String formatter;

    public Boolean getShowContent() {
        return showContent;
    }

    public Tooltip setShowContent(Boolean showContent) {
        this.showContent = showContent;
        return this;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Tooltip setTrigger(Trigger trigger) {
        this.trigger = trigger;
        return this;
    }

    public String getFormatter() {
        return formatter;
    }

    /**
     * 字符串模板
     * <p>
     * 模板变量有 {a}, {b}，{c}，{d}，{e}，分别表示系列名，数据名，数据值等。
     * 在 trigger 为 'axis' 的时候，会有多个系列的数据，此时可以通过 {a0}, {a1}, {a2} 这种后面加索引的方式表示系列的索引。
     * 不同图表类型下的 {a}，{b}，{c}，{d} 含义不一样。 其中变量{a}, {b}, {c}, {d}在不同图表类型下代表数据含义为：
     * 折线（区域）图、柱状（条形）图、K线图 : {a}（系列名称），{b}（类目值），{c}（数值）, {d}（无）
     * 散点图（气泡）图 : {a}（系列名称），{b}（数据名称），{c}（数值数组）, {d}（无）
     * 地图 : {a}（系列名称），{b}（区域名称），{c}（合并数值）, {d}（无）
     * 饼图、仪表盘、漏斗图: {a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）
     */
    public Tooltip setFormatter(String formatter) {
        this.formatter = formatter;
        return this;
    }
}
