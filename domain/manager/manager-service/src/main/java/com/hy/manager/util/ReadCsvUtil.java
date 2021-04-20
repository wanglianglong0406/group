package com.hy.manager.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/2/6 20:51
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/2/6 20:51
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class ReadCsvUtil {

    private static final String FIX="\uFEFF";
    /**
     * 获取csv文件内容
     * @return 对象list
     */
    public static List<String> getResource(byte[] bate) throws IOException {
        List<String> list = new ArrayList();
        // 获取文件内容
        list = getSource(bate);
        return  list;
    }

    /**
     * 读文件数据
     */
    public static List<String> getSource(byte[] bate) throws  IOException {
        BufferedReader br = null;
        ByteArrayInputStream fis=null;
        InputStreamReader isr = null;
        try {
            fis = new ByteArrayInputStream(bate);
            //指定以UTF-8编码读入
            isr = new InputStreamReader(fis,"UTF-8");
            br = new BufferedReader(isr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String line;
        String everyLine ;
        List<String> allString = new ArrayList<>();
        try {
            //读取到的内容给line变量
            while ((line = br.readLine()) != null){
                everyLine = line;
                allString.add(everyLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis != null){
                fis.close();
            }
            if(isr != null){
                isr.close();
            }
        }
        return allString;
    }
}
