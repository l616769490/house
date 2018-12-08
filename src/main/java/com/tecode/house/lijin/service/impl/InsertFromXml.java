package com.tecode.house.lijin.service.impl;

import com.tecode.house.lijin.service.InsertMysqlServer;
import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.mysql.bean.*;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 从xml中读取配置，根据配置文件将数据写入数据库
 *
 * @author : 李晋
 * @version ：2018/12/4 V1.0
 */
public class InsertFromXml extends InsertMysqlServer {

    private Document document;

    public InsertFromXml(String resource, String path) {
        super(resource);
        try {
            SAXReader saxReader = new SAXReader();
            // 读取XML文件,获得document对象
            document = saxReader.read(new File(path));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void insert(Map<String, Map<String, String>> datas, int year) {
        // 根节点
        Element root = document.getRootElement();
        String reportName = root.element("name").getText();

        String reportGroup = root.element("group").getText();
        String url = root.element("url").getText();

        String diagramName = root.element("diagrams").element("diagram").attribute("name").getText();
        String subName = root.element("diagrams").element("diagram").attribute("subName").getText();

        String yName = root.element("diagrams").element("diagram").element("yAxes").element("yAxis").attribute("name").getText();
        String xName = root.element("diagrams").element("diagram").element("xAxes").element("xAxis").element("name").getText();
        String xDimGroupName = root.element("diagrams").element("diagram").element("xAxes").element("xAxis").element("dimGroupName").getText();
        String legendName = root.element("diagrams").element("diagram").element("legends").element("legend").element("name").getText();
        String legendDimGroupName = root.element("diagrams").element("diagram").element("legends").element("legend").element("dimGroupName").getText();
        // 报表表
        Report report = getReport(year, reportName, reportGroup, url);
        // 图表表
        List<Diagram> diagrams = getDiagrams(report, new String[]{diagramName}, new String[]{subName});
        // x轴
        List<XAxis> xAxes = getXAxes(diagrams.get(0), new String[]{xName}, new String[]{xDimGroupName});
        // y轴
        List<YAxis> yAxes = getYAxes(diagrams.get(0), new String[]{yName});
        // 图例
        List<Legend> legends = getLefends(diagrams.get(0), new String[]{legendName}, new String[]{legendDimGroupName});
        // 生成数据库能识别的数据集
        List<Data> dataList = getDatas(report, datas, xAxes.get(0), legends.get(0));
        // 写入数据
        insertDataList(dataList);
        // 修改状态
        reStatus(report);
        // 提交并关闭事务
        close();
    }

    public static void main(String[] args) {
        new InsertFromXml(ConfigUtil.get("mybatis-config2"), InsertFromXml.class.getResource("/report/basics-rooms.xml").getPath()).insert(null, 2013);
    }
}
