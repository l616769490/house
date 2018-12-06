package com.tecode.house.liuhao.control

import com.tecode.table.TablePost
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RequestParam, ResponseBody}

/**
  * Created by Administrator on 2018/12/6.
  */
@Controller
class PackageTable {
  @ResponseBody
  @RequestMapping(value = Array("/test-table"), method = Array(RequestMethod.POST))
  def tabletest(@RequestParam(required = false)tablepost:TablePost,tablename:String)={
    val con = new SparkConf().setAppName("hbase").setMaster("local[*]")
    val sc = new SparkContext(con)
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tablename)
    conf.set(TableInputFormat.SCAN_COLUMNS, "INFO")
    //读取hbase中数据并转换为rdd

    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],

      classOf[Result]).map(x =>
        x._2.getValue(Bytes.toBytes(""))



  }
}
