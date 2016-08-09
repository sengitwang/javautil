package com.senit.javautil.util;

import com.senit.javautil.model.ExcelEntity;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.commons.collections.map.LinkedMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sen on 2016/8/9.
 * 导出Excel
 */
public class JxlExcelUtil {
    /**
     * 导出Excel
     * @param fileName
     * @param list
     * @return
     * @throws Exception
     */
    public static boolean exportToExcel(String fileName, List<ExcelEntity> list) throws Exception{
        //声明工作簿jxl.write.WritableWorkbook
        WritableWorkbook wwb = null;
        OutputStream os = null;

        //根据传进来的file对象创建可写入的Excel工作薄
        try {
            os = new FileOutputStream(fileName);

            wwb = Workbook.createWorkbook(os);

            for (int i = 0; i < list.size(); i++){
                ExcelEntity excelEntity = list.get(i);
                addSheetContent(wwb, excelEntity.getContent(), excelEntity.getSheetName(), excelEntity.getKeys(), excelEntity.getColumns(), i);
            }

        } catch (FileNotFoundException e) {
           // log.error("未找到文件fileName:" + fileName + ",message:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
           // log.error("创建Excel文件失败message:" + e.getMessage());
            e.printStackTrace();
        } finally {
            //写入Exel工作表
            wwb.write();

            //关闭Excel工作薄对象
            wwb.close();

            //关闭流
            os.flush();
            os.close();
            os =null;
        }
       // log.info("导出Excel成功,文件路径:" + fileName);


        return true;
    }
    /**
     * @author
     * @param objData 导出内容数组
     * @param sheetName 导出工作表的名称
     * @param columns 导出Excel的表头数组
     * @throws Exception
     */
    private static void addSheetContent(WritableWorkbook wwb, List<LinkedMap> objData, String sheetName, List<String> keys, LinkedMap columns, int index) throws Exception {
        try {
			/*
			 * 创建一个工作表、sheetName为工作表的名称、"0"为第一个工作表
			 * 打开Excel的时候会看到左下角默认有3个sheet、"sheet1、sheet2、sheet3"这样
			 * 代码中的"0"就是sheet1、其它的一一对应。
			 * createSheet(sheetName, 0)一个是工作表的名称，另一个是工作表在工作薄中的位置
			 */
            WritableSheet ws = wwb.createSheet(sheetName, index);

            SheetSettings ss = ws.getSettings();
            ss.setVerticalFreeze(1);//冻结表头

            WritableFont font1 =new WritableFont(WritableFont.createFont("微软雅黑"), 10 ,WritableFont.NO_BOLD);
            WritableFont font2 =new WritableFont(WritableFont.createFont("微软雅黑"), 9 ,WritableFont.NO_BOLD);
            WritableCellFormat wcf = new WritableCellFormat(font1);
            WritableCellFormat wcf2 = new WritableCellFormat(font2);
            WritableCellFormat wcf3 = new WritableCellFormat(font2);//设置样式，字体

            //创建单元格样式
            //WritableCellFormat wcf = new WritableCellFormat();

            //背景颜色
            wcf.setBackground(jxl.format.Colour.BLACK);
            wcf.setAlignment(Alignment.CENTRE);  //平行居中
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
            wcf3.setAlignment(Alignment.CENTRE);  //平行居中
            wcf3.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中
            wcf3.setBackground(Colour.LIGHT_ORANGE);
            wcf2.setAlignment(Alignment.CENTRE);  //平行居中
            wcf2.setVerticalAlignment(VerticalAlignment.CENTRE);  //垂直居中

			/*
			 * 这个是单元格内容居中显示
			 * 还有很多很多样式
			 */
            wcf.setAlignment(Alignment.CENTRE);

            //判断一下表头数组是否有数据
            if (keys != null && keys.size() > 0) {

                for (int i = 0 ; i < objData.size(); i++) {
                    for (int j = 0; j < keys.size(); j++) {
                        if(i == 0){
							/*
							 * 添加单元格(Cell)内容addCell()
							 * 添加Label对象Label()
							 * 数据的类型有很多种、在这里你需要什么类型就导入什么类型
							 * 如：jxl.write.DateTime 、jxl.write.Number、jxl.write.Label
							 * Label(i, 0, columns[i], wcf)
							 * 其中i为列、0为行、columns[i]为数据、wcf为样式
							 * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中
							 */
                            ws.addCell(new Label(j, 0, columns.get(keys.get(j)).toString(), wcf));
//							ws.addCell(new Label(j, 0, keys.get(j), wcf));
                        }

                        //转换成map集合{activyName:测试功能,count:2}
                        LinkedMap map = (LinkedMap)objData.get(i);

                        //循环输出map中的子集：既列值
                        //ps：因为要“”通用”“导出功能，所以这里循环的时候不是get("Name"),而是通过map.get(o)
                        ws.addCell(new Label(j,i+1,map.get(keys.get(j)) == null ? "" : map.get(keys.get(j)).toString()));
                    }
                }

                if(objData == null || objData.size() == 0){
                    for (int j = 0; j < keys.size(); j++) {
						/*
						 * 添加单元格(Cell)内容addCell()
						 * 添加Label对象Label()
						 * 数据的类型有很多种、在这里你需要什么类型就导入什么类型
						 * 如：jxl.write.DateTime 、jxl.write.Number、jxl.write.Label
						 * Label(i, 0, columns[i], wcf)
						 * 其中i为列、0为行、columns[i]为数据、wcf为样式
						 * 合起来就是说将columns[i]添加到第一行(行、列下标都是从0开始)第i列、样式为什么"色"内容居中
						 */
                        ws.addCell(new Label(j, 0, columns.get(keys.get(j)).toString(), wcf));
                    }
                }

            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public static void main(String[] args){
        List<ExcelEntity> list = new ArrayList<ExcelEntity>();

        ExcelEntity ee = new ExcelEntity();

        List<String> columns = new ArrayList<String>();
        columns.add("行号");
        columns.add("名称");
        columns.add("内容");

        ee.setKeys(columns);

        List<LinkedMap> content = new ArrayList<LinkedMap>();
        for (int i = 0; i < 4; i++) {
            LinkedMap map = new LinkedMap();
            map.put("行号", "行号" + i);
            map.put("名称", "名称" + i);
            map.put("内容", "内容" + i);
            content.add(map);
        }
        ee.setContent(content);
        ee.setSheetName("短款");

        list.add(ee);
        ee = new ExcelEntity();
        ee.setKeys(columns);
        ee.setContent(content);
        ee.setSheetName("长款");
        list.add(ee);

        try {
            JxlExcelUtil.exportToExcel("D:/test2.xls", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
