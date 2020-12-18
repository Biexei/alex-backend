package org.alex.platform.util;

import org.alex.platform.exception.BusinessException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
public class ExcelUtil {

    /**
     * excel读取
     * @param fullPath 全路径（含文件名称）
     * @return 二维list
     * @throws IOException 解析异常
     * @throws BusinessException 格式错误
     */
    public static ArrayList<List> read(String fullPath) throws BusinessException, IOException {
        FileInputStream fis = new FileInputStream(fullPath);
        String type = fullPath.substring(fullPath.lastIndexOf(".") + 1).trim();
        ArrayList<List> result = new ArrayList<>();

        Workbook workbook;
        Row row;
        Cell cell;

        if ("xlsx".equalsIgnoreCase(type)) {
            workbook = new XSSFWorkbook(fis);
        } else if ("xls".equalsIgnoreCase(type)) {
            workbook = new HSSFWorkbook(fis);
        } else {
            throw new BusinessException("格式错误");
        }
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            //获得当前sheet的开始行
            int firstRowNum  = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //无数据sheet跳过
            if (lastRowNum == 0) {
                continue;
            }
            //获取总列数
            int columnNum = sheet.getRow(0).getPhysicalNumberOfCells();
            //循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++){
                ArrayList rowData = new ArrayList<>();
                //获得当前行
                row = sheet.getRow(rowNum);
                if(row == null){
                    continue;
                }
                for (int c = 0; c < columnNum; c++) {
                    cell = row.getCell(c);
                    if (cell != null) {
                        CellType cellTypeEnum = cell.getCellTypeEnum();
                        if (cellTypeEnum == CellType.STRING) {
                            rowData.add(cell.getStringCellValue());
                        } else if (cellTypeEnum == CellType.BLANK) {
                            rowData.add("");
                        } else if (cellTypeEnum == CellType.NUMERIC) {
                            rowData.add(cell.getNumericCellValue());
                        } else if (cellTypeEnum == CellType.BOOLEAN) {
                            rowData.add(cell.getBooleanCellValue());
                        } else if (cellTypeEnum == CellType.FORMULA) {
                            rowData.add(cell.getCellFormula());
                        } else {
                            rowData.add("error! unknown format");
                        }
                    }
                }
                result.add(rowData);
            }
        }
        fis.close();
        return result;
    }
}