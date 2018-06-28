package com.chenjie.workutil;


import com.chenjie.workutil.excel.ExcelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelTest {

       @Test
    public void testWrite() throws Exception {
        List<List<String>> personList = new ArrayList<>();

        List<String> person1 = new ArrayList<>();
        person1.add("张三");
        person1.add("男");
        person1.add("24");
        person1.add("湖南人");
        personList.add(person1);

        List<String> person2 = new ArrayList<>();
        person2.add("李四");
        person2.add("女");
        person2.add("21");
        person2.add("广东人");
        personList.add(person2);

        List<String> person3 = new ArrayList<>();
        person3.add("王五");
        person3.add("男");
        person3.add("22");
        person3.add("美国人");
        personList.add(person3);

        ExcelUtil.writeMultiCol(personList, new File("D:\\用户数据.xlsx"));
    }

    @Test
    public void testRead() throws Exception {
        // 列与字段名，key代表读取excel中的第几列，value代表读取出来后的字段名
        Map<Integer,String> colMap = new HashMap<>();
        colMap.put(0, "name");
        colMap.put(1, "sex");
        colMap.put(2, "age");
        colMap.put(3, "from");

        // 读取第1张excel表
        int sheetNum = 0;

        // 从第2行开始读
        int firstRow = 1;

        // excel路径
        String filePath = "D:\\用户数据.xlsx";

        List<Map> personList = ExcelUtil.readExcel2007(filePath, sheetNum, firstRow, colMap);

        System.out.println("totalSize:"+personList.size());

        for (Map personMap : personList) {
            System.out.println(String.format("%s \t%s\t%s\t%s",
                    personMap.get("name"),personMap.get("age"),personMap.get("from"),personMap.get("sex")));
        }
    }

}
