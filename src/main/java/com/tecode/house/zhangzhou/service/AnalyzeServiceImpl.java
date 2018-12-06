package com.tecode.house.zhangzhou.service;

import com.tecode.house.d01.service.Analysis;
import com.tecode.house.zhangzhou.hbaseDao.HBaseDao;
import com.tecode.house.zhangzhou.hbaseDao.impl.HBaseDaoImpl;
import com.tecode.house.zhangzhou.mysqlDao.impl.MysqlDaoImpl;

/**
 * 实现数据分析接口
 * @author zhangzhou
 */
public class AnalyzeServiceImpl implements Analysis {
    private boolean judge = true;
    /**
     * 初始化时创建命名空间
     */
   /* static{
        HBaseDao hBaseDao = new HBaseDaoImpl();
        boolean bl = hBaseDao.createNameSpace();
        if(bl){
            System.out.println("创建命名空间成功");
        }else {
            System.out.println("创建命名空间失败");
        }
    }*/

    /**
     *
     * @param tableName HBase数据库名字
     * @return 返回成功/失败
     */
    @Override
    public boolean analysis(String tableName) {
        /*//首先在HBase中创建表格，调用HBaseDao
        HBaseDao hBaseDao = new HBaseDaoImpl();
        boolean bl = hBaseDao.create(tableName);
        if(!bl){
            System.out.println("创建hbase数据库表格失败");
            return false;
        }
        //在hbase的“house”表格中添加数据
        String path = "E:\\american\\thads2013n.csv";
        boolean bl1 = hBaseDao.insert(tableName,path);
        if(!bl1){
            System.out.println("插入数据失败");
            return false;
        }*/

        //第一次没有表时创建mysql表
        /*if(judge){
            boolean bl = createMysqlTableState();
            if(bl){
                judge = false;
            }
        }
        System.out.println("正在向mysql表中插入数据...");
        insertIntoMysql(tableName);
        System.out.println("插入数据成功");*/


        return true;
    }
    /**
     * 在mysql中创建表阶段,创建8个表
     */
    private boolean createMysqlTableState(){
        boolean bl1 = MysqlDaoImpl.createMysqlTables();
        if(bl1){
            System.out.println("mysql表创建成功！");
        }else {
            System.out.println("mysql表创建失败！");
        }
        return bl1;
    }

    /**
     * 将hbase分析数据导入mysql
     * @param tableName hbase表格名称
     *
     */
    private void insertIntoMysql(String tableName){
        //数据分析并导入mysql阶段
        SparkService ss = new SparkService();
        ss.selectVacancyState(tableName,2013);
        ss.selectSingleBuildingGroupByCity(tableName,2013);
        ss.selectHouseDutyGroupByCity(tableName,2013);
    }

    public static void main(String[] args) {
        AnalyzeServiceImpl as = new AnalyzeServiceImpl();

        boolean bl = as.analysis("thads:2013");

    }
}
