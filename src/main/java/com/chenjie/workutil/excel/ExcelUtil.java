package util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
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
        if(file.exists())
        {
            return;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook.createSheet("包名");
        FileOutputStream fo = new FileOutputStream(file);
        workbook.write(fo);
        fo.close();
    }
    private static void createNewExcelAndFirstRow(File file) throws Exception
    {
        if(file.exists())
        {
            return;
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("K5");
        createFirstRow(sheet, new String[]{"测试人","编号","应用名称","应用包名","版本号","版本名","应用简介","下载地址","参考标签","确认分类标签","安全风险标签","有无付费标签","发布方式","备注","应用级别","是否有异常信息","抓包来源"});
        sheet = workbook.createSheet("其他机型");
        createFirstRow(sheet,new String[]{"测试人","编号","应用名称","应用包名","版本名","版本号","下载地址","下载量","机型","状态","测试结果","备注"});
        sheet = workbook.createSheet("抓包不到");
        createFirstRow(sheet,new String[]{"应用名称","包名","版本名","机型","备注"});
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
    public static void writeMultiCol(Collection<List<String>> datas, File file, int sheetNum) throws Exception
    {
        createNewExcelAndFirstRow(file);
        FileInputStream fi = new FileInputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook(fi);
        Sheet sheet = workbook.getSheetAt(sheetNum);

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
    public static List<String> read(InputStream is, int sheetNum, int firstRow)
    {
        List<String> resultList = new ArrayList<>();
        XSSFWorkbook xwb;
        try
        {
            xwb = new XSSFWorkbook(is); // 创建Excel2007文件对象
            // 读取第一章表格内容
            XSSFSheet sheet = xwb.getSheetAt(sheetNum);
            XSSFRow row;
            int allRows = sheet.getPhysicalNumberOfRows();
            for(int i = firstRow ; i < allRows; i++ )
            {
                row = sheet.getRow(i); // 获取行对象
                if (row == null)
                { // 如果为空，不处理
                    continue;
                }
                Map<String,String > beanMap = new HashMap<>();
                XSSFCell cell = row.getCell(0);
                if(cell != null)
                {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = cell.getStringCellValue();
                    resultList.add(value);
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
}
