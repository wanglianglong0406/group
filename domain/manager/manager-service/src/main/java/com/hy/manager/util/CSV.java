package com.hy.manager.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/2/6 12:47
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/2/6 12:47
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class CSV {

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_RN = "\r\n";


    public static void exportCsv(HttpServletResponse response, HttpServletRequest request) {
        long startTime = System.currentTimeMillis();
        // 设置表格头
        Object[] head = {"商品名称", "商品描述", "商品价格", "商品链接地址（图片地址）"};
        List<Object> headList = Arrays.asList(head);
        List<List<Object>> dataList = getNovel();
        // 导出文件路径
        String downloadFilePath = request.getSession().getServletContext().getRealPath("");
        // 导出文件名称
        String fileName = "download";
        // 导出CSV文件
        File csvFile = CSV.createCSVFile(headList, dataList, downloadFilePath, fileName);

//########################################################################
        try {

            // 取得文件名。
            String filename = csvFile.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件。
//            InputStream fis = new FileInputStream(csvFile);
            FileInputStream fis = new FileInputStream(csvFile);
            // 设置response的Header

            String userAgent = request.getHeader("User-Agent");
//            // 针对IE或者以IE为内核的浏览器：
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                filename = java.net.URLEncoder.encode(filename, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            }
//            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", filename));
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");


//            response.addHeader("Content-Disposition", "attachment;filename=大数据.csv" );
//            response.addHeader("Content-Length", "" + csvFile.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");


            int content = 0;
            while ((content = fis.read()) != -1) {
                toClient.write(content);
            }
            fis.close();
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

//#############################################################################

        long endTime = System.currentTimeMillis();
        System.out.println("整个CSV导出" + (endTime - startTime));
    }

    private static List<List<Object>> getNovel() {
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        List<Object> rowList = null;
//        for (int i = 0; i < 5; i++) {
//            rowList = new ArrayList<Object>();
//            Object[] row = new Object[5];
//            row[0] = "test"+i;
//            row[1] = "风云第一刀" + i + "";
//            row[2] = 100.00;
//            row[3] = 1;
//            row[4] = "http://www.xxx.com";
//            for (int j = 0; j < row.length; j++) {
//                rowList.add(row[j]);
//            }
//            dataList.add(rowList);
//        }
        return dataList;
    }


    /**
     * CSV文件生成方法
     *
     * @param head
     * @param dataList
     * @param outPutPath
     * @param filename
     * @return
     */
    public static File createCSVFile(List<Object> head, List<List<Object>> dataList, String outPutPath, String filename) {
        File csvFile = null;
        BufferedWriter csvWtriter = null;
        try {
            csvFile = new File(outPutPath + File.separator + filename + ".csv");
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            // GB2312使正确读取分隔符","
            csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"), 1024);
            // 写入文件头部
            writeRow(head, csvWtriter);

            // 写入文件内容
            for (List<Object> row : dataList) {
                writeRow(row, csvWtriter);
            }
            csvWtriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWtriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 写一行数据方法
     *
     * @param row
     * @param csvWriter
     * @throws IOException
     */
    private static void writeRow(List<Object> row, BufferedWriter csvWriter) throws IOException {
        // 写入文件头部
        for (Object data : row) {
            StringBuffer buf = new StringBuffer();
            // String rowStr = buf.append("\"").append(data).append("\t\",").toString();
            String rowStr = buf.append(data).append("\t,").toString();

            csvWriter.write(new String(rowStr.getBytes("UTF-8")));
        }
        csvWriter.newLine();
    }


}
