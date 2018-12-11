package com.tecode.house.azxl.server.impl;


import com.tecode.house.d01.service.Analysis;
import com.tecode.house.azxl.dao.HbaseDao;
import com.tecode.house.azxl.dao.impl.HbaseDaoImpl;
import com.tecode.table.*;
import com.tecode.house.azxl.dao.MySQLDao;
import com.tecode.house.azxl.dao.impl.MySQLDaoImpl;
import com.tecode.house.azxl.server.MaketPriceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.Tuple3;

import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;

@Service
public class ServerImpl implements MaketPriceServer, Analysis {


    @Autowired
    private MySQLDao sd =new MySQLDaoImpl();

    private HbaseDao hd=new HbaseDaoImpl();


    /**
     * 向mysql中插入统计家庭人数的结果
     *
     * @param tableName 要统计的表名
     * @return
     */
    private boolean intoPerson(String tableName) {
        Map<String, Object> personDistribution = hd.getPersonDistribution(tableName);
        Iterator<Tuple2<String, Object>> pIt = personDistribution.iterator();
        String year=tableName.split(":")[1];
        return sd.into("家庭人数", "城市规模", "/zxl_person", "家庭", "人数", "家庭人数", pIt, "统计家庭人数", year);
    }

    /**
     * 向mysql中插入统计市场价的结果
     *
     * @param tableName 要统计的表名
     * @return
     */
    private boolean intoValue(String tableName) {
        Map<String, Object> vmap = hd.getValueDistribution(tableName);
        Iterator<Tuple2<String, Object>> vIt = vmap.iterator();
        String year=tableName.split(":")[1];
        return sd.into("市场价", "基础分析", "/zxl_value", "市场价", "价格", "市场价", vIt, "统计价格区间", year);
    }

    /**
     * 向mysql中插入统计家庭收入的结果
     *
     * @param tableName 要统计的表名
     * @return
     */
    private boolean intoIncome(String tableName) {
        Map<String, Object> incomeDistributionByCity = hd.getIncomeDistributionByCity(tableName);
        Iterator<Tuple2<String, Object>> cIt = incomeDistributionByCity.iterator();
        String year=tableName.split(":")[1];
        return sd.into("家庭收入", "城市规模", "/zxl_income", "城市", "收入", "年收入", cIt, "家庭的年收入", year);
    }

    /**
     * 获取市场价的表格
     *
     * @return 表格结果
     */
    @Override
    public Table getValueTable() {

        Table table = new Table();
        table.setYear(2013).setPage(new Page().setThisPage(1));
        table.addTop("房屋ID").addTop("城市").addTop("市场价");

        Page p = new Page();

        Map<String, Iterable<Tuple3<String, Object, Object>>> data = hd.getValue();
        setTable(data, table, p);
        return table;
    }

    /**
     * 获取市场价的表格，有搜索条件
     *
     * @param tablePost 封装了各种条件的对象
     * @return 符合条件的表格数据
     */
    @Override
    public Table getValueTable(TablePost tablePost) {
        Table table = new Table();

        table.addSearchs(new Search().setTitle("市场价").addValue("50万以下").addValue("50万-100万").addValue("100万-150万").addValue("150万-200万")
                .addValue("200万-250万").addValue("250万以上"));
        String year = tablePost.getYear().toString();
        String search = tablePost.getSearches().get(0).getValues().get(0);
        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("市场价");
        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));

        List<Tuple2<Object, Tuple3<String, Object, Object>>> value = hd.getValue("thads:"+year, search, tablePost.getPage());

        setTable2(value, table, p);

        return table;
    }

    @Override
    public java.util.Map<String, Integer> getMaket(int year) {
        return sd.get(year);

    }

    /**
     * 获取家庭人数的表格数据
     *
     * @return 表格数据
     */
    @Override
    public Table getPersonTable() {
        Table table = new Table();
        table.setYear(2013).setPage(new Page().setThisPage(1));
        table.addTop("房屋ID").addTop("城市").addTop("家庭人数");

        Page p = new Page();

        Map<String, Iterable<Tuple3<String, Object, Object>>> data = hd.getPerson();

        setTable(data, table, p);
        return table;
    }

    /**
     * 获取家庭人数的表格数据
     *
     * @param tablePost 封装了查询信息的对象
     * @return 表格信息
     */
    @Override
    public Table getPersonTable(TablePost tablePost) {
        Table table = new Table();

        table.addSearchs(new Search().setTitle("家庭人数").addValue("1人").addValue("2人").addValue("3人").addValue("4人")
                .addValue("5人").addValue("6人").addValue("6人以上"));
        table.addSearchs(new Search().setTitle("城市").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5"));

        String year = tablePost.getYear().toString();

        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("家庭人数");
        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));
        java.util.List<Search> searches = tablePost.getSearches();
        String search = "";

        Map<String, Iterable<Tuple3<String, Object, Object>>> valueData = null;
        if (searches.size() == 1) {
            search = searches.get(0).getValues().get(0);
            if (!search.contains("人")) {
                valueData = hd.getPerson("thads:"+year, Integer.valueOf(search));
                setTable(valueData, table, p);
            } else {
                valueData = hd.getPerson("thads:"+year, search);
                setTable(valueData, table, p);
            }
        } else if (searches.size() == 2) {
            List<Tuple2<Object, Tuple3<String, Object, Object>>> person = hd.getPerson("thads:"+year, searches.get(0).getValues().get(0), searches.get(1).getValues().get(0), tablePost.getPage());
            setTable2(person, table, p);
        }


        return table;
    }


    /**
     *
     * @return
     */
    @Override
    public Table getIncomeTable() {
        Table table = new Table();
        table.setYear(2013).setPage(new Page().setThisPage(1));
        table.addTop("房屋ID").addTop("城市").addTop("家庭收入");

        Page p = new Page();

        Map<String, Iterable<Tuple3<String, Object, Object>>> data = hd.getIncome();

        setTable(data, table, p);
        return table;
    }

    /**
     * 获得家庭收入的表格
     * @param tablePost 封装了查询参数的对象
     * @return 查询结果
     */
    @Override
    public Table getIncomeTable(TablePost tablePost) {
        Table table = new Table();

        table.addSearchs(new Search().setTitle("家庭收入").addValue("5万以下").addValue("5万-10万").addValue("10万-15万").addValue("15-20万")
        .addValue("20万-25万").addValue("25万以上"));
        table.addSearchs(new Search().setTitle("城市").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5"));

        String year = tablePost.getYear().toString();

        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("家庭收入");
        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));
        java.util.List<Search> searches = tablePost.getSearches();

        Map<String, Iterable<Tuple3<String, Object, Object>>> valueData = null;

        if (searches.size() == 1) {
            java.util.List<String> values = searches.get(0).getValues();
            for (String value : values) {
                if (!value.contains("万")) {
                    valueData = hd.getIncome(year, Integer.valueOf(value));
                    setTable(valueData, table, p);
                } else {
                    valueData = hd.getIncome(year, value);
                    setTable(valueData, table, p);
                }
            }

        } else if (searches.size() == 2) {
            List<Tuple2<Object, Tuple3<String, Object, Object>>> income = hd.getIncome("thads:"+year, searches.get(0).getValues().get(0), searches.get(1).getValues().get(0), tablePost.getPage());
            setTable2(income, table,p);
        }


        return table;
    }

    @Override
    public java.util.Map<String, Integer> getincome(int year) {
        return sd.getIncome(year);
    }

    @Override
    public java.util.Map<String, Integer> getPerson(int year) {
        return sd.getPerson(year);
    }

    /**
     * 设置table对象的数据
     *
     * @param valueData
     * @param table
     * @param p
     */
    private void setTable(Map<String, Iterable<Tuple3<String, Object, Object>>> valueData, Table table, Page p) {

        Iterator<Tuple2<String, Iterable<Tuple3<String, Object, Object>>>> iterator = valueData.iterator();

        while (iterator.hasNext()) {

            Tuple2<String, Iterable<Tuple3<String, Object, Object>>> next = iterator.next();
            Iterable<Tuple3<String, Object, Object>> it = next._2;
            Iterator<Tuple3<String, Object, Object>> it2 = it.iterator();
            List<Tuple3<String, Object, Object>> tuple3List = it.toList();

            int size = tuple3List.size();
            int page = size / 10 + 1;
            for (int i = 1; i < page; i++) {
                table.setPage(p.addData(i));
            }
            while (it2.hasNext()) {
                Tuple3<String, Object, Object> next1 = it2.next();
                table.addData(new Row().addRow(next1._1()).addRow(next1._2().toString()).addRow(next1._3().toString()));
            }
        }
    }

    private  void  setTable2( List<Tuple2<Object, Tuple3<String, Object, Object>>> income,Table table,Page p){
        Iterator<Tuple2<Object, Tuple3<String, Object, Object>>> iterator = income.iterator();
        int size=0;
        while (iterator.hasNext()){
            Tuple2<Object, Tuple3<String, Object, Object>> next = iterator.next();
            size=Integer.valueOf(next._1.toString());

            Tuple3<String, Object, Object> next1 = next._2;
            table.addData(new Row().addRow(next1._1()).addRow(next1._2().toString()).addRow(next1._3().toString()));

        }
        int page = size / 10 + 1;
        for (int i = 1; i < page; i++) {
            table.setPage(p.addData(i));
        }

    }


    @Override
    public boolean analysis(String tableName) {
        boolean value = intoValue(tableName);

        boolean personValue = intoPerson(tableName);

        boolean cityValue = intoIncome(tableName);

        if (value && personValue && cityValue) {
            return true;
        } else {
            return false;
        }

    }
}
