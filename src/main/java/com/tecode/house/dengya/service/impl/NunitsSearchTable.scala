package com.tecode.house.dengya.service.impl

import java.util

import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class NunitsSearchTable {

    def read(tableName:String, sc: SparkContext):util.ArrayList[util.ArrayList[String]] ={
      val hconf = HBaseConfiguration.create()
      //    配置读取的表名
      hconf.set(TableInputFormat.INPUT_TABLE, tableName)
      //    配置读取的列族
      hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
      //    获取数据
      val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

      val list: util.ArrayList[String] = new util.ArrayList[String]()
      val lists: util.ArrayList[util.ArrayList[String]] = new util.ArrayList[util.ArrayList[String]]()

      val value: RDD[Result] = valuess.map(_._2)
      value.map(x =>{
        //      遍历每个Result对象，获取数据
        val cells: Array[Cell] = x.rawCells()
        //房屋编号//房屋编号

        var CONTROL:String = null
        //城市等级
        var METRO3:String = null
        //建成年份
        var BUILT:String = null
        //建筑单元书
        var NUNITS:String = null
        for (elem <- cells) {
          //        获取传入的列的值
         val str: String = Bytes.toString(CellUtil.cloneRow(elem))
          val rowKey: Array[String] = str.split("_")
          CONTROL = rowKey(1)
          if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
            METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
          if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
            BUILT = Bytes.toString(CellUtil.cloneValue(elem))
          if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("NUNITS"))
            NUNITS = Bytes.toString(CellUtil.cloneValue(elem))

        }
        list.add(CONTROL)
        list.add(METRO3)
        list.add(BUILT)
        list.add(NUNITS)
        lists.add(list)
      })

      lists
    }
}
