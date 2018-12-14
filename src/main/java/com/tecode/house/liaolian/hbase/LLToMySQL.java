//package com.tecode.house.liaolian.HBase;
//
//import com.tecode.house.liaolian.until.DBUtil;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class LLToMySQL {
//
//    public static void main(String[] args) {
//        init();
//    }
//
//    private static void init(){
//        String[] s={"CREATE TABLE `report` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `name` varchar(50) NOT NULL,\n" +
//                "  `create` bigint(20) NOT NULL,\n" +
//                "  `year` int(4) NOT NULL,\n" +
//                "  `group` varchar(50) NOT NULL,\n" +
//                "  `status` int(1) NOT NULL,\n" +
//                "  `url` varchar(100) NOT NULL,\n" +
//                "  PRIMARY KEY (`id`)\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n",
//
//                "CREATE TABLE `diagram` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `name` varchar(50) NOT NULL,\n" +
//                "  `type` int(1) NOT NULL,\n" +
//                "  `reportId` int(11) NOT NULL,\n" +
//                "  `subtext` varchar(100) DEFAULT NULL,\n" +
//                "  PRIMARY KEY (`id`),\n" +
//                "  KEY `reportId` (`reportId`),\n" +
//                "  CONSTRAINT `reportId` FOREIGN KEY (`reportId`) REFERENCES `report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n",
//
//                "CREATE TABLE `xaxis` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `name` varchar(20) NOT NULL,\n" +
//                "  `diagramId` int(11) NOT NULL,\n" +
//                "  `dimGroupName` varchar(50) NOT NULL,\n" +
//                "  PRIMARY KEY (`id`),\n" +
//                "  KEY `diagramId` (`diagramId`),\n" +
//                "  CONSTRAINT `diagramId` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n",
//
//        "CREATE TABLE `yaxis` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `name` varchar(255) NOT NULL,\n" +
//                "  `diagramId` int(11) NOT NULL,\n" +
//                "  PRIMARY KEY (`id`),\n" +
//                "  KEY `ydim` (`diagramId`),\n" +
//                "  CONSTRAINT `ydim` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n",
//
//        "CREATE TABLE `dimension` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `groupName` varchar(50) NOT NULL,\n" +
//                "  `dimName` varchar(50) NOT NULL,\n" +
//                "  `dimNameEN` varchar(50) DEFAULT NULL,\n" +
//                "  PRIMARY KEY (`id`)\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n",
//
//        "CREATE TABLE `legend` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `name` varchar(50) NOT NULL,\n" +
//                "  `dimGroupName` varchar(50) NOT NULL,\n" +
//                "  `diagramId` int(11) NOT NULL,\n" +
//                "  PRIMARY KEY (`id`),\n" +
//                "  KEY `diaID` (`diagramId`),\n" +
//                "  CONSTRAINT `diaID` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n",
//
//        "CREATE TABLE `data` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `value` varchar(50) NOT NULL,\n" +
//                "  `xId` int(11) NOT NULL,\n" +
//                "  `legendId` int(11) NOT NULL,\n" +
//                "  `x` varchar(50),\n" +
//                "  `legend` varchar(50),\n" +
//                "  PRIMARY KEY (`id`),\n" +
//                "  KEY `xid` (`xId`),\n" +
//                "  KEY `legid` (`legendId`)\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n",
//
//        "CREATE TABLE `search` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `name` varchar(50) NOT NULL,\n" +
//                "  `dimGroupName` varchar(50) NOT NULL,\n" +
//                "  `reportId` int(11) NOT NULL,\n" +
//                "  PRIMARY KEY (`id`),\n" +
//                "  KEY `reid` (`reportId`),\n" +
//                "  CONSTRAINT `reid` FOREIGN KEY (`reportId`) REFERENCES `report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n"};
//        for (String s1 : s) {
//            create(s1);
//        }
//
//
//    }
//
//    private static void create(String table) {
//        Connection conn=null;
//        try {
//            conn= DBUtil.getConn();
//            Statement stat = conn.createStatement();
//            String report="CREATE TABLE `report` (\n" +
//                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                    "  `name` varchar(50) NOT NULL,\n" +
//                    "  `create` bigint(20) NOT NULL,\n" +
//                    "  `year` int(4) NOT NULL,\n" +
//                    "  `group` varchar(50) NOT NULL,\n" +
//                    "  `status` int(1) NOT NULL,\n" +
//                    "  PRIMARY KEY (`id`)\n" +
//                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n";
//            boolean execute = stat.execute(table);
//            if(!execute){
//                System.out.println("创建表成功");
//            }else {
//                System.out.println("创建表失败");
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            DBUtil.close(conn);
//        }
//
//    }
//
//
//}
