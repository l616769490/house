package com.tecode.house.zzliuhao.dao.impl;


import com.tecode.house.zzliuhao.bean.City;
import com.tecode.house.zzliuhao.bean.Xaxis;
import com.tecode.house.zzliuhao.dao.ReadMyqslDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
public class ReadMysqlDaoImpl implements ReadMyqslDao {
    /**
     * 读取mysql中和建筑结构类型相关的数据
     *
     * @param conn
     * @return
     */
    public List ReadTableData(Connection conn, String where) {
        Statement statement = null;

        List<String> list = new ArrayList<>();
        try {
            statement = conn.createStatement();
            //sql查询语句
            String sql = "select x,value from data where id" + where;
            //用于返回结果
            ResultSet resultSet = statement.executeQuery(sql);
            String xname = "";
            String xvalue = "";
            String value = "";
            Xaxis x = new Xaxis();
            while (resultSet.next()) {
                xname = resultSet.getString(1);
                xvalue = resultSet.getString(2);
                value = xname + ":" + xvalue;


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (String s : list) {
            System.out.println(s);
        }
        return list;
    }

    @Override
    public City ReadCityTax(Connection conn) {
        Statement statement = null;

        List<String> list = new ArrayList<>();
        City city1 = new City();
        try {
            statement = conn.createStatement();
            //sql查询语句
            String sql = "select x,value from data where id <16";
            //用于返回结果
            ResultSet resultSet = statement.executeQuery(sql);
            String xname = "";
            int xvalue = 0;
            String city = "";
            String type = "";
            Xaxis x = new Xaxis();

            while (resultSet.next()) {
                xname = resultSet.getString(1);
                city = xname.substring(0, 4);
                type = xname.substring(4, 8);
                xvalue = resultSet.getInt(2);

                if (type.equals("房屋费用")) {

                    if (city.equals("一线城市")) {

                        city1.setOneHomeCost(xvalue);
                    } else if (city.equals("二线城市")) {

                        city1.setTwoHomeCost(xvalue);
                    } else if (city.equals("三线城市")) {

                        city1.setThreeHomeCost(xvalue);
                    } else if (city.equals("四线城市")) {

                        city1.setFourHomeCost(xvalue);
                    } else if (city.equals("五线城市")) {

                        city1.setFiveHomeCost(xvalue);
                    }
                }
                if (type.equals("水电费用")) {
                    if (city.equals("一线城市")) {

                        city1.setOneUtityFee(xvalue);
                    } else if (city.equals("二线城市")) {

                        city1.setTwoUtityFee(xvalue);
                    } else if (city.equals("三线城市")) {

                        city1.setThreeUtityFee(xvalue);
                    } else if (city.equals("四线城市")) {

                        city1.setFourUtityFee(xvalue);
                    } else if (city.equals("五线城市")) {

                        city1.setFiveUtityFee(xvalue);
                    }
                }
                if (type.equals("其他费用")) {
                    if (city.equals("一线城市")) {
                        System.out.println("进入双循环");

                        city1.setOneOtherCost(xvalue);
                    } else if (city.equals("二线城市")) {

                        city1.setTwoOtherCost(xvalue);
                    } else if (city.equals("三线城市")) {

                        city1.setThreeOtherCost(xvalue);
                    } else if (city.equals("四线城市")) {

                        city1.setFourOtherCost(xvalue);
                    } else if (city.equals("五线城市")) {

                        city1.setFiveOtherCost(xvalue);
                    }

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return city1;
    }


}
