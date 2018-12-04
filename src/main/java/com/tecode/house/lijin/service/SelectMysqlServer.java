package com.tecode.house.lijin.service;

import com.tecode.echarts.Option;
import com.tecode.mysql.bean.*;
import com.tecode.mysql.dao.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 从数据库中查询数据
 * 版本：2018/12/3 V1.0
 * 成员：李晋
 */
public abstract class SelectMysqlServer {
    /**
     * 数据源
     */
    protected SqlSession session = null;

    public SelectMysqlServer(String path) {
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
     * 读取报表
     *
     * @param year       年份
     * @param reportName 报表名
     * @return Echarts格式的报表
     */
    public abstract Option select(int year, String reportName);

    /**
     * 关闭session
     */
    public void close() {
        session.close();
    }

    /**
     * 查询报表
     *
     * @param year       年份
     * @param reportName 报表名
     * @return 报表
     */
    protected Report getReport(int year, String reportName) {
        ReportMapper reportMapper = session.getMapper(ReportMapper.class);
        return reportMapper.selectByNameAndYear(reportName, year);
    }

    /**
     * 查询图表
     *
     * @param report 报表
     * @return 图表
     */
    protected List<Diagram> getDiagrams(Report report) {
        DiagramMapper diagramMapper = session.getMapper(DiagramMapper.class);
        return diagramMapper.selectByReportKey(report.getId());
    }

    /**
     * 获取x轴，不存在则创建
     *
     * @param diagram 图表
     * @return x轴集
     */
    protected List<XAxis> getXAxes(Diagram diagram) {
        XAxisMapper mapper = session.getMapper(XAxisMapper.class);
        XAxisExample xAxisExample = new XAxisExample();
        xAxisExample.or().andDiagramidEqualTo(diagram.getId());
        return mapper.selectByExample(xAxisExample);
    }

    /**
     * 获取y轴，不存在则创建
     *
     * @param diagram 图表
     * @return Y轴
     */
    protected List<YAxis> getYAxes(Diagram diagram) {
        YAxisMapper mapper = session.getMapper(YAxisMapper.class);
        YAxisExample yAxisExample = new YAxisExample();
        yAxisExample.or().andDiagramidEqualTo(diagram.getId());
        return mapper.selectByExample(yAxisExample);
    }

    /**
     * 获取图例
     *
     * @param diagram 图表
     * @return 图例
     */
    protected List<Legend> getLegends(Diagram diagram) {
        LegendMapper mapper = session.getMapper(LegendMapper.class);
        LegendExample legendExample = new LegendExample();
        legendExample.or().andDiagramidEqualTo(diagram.getId());
        return mapper.selectByExample(legendExample);
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
     * 获取Data
     *
     * @param xAxis  x轴
     * @param legend 图例
     * @return 数据集
     */
    protected Map<String, Map<String, String>> getDatum(XAxis xAxis, Legend legend) {
        // x轴id
        Integer xAxisId = xAxis.getId();
        // 图例id
        Integer legendId = legend.getId();
        DataMapper mapper = session.getMapper(DataMapper.class);
        DataExample dataExample = new DataExample();
        dataExample.or().andXidEqualTo(xAxisId).andLegendidEqualTo(legendId);

        List<Data> dataList = mapper.selectByExample(dataExample);
        Map<String, Map<String, String>> datum = new HashMap<>();
        for (Data data : dataList) {
            // 图例维度
            String l = data.getLegend();
            // x轴维度
            String x = data.getX();
            if (datum.get(l) != null) {
                datum.get(l).put(x, data.getValue());
            } else {
                Map<String, String> map = new LinkedHashMap<>();
                map.put(x, data.getValue());
                datum.put(l, map);
            }
        }
        return datum;
    }

    /**
     * 获取搜索
     *
     * @param report 报表
     * @return 搜索组
     */
    protected List<Search> getSearch(Report report) {
        SearchMapper mapper = session.getMapper(SearchMapper.class);
        SearchExample searchExample = new SearchExample();
        searchExample.or().andReportidEqualTo(report.getId());
        return mapper.selectByExample(searchExample);
    }

    /**
     * 通过ID查询数据库
     *
     * @param id          查询的id
     * @param mapperClass 查询的表对应的Mapper.class
     * @return 查到的数据
     */
    protected Object getById(Integer id, Class<Object> mapperClass) {

        try {
            String beanName = mapperClass.getSimpleName().substring(0, mapperClass.getSimpleName().lastIndexOf("Mapper"));
            // Bean的全类路径
            String beanClassName = "com.tecode.mysql.bean." + beanName;
            // Example的全类路径
            String exampleClassName = "com.tecode.mysql.bean." + beanName + "Example";

            Class beanClass = Class.forName(beanClassName).newInstance().getClass();
            Object mapper = session.getMapper(mapperClass);
            Object example = Class.forName(exampleClassName).newInstance();

            Method or = example.getClass().getMethod("or");
            Object invokeOr = or.invoke(example);
            Method idEqualTo = invokeOr.getClass().getMethod("andIdEqualTo", Integer.class);
            idEqualTo.invoke(invokeOr, id);

            Method selectByExample = mapper.getClass().getMethod("selectByExample", example.getClass());
            return selectByExample.invoke(mapper, example);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        SelectMysqlServer selectMysqlServer = new SelectMysqlServer(ConfigUtil.get("mybatis-config2"));
//        Object byId = selectMysqlServer.getById(1, DimensionMapper.class);
//        System.out.println(byId);
//    }
}
