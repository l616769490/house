package com.tecode.house.zxl.server.impl;


import com.tecode.house.zxl.dao.HbaseDao;
import com.tecode.house.zxl.dao.impl.HbaseDaoImpl;
import com.tecode.table.*;
import com.tecode.house.zxl.dao.MySQLDao;
import com.tecode.house.zxl.dao.impl.MySQLDaoImpl;
import com.tecode.house.zxl.server.MaketPriceServer;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;

@Service
public class ServerImpl implements MaketPriceServer {


    private MySQLDao sd = new MySQLDaoImpl();

    private HbaseDao hd = new HbaseDaoImpl();

    /**
     * 向mysql里插入统计结果
     *
     * @param year
     * @return
     */
    @Override
    public boolean intoMysql(String year) {

        boolean value = intoValue(year);

        boolean personValue = intoPerson(year);

        boolean cityValue = intoIncome(year);

        if (value && personValue && cityValue) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 向mysql中插入统计家庭人数的结果
     *
     * @param year
     * @return
     */
    private boolean intoPerson(String year) {
        Map<String, Object> personDistribution = hd.getPersonDistribution(year);
        Iterator<Tuple2<String, Object>> pIt = personDistribution.iterator();

        return sd.into("家庭人数", "单项查询", "http://166.166.1.10/person", "家庭", "人数", "家庭人数", pIt, "统计家庭人数", year);
    }

    /**
     * 向mysql中插入统计市场价的结果
     *
     * @param year
     * @return
     */
    private boolean intoValue(String year) {
        Map<String, Object> vmap = hd.getValueDistribution(year);
        Iterator<Tuple2<String, Object>> vIt = vmap.iterator();
        return sd.into("市场价", "单项查询", "http://166.166.1.10/value", "市场价", "价格", "市场价", vIt, "统计价格区间", year);
    }

    /**
     * 向mysql中插入统计家庭收入的结果
     *
     * @param year
     * @return
     */
    private boolean intoIncome(String year) {
        Map<String, Object> incomeDistributionByCity = hd.getIncomeDistributionByCity(year);
        Iterator<Tuple2<String, Object>> cIt = incomeDistributionByCity.iterator();
        return sd.into("家庭收入", "多项查询", "http://166.166.1.10/income", "城市", "收入", "年收入", cIt, "家庭的年收入", year);
    }

    /**
     * 获取市场价的表格
     *
     * @param year 要查询的年份
     * @return 表格结果
     */
    @Override
    public Table getValueTable(int year) {

        Table table = new Table();
        table.setYear(year).setPage(new Page().setThisPage(1));
        table.addTop("房屋ID").addTop("城市").addTop("市场价");

        Page p = new Page();

        Map<String, Iterable<Tuple3<String, Object, Object>>> data = hd.getValue(String.valueOf(year));
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

        String year = tablePost.getYear().toString();
        String search = tablePost.getSearches().get(1).getTitle();
        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("市场价");
        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));

        Map<String, Iterable<Tuple3<String, Object, Object>>> valueData = hd.getValue(year, search);

        setTable(valueData, table, p);

        return table;
    }

    @Override
    public java.util.Map<String, Integer> getMaket() {
        return sd.get();

    }

    /**
     * 获取家庭人数的表格数据
     *
     * @param year 查询的年份
     * @return 表格数据
     */
    @Override
    public Table getPersonTable(int year) {
        Table table = new Table();
        table.setYear(year).setPage(new Page().setThisPage(1));
        table.addTop("房屋ID").addTop("城市").addTop("家庭人数");

        Page p = new Page();

        Map<String, Iterable<Tuple3<String, Object, Object>>> data = hd.getPerson(String.valueOf(year));

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

        String year = tablePost.getYear().toString();

        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("家庭人数");
        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));
        java.util.List<Search> searches = tablePost.getSearches();
        String search = "";

        Map<String, Iterable<Tuple3<String, Object, Object>>> valueData = null;
        if (searches.size() == 1) {
            search = searches.get(0).getTitle();
            if (!search.contains("人")) {
                valueData = hd.getPerson(year, Integer.valueOf(search));
                setTable(valueData, table, p);
            } else {
                valueData = hd.getPerson(year, search);
                setTable(valueData, table, p);
            }
        } else if (searches.size() == 2) {
            valueData = hd.getPerson(year, searches.get(0).getTitle(), searches.get(1).getTitle());
            setTable(valueData, table, p, searches.get(1).getTitle());
        }


        return table;
    }


    /**
     *
     * @param year
     * @return
     */
    @Override
    public Table getIncomeTable(int year) {
        Table table = new Table();
        table.setYear(year).setPage(new Page().setThisPage(1));
        table.addTop("房屋ID").addTop("城市").addTop("家庭收入");

        Page p = new Page();

        Map<String, Iterable<Tuple3<String, Object, Object>>> data = hd.getIncome(String.valueOf(year));

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

        String year = tablePost.getYear().toString();

        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("家庭收入");
        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));
        java.util.List<Search> searches = tablePost.getSearches();
        String search = "";

        Map<String, Iterable<Tuple3<String, Object, Object>>> valueData = null;

        if (searches.size() == 1) {
            search = searches.get(0).getTitle();
            if (!search.contains("万")) {
                valueData = hd.getIncome(year, Integer.valueOf(search));
                setTable(valueData, table, p);
            } else {
                valueData = hd.getIncome(year, search);
                setTable(valueData, table, p);
            }
        } else if (searches.size() == 2) {
            valueData = hd.getIncome(year, searches.get(0).getTitle(), searches.get(1).getTitle());
            setTable(valueData, table, p, searches.get(1).getTitle());
        }


        return table;
    }

    @Override
    public java.util.Map<String, Integer> getincome() {
        return sd.getIncome();
    }

    @Override
    public java.util.Map<String, Integer> getPerson() {
        return sd.getPerson();
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
            String name = next._1;
            table.addSearchs(new Search().setTitle(name));
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

    private void setTable(Map<String, Iterable<Tuple3<String, Object, Object>>> valueData, Table table, Page p, String city) {
        Iterator<Tuple2<String, Iterable<Tuple3<String, Object, Object>>>> iterator = valueData.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, Iterable<Tuple3<String, Object, Object>>> next = iterator.next();
            String name = next._1;
            table.addSearchs(new Search().setTitle(name));
            table.addSearchs(new Search().setTitle(city));
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

}
