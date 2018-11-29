package com.tecode.house.lijin.test;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

/**
 * 文件上传
 */
@Controller
public class TestFile {
    // 当前年份
    private int nowYear = Calendar.getInstance().get(Calendar.YEAR);


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

        // 新建文件，文件保存在/upload目录下，以"年份.csv"作为文件名
        File file = new File(path + "/" + year + ".csv");
        try (InputStream inputStream = partFile.getInputStream()) {
            FileUtils.copyInputStreamToFile(inputStream, file);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/file-page")
    public String upload() {
        return "file";
    }


}
