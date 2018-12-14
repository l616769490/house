package com.tecode.house.liaolian.server.impl;


import com.tecode.house.d01.service.Analysis;
import com.tecode.house.liaolian.dao.LLHbaseDao;
import com.tecode.house.liaolian.dao.LLMySQLDao;
import com.tecode.house.liaolian.dao.impl.LLHbaseDaoImpl;
import com.tecode.house.liaolian.dao.impl.LLMySQLDaoImpl;
import com.tecode.house.liaolian.server.LLMaketPriceServer;
import com.tecode.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.Iterator;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;

@Service
public class LLServerImpl implements LLMaketPriceServer, Analysis {


    @Autowired
    private LLMySQLDao sd =new LLMySQLDaoImpl();

    private LLHbaseDao hd=new LLHbaseDaoImpl();


    /**
     *
     * @param tableName 要统计的表名
     * @return
     */
    private boolean intoPerson(String tableName) {
        Map<String, Object> personDistribution = hd.getPersonDistribution(tableName);
        Iterator<Tuple2<String, Object>> pIt = personDistribution.iterator();
        String year=tableName.split(":")[1];
        return sd.into("按区域-税费统计", "普查区域", "/ll_person", "家庭", "税费", "税费统计", pIt, "统计税费", year);
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
        return sd.into("家庭收入", "基础分析", "/ll_value", "家庭", "收入", "家庭收入", vIt, "统计收入区间", year);
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
        return sd.into("按区域-家庭人数", "普查区域", "/ll_income", "区域", "总人数", "家庭总人数", cIt, "家庭总人数统计", year);
    }


    /*
     *
     * @param tablePost 封装了各种条件的对象
     * @return 符合条件的表格数据
     */
    @Override
    public Table getValueTable(TablePost tablePost) {
        Table table = new Table();

        table.addSearchs(new Search().setTitle("家庭收入").addValue("0-1万").addValue("2万-3万").addValue("3万-4万").addValue("4万-5万")
                .addValue("5万-6万").addValue("6万以上"));

        String year = tablePost.getYear().toString();

        //获取查询项
        java.util.List<Search> searches = tablePost.getSearches();

        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("家庭收入");

        Page p = new Page();

        //获取当前页
        int thisPage=tablePost.getPage();

        if(searches.size()==0){
            List<Tuple2<String, Tuple3<String, Object, Object>>> value = hd.getValue("thads:" + year, thisPage);
            setTable3(value, table, p,thisPage);
        }else if(searches.size()==1&&(searches.get(0).getValues().get(0)==null||"".equals(searches.get(0).getValues().get(0)))){
            List<Tuple2<String, Tuple3<String, Object, Object>>> value = hd.getValue("thads:" + year, thisPage);
            setTable3(value, table, p,thisPage);
        }
        else{
            List<Tuple2<Object, Tuple3<String, Object, Object>>> value = hd.getValue("thads:"+year, searches.get(0).getValues().get(0), thisPage);

            setTable2(value, table, p,thisPage);
        }


        return table;
    }

    @Override
    public java.util.Map<String, Integer> getMaket(int year) {
        return sd.get(year);

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

        table.addSearchs(new Search().setTitle("税费统计").addValue("1万以内").addValue("2万以内").addValue("3万以内").addValue("4万以内")
                .addValue("5万以内").addValue("6万以内").addValue("6万以上"));
        table.addSearchs(new Search().setTitle("区域").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5"));

        String year = tablePost.getYear().toString();

        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("税费");
        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));
        java.util.List<Search> searches = tablePost.getSearches();

        String search = "";
        int thisPage=tablePost.getPage();

        if (searches.size() == 0||searches==null) {
            List<Tuple2<String, Tuple3<String, Object, Object>>> person = hd.getPerson("thads:" + year, thisPage);
            setTable3(person,table,p,tablePost.getPage());


        }else if(searches.size()==1){
            search = searches.get(0).getValues().get(0);
            if (search.contains("人")) {
                List<Tuple2<Object, Tuple3<String, Object, Object>>> person = hd.getPerson("thads:" + year, search, thisPage);
                setTable2(person,table,p,thisPage);

            } else {
                List<Tuple2<Object, Tuple3<String, Object, Object>>> person = hd.getPerson("thads:" + year, Integer.valueOf(search), thisPage);
                setTable2(person, table, p,thisPage);

            }
        } else if (searches.size() == 2) {
            List<Tuple2<Object, Tuple3<String, Object, Object>>> person = hd.getPerson("thads:"+year, searches.get(0).getValues().get(0), searches.get(1).getValues().get(0), thisPage);
            setTable2(person, table, p,thisPage);
        }


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

        table.addSearchs(new Search().setTitle("家庭总人数").addValue("2人以下").addValue("2人").addValue("3人").addValue("4人")
        .addValue("5人").addValue("5人以上"));
        table.addSearchs(new Search().setTitle("区域").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5"));

        String year = tablePost.getYear().toString();

        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("家庭人数");
        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));
        java.util.List<Search> searches = tablePost.getSearches();
        int thisPage=tablePost.getPage();
        String search = "";
        if (searches.size() == 0||searches==null) {
            List<Tuple2<String, Tuple3<String, Object, Object>>> income = hd.getIncome("thads:" + year, thisPage);
            setTable3(income,table,p,thisPage);


        }else if(searches.size()==1){
            search = searches.get(0).getValues().get(0);
            if (search.contains("家庭总人数")) {
                List<Tuple2<Object, Tuple3<String, Object, Object>>> income = hd.getIncome("thads:" + year, search, thisPage);
                setTable2(income,table,p,thisPage);

            } else {
                List<Tuple2<Object, Tuple3<String, Object, Object>>> income = hd.getIncome("thads:" + year, Integer.valueOf(search), thisPage);
                setTable2(income,table,p,thisPage);
            }

        } else if (searches.size() == 2) {
            List<Tuple2<Object, Tuple3<String, Object, Object>>> income = hd.getIncome("thads:"+year, searches.get(0).getValues().get(0), searches.get(1).getValues().get(0),thisPage);
            setTable2(income, table,p,thisPage);
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



    private  void  setTable2( List<Tuple2<Object, Tuple3<String, Object, Object>>> income,Table table,Page p,int thisPage){
        Iterator<Tuple2<Object, Tuple3<String, Object, Object>>> iterator = income.iterator();
        int size=0;
        while (iterator.hasNext()){
            Tuple2<Object, Tuple3<String, Object, Object>> next = iterator.next();
            size=Integer.valueOf(next._1.toString());

            Tuple3<String, Object, Object> next1 = next._2;
            table.addData(new Row().addRow(next1._1()).addRow(next1._2().toString()).addRow(next1._3().toString()));

        }
        int page = size / 10 + 1;
        setPage(table,p,page,thisPage);


    }

    private  void  setTable3( List<Tuple2<String, Tuple3<String, Object, Object>>> income,Table table,Page p,int thisPage){
        Iterator<Tuple2<String, Tuple3<String, Object, Object>>> iterator = income.iterator();
        int size=0;
        while (iterator.hasNext()){
            Tuple2<String, Tuple3<String, Object, Object>> next = iterator.next();
            size=Integer.valueOf(next._1.split("_")[0]);

            Tuple3<String, Object, Object> next1 = next._2;
            table.addData(new Row().addRow(next1._1()).addRow(next1._2().toString()).addRow(next1._3().toString()));

        }
        int page = size / 10 + 1;
        setPage(table,p,page,thisPage);


    }


    private void setPage(Table table,Page page,int size,int thisPage){
        if(size>=5&&thisPage>=5){
            if(size>(thisPage+2)){
            table.setPage(page.addData(1).addData(thisPage-2).addData(thisPage-1).addData(thisPage).addData(thisPage+1).addData(size));
            }else if(size==(thisPage+1)){
                table.setPage(page.addData(1).addData(thisPage-2).addData(thisPage-1).addData(thisPage).addData(size));
            }
            else if(thisPage>=size||size==thisPage){
                table.setPage(page.addData(1).addData(size-2).addData(size-1).addData(size));
            }
        }else if(thisPage==4&&size>5){
            table.setPage(page.addData(1).addData(thisPage-1).addData(thisPage).addData(thisPage+1).addData(size));
        }else if(thisPage==3&&size>5){
            table.setPage(page.addData(1).addData(2).addData(thisPage).addData(4).addData(size));
        }else if(thisPage==2&&size>5){
            table.setPage(page.addData(1).addData(thisPage).addData(3).addData(size));
        }else if(thisPage==1&&size>5){
            table.setPage(page.addData(thisPage).addData(2).addData(size));
        }else if(size<=5&&thisPage<=5){
            if(size>=thisPage) {
                for (int i = 1; i <= size; i++) {
                    table.setPage(page.addData(i));
                }
            }else{
                for (int i = 1; i <= thisPage; i++) {
                    table.setPage(page.addData(i));
                }
            }

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
