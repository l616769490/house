package com.tecode.house.libo.test

import com.tecode.table.{Page, Table}

object test extends App{

  val t = new Table();
  t.setPage(new Page().setThisPage(5))
  t.setPage(new Page().addData(1))
  println(t.getPage)
}
