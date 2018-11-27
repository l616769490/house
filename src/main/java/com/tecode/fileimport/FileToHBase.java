package com.tecode.fileimport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件导入到HBase
 */
public class FileToHBase {

    /**
     * 从文件中导入数据到HBase
     *
     * @param path 文件路径
     */
    public static void fileToHbase(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            char c = line.charAt(0);
            while (!Character.isLowerCase(c) && !Character.isUpperCase(c)) {
                line = br.readLine();
            }
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = FileToHBase.class.getResource("/upload/2011.csv").getPath();
        fileToHbase(path);
    }

}
