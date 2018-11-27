package com.tecode.echarts;

import com.tecode.echarts.enums.Baseline;
import com.tecode.echarts.enums.FontStyle;
import com.tecode.echarts.enums.X;
import com.tecode.echarts.style.TextStyle;

/**
 * 表头
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public class Title {

    /**
     * 主标题文本，'\n'换行
     */
    private String text;
    /**
     * 副标题文本，'\n'换行
     */
    private String subtext;

    /**
     * 主标题文本样式
     */
    private TextStyle textStyle;
    /**
     * 副标题文本样式
     */
    private TextStyle subtextStyle;

    public Title() {
        textStyle = new TextStyle();
        textStyle.setAlign(X.center).setColor("#333").setVerticalAlign(Baseline.top)
                .setFontStyle(FontStyle.normal).setFontSize(18).setFontFamily("sans-serif").setFontWeight("normal");
        subtextStyle = new TextStyle();
        subtextStyle.setAlign(X.center).setColor("#aaa").setVerticalAlign(Baseline.top)
                .setFontStyle(FontStyle.normal).setFontSize(12).setFontFamily("sans-serif").setFontWeight("normal");
    }



    public String getText() {
        return text;
    }

    public Title setText(String text) {
        this.text = text;
        return this;
    }

    public String getSubtext() {
        return subtext;
    }

    public Title setSubtext(String subtext) {
        this.subtext = subtext;
        return this;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public Title setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    public TextStyle getSubtextStyle() {
        return subtextStyle;
    }

    public Title setSubtextStyle(TextStyle subtextStyle) {
        this.subtextStyle = subtextStyle;
        return this;
    }
}
