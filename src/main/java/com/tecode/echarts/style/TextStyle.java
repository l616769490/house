package com.tecode.echarts.style;

import com.tecode.echarts.enums.Baseline;
import com.tecode.echarts.enums.FontStyle;
import com.tecode.echarts.enums.X;

/**
 * 文字样式
 * 版本：2018/11/19 V1.0
 * 成员：李晋
 */
public class TextStyle {
    /**
     * 颜色
     */
    private String color;
    /**
     * 水平对齐方式
     */
    private X align;
    /**
     * 垂直对齐方式
     */
    private Baseline verticalAlign;
    /**
     * 字号 ，单位px
     */
    private Integer fontSize;
    /**
     * [ default: 'sans-serif' ]
     * 主标题文字的字体系列
     *
     * 还可以是 'serif' , 'monospace', 'Arial', 'Courier New', 'Microsoft YaHei', ...
     */
    private String fontFamily;
    /**
     * 样式
     */
    private FontStyle fontStyle;
    /**
     * 主标题文字字体的粗细
     * <p>
     * 可选：
     * 'normal'
     * 'bold'
     * 'bolder'
     * 'lighter'
     * 100 | 200 | 300 | 400...
     */
    private String fontWeight;

    public String getColor() {
        return color;
    }

    public TextStyle setColor(String color) {
        this.color = color;
        return this;
    }

    public X getAlign() {
        return align;
    }

    public TextStyle setAlign(X align) {
        this.align = align;
        return this;
    }

    public Baseline getVerticalAlign() {
        return verticalAlign;
    }

    public TextStyle setVerticalAlign(Baseline verticalAlign) {
        this.verticalAlign = verticalAlign;
        return this;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public TextStyle setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public TextStyle setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    public FontStyle getFontStyle() {
        return fontStyle;
    }

    public TextStyle setFontStyle(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public TextStyle setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
        return this;
    }
}
