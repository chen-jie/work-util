package com.chenjie.workutil.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil
{
    private static void createNewExcel(File file) throws Exception
    {
        createNewExcel(file, "first", null);
    }

    /**
     * 创建一个excel文件（已存在，删除原文件），初始化第一行数据
     *
     * @param file
     * @param firstSheetName
     * @param firstRows
     */
    private static void createNewExcel(File file,String firstSheetName, String[] firstRows) throws Exception
    {
        if(file.exists())
        {
            file.delete();
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(firstSheetName);
        if(firstRows != null && firstRows.length > 0){
            createFirstRow(sheet, firstRows);
        }
        FileOutputStream fo = new FileOutputStream(file);
        workbook.write(fo);
        fo.close();
    }

    private static void createFirstRow(XSSFSheet sheet, String[] cols)
    {
        XSSFRow row = sheet.createRow(0);
        int cellCount = 0;
        for (String col : cols)
        {
            XSSFCell cell = row.createCell(cellCount++);
            cell.setCellValue(col);
        }
    }

    /**
     * 向excel文件里面写数据，只能写一列
     *
     * @param datas
     * @param file
     */
    public static void write(Collection<String> datas, File file) throws Exception
    {
        createNewExcel(file);
        FileInputStream fi = new FileInputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook(fi);
        Sheet sheet = workbook.getSheetAt(0);

        for (String data : datas)
        {
            int lastRowNum = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRowNum+1);
            Cell cell = row.createCell(0);
            cell.setCellValue(data);
        }
        FileOutputStream fo = new FileOutputStream(file);
        workbook.write(fo);

        fi.close();
        fo.flush();
        fo.close();
    }

    /**
     * 向excel文件里面写数据，写多列
     *
     * @param datas
     * @param file
     */
    public static void writeMultiCol(Collection<List<String>> datas, File file) throws Exception
    {
        createNewExcel(file);
        FileInputStream fi = new FileInputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook(fi);
        Sheet sheet = workbook.getSheetAt(0);

        for (List<String> data : datas)
        {
            int lastRowNum = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRowNum+1);
            int cellCount = 0;
            for (String item : data)
            {
                Cell cell = row.createCell(cellCount++);
                cell.setCellValue(item);
            }
        }
        FileOutputStream fo = new FileOutputStream(file);
        workbook.write(fo);

        fi.close();
        fo.flush();
        fo.close();
    }

    /**
     * excel 2007格式
     * @param is 文件输入流
     */
    public static List<Map> readFileFromInputStreamPOIX(InputStream is, int sheetNum, int firstRow , Map<Integer,String> colMap)
    {
        List<Map> resultList = new ArrayList<>();
        XSSFWorkbook xwb;
        try
        {
            xwb = new XSSFWorkbook(is); // 创建Excel2007文件对象
            // 读取第一章表格内容
            XSSFSheet sheet = xwb.getSheetAt(sheetNum);
            XSSFRow row;
//            int allRows = sheet.getPhysicalNumberOfRows();
            int allRows = sheet.getLastRowNum();
            for(int i = firstRow ; i <= allRows; i++ )
            {
                row = sheet.getRow(i); // 获取行对象
                if (row == null)
                { // 如果为空，不处理
                    continue;
                }
                Map<String,String > beanMap = new HashMap<>();
                for (Map.Entry<Integer, String> entry : colMap.entrySet())
                {
                    Integer col = entry.getKey();
                    String property = entry.getValue();
                    XSSFCell cell = row.getCell(col);
                    if(cell != null)
                    {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String value = cell.getStringCellValue();
                        beanMap.put(property,value);
                    }
                }
                if(!beanMap.isEmpty())
                {
                    resultList.add(beanMap);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                is.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public static List<Map> readExcel2007(String filePath,int sheetNum,int firstRow,Map colMap)
    {
        try {
            InputStream is = new FileInputStream(filePath);
            return readFileFromInputStreamPOIX(is,sheetNum,firstRow,colMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
