package com.tecode.house.lijin.test;

import com.tecode.fileimport.FileToHBase;
import com.tecode.house.lijin.hbase.HBaseFromFile;
import com.tecode.mysql.server.DelSQL;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * 文件上传
 */
@Controller
public class TestFile {
    // 当前年份
    private int nowYear = Calendar.getInstance().get(Calendar.YEAR);

    private Configuration conf = null;
    // hdfs 文件系统
    private FileSystem fs = null;

    public TestFile() {

    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@RequestParam("file") CommonsMultipartFile partFile, HttpServletRequest request) {
        // 路径
        String path = request.getSession().getServletContext().getRealPath("/upload");
        // 获取年份
        int year = 0;
        try {
            year = Integer.parseInt(request.getParameter("year"));
        } catch (NumberFormatException e) {
            return "year error";
        }

        if (year < 1970 || year > nowYear) {
            return "year on 1970-" + nowYear;
        }

        // 获取选择框的数据
        String check = request.getParameter("check");

        path = path + "/" + year + ".csv";

        // 新建文件，文件保存在/upload目录下，以"年份.csv"作为文件名
        File file = new File(path);
        boolean b = file.exists();
        // 如果已经存在且用户没选择覆盖
        if (b && !"on".equals(check)) {
            return "exists";
        }

        // 清除数据库
        DelSQL.delSqlByYear(year);

        try (InputStream inputStream = partFile.getInputStream()) {
            FileUtils.copyInputStreamToFile(inputStream, file);
            fileToHBase(path);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 开启新线程上传文件到HBase
     *
     * @param path 文件路径
     */
    private void fileToHBase(String path) {
        Thread thread = new Thread(() ->  new FileToHBase().fileToHbase(path, true));
        thread.start();
    }

    @RequestMapping(value = "/upload1", method = RequestMethod.POST)
    @ResponseBody
    public String uploadToHDFS(@RequestParam("file") CommonsMultipartFile partFile, HttpServletRequest request) {
        try {
            conf = new Configuration();
            fs = FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 路径
        String path = "/upload/";
        // 获取年份
        int year = 0;
        try {
            year = Integer.parseInt(request.getParameter("year"));
        } catch (NumberFormatException e) {
            return "year error";
        }

        if (year < 1970 || year > nowYear) {
            return "year on 1970-" + nowYear;
        }

        // 获取选择框的数据
        String check = request.getParameter("check");

        path = path + year + ".csv";
        // 判断是否已有数据
        boolean b = isExist(path);
        // 如果已经存在且用户没选择覆盖
        if (b && !"on".equals(check)) {
            return "fail";
        }

        if (b) {
            delectFile(path);
        }

        return uploadFileToHDFS(partFile, path);
    }

    // 上传文件到HDFS
    private String uploadFileToHDFS(CommonsMultipartFile partFile, String path) {
        try {
            FSDataOutputStream outputStream = fs.create(new Path(path));
            InputStream inputStream = partFile.getInputStream();
            byte[] bs = new byte[1024 * 1024];
            int len = -1;
            while ((len = inputStream.read(bs)) >= 0) {
                outputStream.write(bs, 0, len);
            }

            return "success";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "fail";
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件路径
     * @return 是否存在
     */
    private boolean isExist(String fileName) {
        try {
            return fs.exists(new Path(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 删除HDFS文件
    private void delectFile(String path) {
        try {
            fs.delete(new Path(path), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/file-page")
    public String upload() {
        return "file";
    }

}
