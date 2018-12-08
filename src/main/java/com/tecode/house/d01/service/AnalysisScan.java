package com.tecode.house.d01.service;


import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnalysisScan {
    public static void main(String[] args) throws Exception {
        File file = new File(AnalysisScan.class.getResource("/").getPath());
        List<File> files = getClassFile(file);
        classLoader(files);
    }

    private static List<File> getClassFile(File file) {
        List<File> fileList = new ArrayList<>();
        File[] files = file.listFiles();
        if(files == null) {
            return fileList;
        } else {
            for (File f : files) {
                if(f.isFile()) {
                    String path = f.getPath();
                    if(path.substring(path.lastIndexOf(".") + 1).equals("class"))
                    fileList.add(f);
                } else if( f.isDirectory()  ) {
                    fileList.addAll(getClassFile(f));
                }
            }
        }

        return fileList;
    }

    private static void classLoader(List<File> files) throws Exception {

        for (File file : files) {
            String path = file.getPath();
            int indexOf = path.indexOf("com");
            int lastIndexOf = path.lastIndexOf(".");
            String classPath = path.substring(indexOf, lastIndexOf).replace("\\", ".").replace("/", ".");
            Class<?> aClass = Class.forName(classPath);
            if(!aClass.isInterface()) {
                Class<?>[] interfaces = aClass.getInterfaces();
                if(interfaces != null) {
                    boolean b = false;
                    for (Class<?> anInterface : interfaces) {
                        if( anInterface.getSimpleName().equals("Analysis")) {
                            b = true;
                        }
                    }
                    if(b) {
                        Object obj = aClass.newInstance();
                        Method method = aClass.getMethod("analysis", String.class);
                        method.invoke(obj,"thads:2013");
                    }
                }
            }
        }
    }
}
