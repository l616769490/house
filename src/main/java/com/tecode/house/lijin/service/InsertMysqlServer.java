package com.tecode.house.lijin.service;

import com.tecode.echarts.Option;
import com.tecode.mysql.bean.*;
import com.tecode.mysql.dao.*;
import com.tecode.table.Table;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import sun.text.resources.cldr.ii.FormatData_ii;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 存储数据到数据库
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public abstract class InsertMysqlServer {
    /**
     * 数据源
     */
    protected SqlSession session = null;

    public InsertMysqlServer(String path) {
        String resource = path;
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据写入到数据库中
     *
     * @param datas 数据集
     * @param year  数据所属年份
     */
    public abstract void insert(Map<String, Map<String, String>> datas, int year);

    /**
     * 提交并关闭session
     */
    public void close() {
        session.commit();
        session.close();
    }

    /**
     * 查询报表表是否存在，存在则获取，不存在则创建
     *
     * @param year       年份
     * @param reportName 报表名
     * @param groupName  报表组
     * @param url 请求地址
     * @return 报表
     */
    protected Report getReport(int year, String reportName, String groupName, String url) {
        ReportMapper reportMapper = session.getMapper(ReportMapper.class);
        Report report = reportMapper.selectByNameAndYear(reportName, year);
        if (report == null) {
            report = new Report();
            report.setCreate(System.currentTimeMillis());
            report.setGroup(groupName);
            report.setName(reportName);
            report.setUrl(url);
            report.setStatus(0);
            report.setYear(year);
            // 写入报表数据
            reportMapper.insert(report);
        }
        return report;
    }


    /**
     * 查询图表表是否存在，存在则获取，不存在则创建
     *
     * @param report      报表
     * @param diagramName 图表名
     * @param subName     子标题
     * @return 图表
     */
    protected List<Diagram> getDiagrams(Report report, String[] diagramName, String[] subName) {
        if (diagramName.length != subName.length) {
            throw new IllegalArgumentException("标题数目和子标题数目不相同！");
        }
        DiagramMapper diagramMapper = session.getMapper(DiagramMapper.class);
        List<Diagram> diagrams = diagramMapper.selectByReportKey(report.getId());

        if (diagrams == null || diagrams.size() == 0) {
            for (int i = 0; i < diagramName.length; i++) {
                // 不存在则创建图表
                Diagram diagram = new Diagram();
                diagram.setName(diagramName[i]);
                diagram.setReportid(report.getId());
                diagram.setSubtext(subName[i]);
                diagram.setType(1);
                diagrams = new ArrayList<>();
                diagrams.add(diagram);
                diagramMapper.insert(diagram);
            }
        }
        return diagrams;
    }

    /**
     * 获取x轴，不存在则创建
     *
     * @param diagram       图表
     * @param xName         x轴名字
     * @param xDimGroupName 维度组名
     * @return x轴集
     */
    protected List<XAxis> getXAxes(Diagram diagram, String[] xName, String[] xDimGroupName) {
        if (xName.length != xDimGroupName.length) {
            throw new IllegalArgumentException("x轴和维度数目不相同！");
        }
        XAxisMapper mapper = session.getMapper(XAxisMapper.class);
        XAxisExample xAxisExample = new XAxisExample();
        xAxisExample.or().andDiagramidEqualTo(diagram.getId());
        List<XAxis> xAxes = mapper.selectByExample(xAxisExample);
        if (xAxes == null || xAxes.size() == 0) {
            for (int i = 0; i < xName.length; i++) {
                XAxis xAxis = new XAxis();
                xAxis.setName(xName[i]);
                xAxis.setDiagramid(diagram.getId());
                xAxis.setDimgroupname(xDimGroupName[i]);
                xAxes = new ArrayList<>();
                xAxes.add(xAxis);
                mapper.insert(xAxis);
            }
        }
        return xAxes;
    }

    /**
     * 获取y轴，不存在则创建
     *
     * @param diagram 图表
     * @param yName   Y轴名字
     * @return Y轴
     */
    protected List<YAxis> getYAxes(Diagram diagram, String[] yName) {
        YAxisMapper mapper = session.getMapper(YAxisMapper.class);
        YAxisExample yAxisExample = new YAxisExample();
        yAxisExample.or().andDiagramidEqualTo(diagram.getId());
        List<YAxis> yAxes = mapper.selectByExample(yAxisExample);
        if (yAxes == null || yAxes.size() == 0) {
            for (int i = 0; i < yName.length; i++) {
                YAxis yAxis = new YAxis();
                yAxis.setDiagramid(diagram.getId());
                yAxis.setName(yName[i]);
                yAxes = new ArrayList<>();
                yAxes.add(yAxis);
                mapper.insert(yAxis);
            }
        }
        return yAxes;
    }

    /**
     * 获取图例,不存在则创建
     *
     * @param diagram    图表
     * @param legendName 图例名
     * @param groupName  维度组名
     * @return 图例
     */
    protected List<Legend> getLefends(Diagram diagram, String[] legendName, String[] groupName) {
        if (legendName.length != groupName.length) {
            throw new IllegalArgumentException("图例和维度数目不相同！");
        }

        LegendMapper mapper = session.getMapper(LegendMapper.class);
        LegendExample legendExample = new LegendExample();
        legendExample.or().andDiagramidEqualTo(diagram.getId());
        List<Legend> legends = mapper.selectByExample(legendExample);
        if (legends == null || legends.size() == 0) {
            for (int i = 0; i < legendName.length; i++) {
                Legend legend = new Legend();
                legend.setDiagramid(diagram.getId());
                legend.setDimgroupname(groupName[i]);
                legend.setName(legendName[i]);
                legends.add(legend);
                mapper.insert(legend);
            }
        }
        return legends;
    }

    /**
     * 获取维度
     *
     * @param groupName 维度组名
     * @return 维度
     */
    protected List<Dimension> getDimensions(String groupName) {
        DimensionMapper mapper = session.getMapper(DimensionMapper.class);
        DimensionExample dimensionExample = new DimensionExample();
        dimensionExample.or().andGroupnameEqualTo(groupName);
        return mapper.selectByExample(dimensionExample);
    }

    /**
     * 组装Data
     *
     * @param report 报表
     * @param datas  数据，数据类型为List<Map<String, String>>，list为图例维度，Map为x轴维度
     * @param xAxis  x轴
     * @param legend 图例
     * @return 数据集
     */
    protected List<Data> getDatas(Report report, Map<String, Map<String, String>> datas, XAxis xAxis, Legend legend) {
//        System.out.println("维度：");
        // x轴维度组
        List<Dimension> xDimensions = getDimensions(xAxis.getDimgroupname());
//        System.out.println(xDimensions);

        // 图例维度组
        List<Dimension> legendDimensions = getDimensions(legend.getDimgroupname());
//        System.out.println(legendDimensions);


        if (datas.size() != legendDimensions.size()) {
            throw new IllegalArgumentException("数据和图例数目不相同！");
        }

        List<Data> dataList = new ArrayList<>();
        for (int i = 0; i < legendDimensions.size(); i++) {
            // 获取图例维度
            String lengendDimname = legendDimensions.get(i).getDimname();
            // 对应的数据集
            Map<String, String> map = datas.get(lengendDimname);

            for (int j = 0; j < xDimensions.size(); j++) {
                String xDimname = xDimensions.get(j).getDimname();
                // 取出数据
                String value = map.get(xDimname);
                // 当前数据不存在则插入0值
                if (value == null) {
                    value = "0";
                }
                Data data = new Data();
                data.setLegendid(legend.getId());
                data.setLegend(lengendDimname);
                data.setXid(xAxis.getId());
                data.setX(xDimname);
                data.setValue(value);
                dataList.add(data);
                if (j == 0) {
                    insertSearch(report.getId(), legendDimensions.get(i).getDimname(), xDimensions.get(j).getGroupname());
                }
            }
        }

        return dataList;
    }

    /**
     * 存储数据集
     *
     * @param dataList 数据集
     */
    protected void insertDataList(List<Data> dataList) {
        DataMapper mapper = session.getMapper(DataMapper.class);
        for (Data data : dataList) {
            DataExample dataExample = new DataExample();
            dataExample.or().andXidEqualTo(data.getXid()).andLegendidEqualTo(data.getLegendid()).andXEqualTo(data.getX()).andLegendEqualTo(data.getLegend());
            // 如果数据已经存在先将其删除
            List<Data> dataList1 = mapper.selectByExample(dataExample);
            if (dataList1 != null && dataList1.size() != 0) {
                mapper.deleteByExample(dataExample);
            }
            mapper.insert(data);
        }
    }

    /**
     * 写入搜索
     *
     * @param id        报表ID
     * @param dimname   搜索名（单个图例）
     * @param groupname 搜索条件（该图例对应的x轴维度组）
     */
    protected void insertSearch(Integer id, String dimname, String groupname) {
        SearchMapper mapper = session.getMapper(SearchMapper.class);
        Search search = new Search();
        search.setName(dimname);
        search.setDimgroupname(groupname);
        search.setReportid(id);
        mapper.insert(search);
//        System.out.println("搜索：");
//        System.out.println(search);
    }

    /**
     * 将报表状态更新为可用
     *
     * @param report 报表
     */
    protected void reStatus(Report report) {
        report.setStatus(1);
        ReportMapper mapper = session.getMapper(ReportMapper.class);
        ReportExample reportExample = new ReportExample();
        reportExample.or().andIdEqualTo(report.getId());
        mapper.updateByExample(report, reportExample);
    }
}
