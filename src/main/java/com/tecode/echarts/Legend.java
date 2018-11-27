package com.tecode.echarts;

import com.tecode.echarts.enums.*;
import com.tecode.echarts.style.TextStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 图例
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public class Legend {
    /**
     * 朝向
     */
    private Orient orient = Orient.horizontal;

    /**
     * 设置分页方式
     */
    private LegendType type = LegendType.plain;

    /**
     * 图例图形宽度
     */
    private Integer itemWidth = 25;
    /**
     * 图例图形高度
     */
    private Integer itemHeight = 14;
    /**
     * 文字样式
     */
    private TextStyle textStyle;
    /**
     * 图例内容数组，每一项代表一个系列的name。
     */
    private List<String> data = new ArrayList<>();

    /**
     * 图例标记和文本的对齐
     */
    private Align align = Align.auto;

    /**
     * 用来格式化图例文本
     */
    private String formatter;

    public Legend() {
        textStyle = new TextStyle();
        textStyle.setAlign(X.center).setColor("#333").setVerticalAlign(Baseline.top)
                .setFontStyle(FontStyle.normal).setFontSize(12).setFontFamily("sans-serif").setFontWeight("normal");
    }

    public Orient getOrient() {
        return orient;
    }

    public Legend setOrient(Orient orient) {
        this.orient = orient;
        return this;
    }

    public LegendType getType() {
        return type;
    }

    public Legend setType(LegendType type) {
        this.type = type;
        return this;
    }

    public Integer getItemWidth() {
        return itemWidth;
    }

    public Legend setItemWidth(Integer itemWidth) {
        this.itemWidth = itemWidth;
        return this;
    }

    public Integer getItemHeight() {
        return itemHeight;
    }

    public Legend setItemHeight(Integer itemHeight) {
        this.itemHeight = itemHeight;
        return this;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public Legend setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    public List<String> getData() {
        return data;
    }

    public Legend setData(List<String> data) {
        this.data = data;
        return this;
    }

    public Align getAlign() {
        return align;
    }

    public Legend setAlign(Align align) {
        this.align = align;
        return this;
    }

    public String getFormatter() {
        return formatter;
    }

    public Legend setFormatter(String formatter) {
        this.formatter = formatter;
        return this;
    }

    public Legend addData(String d) {
        this.data.add(d);
        return this;
    }
}
