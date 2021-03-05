package org.alex.platform.util;

import com.csvreader.CsvReader;
import org.alex.platform.enums.ExcelType;
import org.alex.platform.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked","rawtypes"})
public class FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);
    /**
     * 通过NIO读取文件
     * @param fullPath 全路径
     * @param charset 字符集
     * @return 文件内容
     */
    public static String readByNIO(String fullPath, Charset charset) {
        if (StringUtils.isEmpty(fullPath)) {
            throw new RuntimeException("full path should not be empty or null");
        }
        RandomAccessFile randomAccessFile;
        FileChannel channel = null;
        StringBuilder sb = null;
        try {
            randomAccessFile = new RandomAccessFile(fullPath, "r");
            channel = randomAccessFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            sb = new StringBuilder();
            int length;
            while((length = channel.read(buffer)) != -1) {
                buffer.clear();
                byte[] bytes = buffer.array();
                String s = new String(bytes, 0, length, charset);
                sb.append(s);
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    LOG.error(ExceptionUtil.msg(e));
                    e.printStackTrace();
                }
            }
        }
        return sb == null ? null : sb.toString();
    }

    /**
     * 通过缓冲流读取文件
     * @param fullPath 全路径
     * @param charset 字符集
     * @return 文件内容
     */
    public static String readByBuffer(String fullPath, Charset charset) {
        if (StringUtils.isEmpty(fullPath)) {
            throw new RuntimeException("full path should not be empty or null");
        }
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        StringBuilder sb = null;
        try {
            fis = new FileInputStream(fullPath);
            bis = new BufferedInputStream(fis);
            sb = new StringBuilder();
            int length;
            byte[] bytes = new byte[1024];
            while((length = bis.read(bytes)) != -1) {
                String s = new String(bytes, 0, length, charset);
                sb.append(s);
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        } finally {
            closeStream(fis, bis);
        }
        return sb == null ? null : sb.toString();
    }

    /**
     * 通过缓冲流读取文件
     * @param fis 文件输入流
     * @param charset 字符集
     * @return 文件内容
     */
    public static String readByBuffer(FileInputStream fis, Charset charset) {
        if (fis == null) {
            throw new RuntimeException("FileInputStream should not be null");
        }
        BufferedInputStream bis = null;
        StringBuilder sb = null;
        try {
            bis = new BufferedInputStream(fis);
            sb = new StringBuilder();
            int length;
            byte[] bytes = new byte[1024];
            while((length = bis.read(bytes)) != -1) {
                String s = new String(bytes, 0, length, charset);
                sb.append(s);
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        } finally {
            closeStream(fis, bis);
        }
        return sb.toString();
    }

    /**
     * 通过缓冲流读取文件
     * @param fis 文件输入流
     * @param charset 字符集
     * @return 文件内容
     */
    public static String readByBufferReader(FileInputStream fis, Charset charset) {
        if (fis == null) {
            throw new RuntimeException("FileInputStream should not be null");
        }
        StringBuilder sb = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(fis, charset);
            sb = new StringBuilder();
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine())!= null) {
                sb.append(line);
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        } finally {
            closeStreamReader(fis, br, isr);
        }
        return sb.toString();
    }

    /**
     * 通过IO读取文件
     * @param fullPath 全路径
     * @param charset 字符集
     * @return 文件内容
     */
    public static String readByIO(String fullPath, Charset charset) {
        if (StringUtils.isEmpty(fullPath)) {
            throw new RuntimeException("full path should not be empty or null");
        }
        FileInputStream fis = null;
        StringBuilder sb = null;
        try {
            fis = new FileInputStream(fullPath);
            byte[] bytes = new byte[1024];
            sb = new StringBuilder();
            int length;
            while ((length = fis.read(bytes)) != -1) {
                String s = new String(bytes, 0, length, charset);
                sb.append(s);
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        } finally {
            closeStream(fis, null);
        }
        return sb == null ? null : sb.toString();
    }

    /**
     * 通过IO读取文件
     * @param fis 文件输入流
     * @param charset 字符集
     * @return 文件内容
     */
    public static String readByIO(FileInputStream fis, Charset charset) {
        if (fis == null) {
            throw new RuntimeException("FileInputStream should not be null");
        }
        StringBuilder sb = null;
        try {
            byte[] bytes = new byte[1024];
            sb = new StringBuilder();
            int length;
            while ((length = fis.read(bytes)) != -1) {
                String s = new String(bytes, 0, length, charset);
                sb.append(s);
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        } finally {
            closeStream(fis, null);
        }
        return sb.toString();
    }

    /**
     * excel读取
     * @param fullPath 全路径（含文件名称）
     * @return 二维list
     * @throws Exception 解析异常
     */
    public static ArrayList<List> readExcel(String fullPath) throws Exception {

        if (StringUtils.isEmpty(fullPath)) {
            throw new RuntimeException("full path should not be empty or null");
        }

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
            throw new BusinessException("文件类型错误");
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

    /**
     * excel读取
     * @param fis 全路径（含文件名称）
     * @return 二维list
     * @throws Exception 解析异常
     */
    public static ArrayList<List> readExcel(FileInputStream fis, ExcelType type) throws Exception {

        if (fis == null) {
            throw new RuntimeException("FileInputStream should not be null");
        }

        ArrayList<List> result = new ArrayList<>();
        Workbook workbook;
        Row row;
        Cell cell;

        if (type == ExcelType.XLSX) {
            workbook = new XSSFWorkbook(fis);
        } else if (type == ExcelType.XLS) {
            workbook = new HSSFWorkbook(fis);
        } else {
            throw new BusinessException("文件类型错误");
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
                    } else {
                        rowData.add(null);
                    }
                }
                result.add(rowData);
            }
        }
        fis.close();
        return result;
    }

    /**
     * CSV解析
     * @param fullPath 全路径（含文件名称）
     * @param ignoreEmptyCell 是否忽略空单元格
     * @param empty2null 是否将空字符串转为null, ignoreEmptyCell == true 时，该字段无效
     * @param charset 字符编码，推荐gb2312，否则乱码
     * @return 二维list
     * @throws Exception 解析异常
     */
    public static ArrayList<List<String>> readCsv(String fullPath, boolean ignoreEmptyCell, boolean empty2null, Charset charset)
            throws Exception {

        if (StringUtils.isEmpty(fullPath)) {
            throw new RuntimeException("full path should not be empty or null");
        }

        final char SEPARATOR = ',';

        final String CSV = "csv";

        String fileType = fullPath.substring(fullPath.lastIndexOf(".") + 1).toLowerCase();
        if (!fileType.equalsIgnoreCase(CSV)) {
            throw new BusinessException("文件类型错误");
        }
        CsvReader csvReader = new CsvReader(fullPath, SEPARATOR, charset);
        ArrayList<List<String>> result = new ArrayList<>();
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
            if (!ignoreEmptyCell) {
                String[] rowArray = csvReader.getValues();
                if (empty2null) {
                    result.add(Arrays.stream(rowArray).map(e->e.isEmpty() ? null : e).collect(Collectors.toList()));
                } else {
                    result.add(Arrays.asList(rowArray));
                }
            } else {
                List<String> collect = Arrays.stream(csvReader.getValues()).filter(
                        (e) -> !e.isEmpty()).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    result.add(collect);
                }
            }
        }
        csvReader.close();
        return result;
    }

    /**
     * CSV解析
     * @param fis 文件输入流
     * @param ignoreEmptyCell 是否忽略空单元格
     * @param empty2null 是否将空字符串转为null, ignoreEmptyCell == true 时，该字段无效
     * @param charset 字符编码，推荐gb2312，否则乱码
     * @return 二维list
     * @throws Exception 解析异常
     */
    public static ArrayList<List<String>> readCsv(FileInputStream fis, boolean ignoreEmptyCell, boolean empty2null, Charset charset)
            throws Exception {

        if (fis == null) {
            throw new RuntimeException("FileInputStream should not be null");
        }

        final char SEPARATOR = ',';

        CsvReader csvReader = new CsvReader(fis, SEPARATOR, charset);
        ArrayList<List<String>> result = new ArrayList<>();
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
            if (!ignoreEmptyCell) {
                String[] rowArray = csvReader.getValues();
                if (empty2null) {
                    result.add(Arrays.stream(rowArray).map(e->e.isEmpty() ? null : e).collect(Collectors.toList()));
                } else {
                    result.add(Arrays.asList(rowArray));
                }
            } else {
                List<String> collect = Arrays.stream(csvReader.getValues()).filter(
                        (e) -> !e.isEmpty()).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    result.add(collect);
                }
            }
        }
        csvReader.close();
        return result;
    }

    /**
     * 文件下载
     * @param fullPath 待下载文件全路径
     * @param charset 编码
     * @param response HttpServletResponse
     */
    public static void download(String fullPath, Charset charset, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        }
        String s = readByNIO(fullPath, charset);
        try {
            if (outputStream != null && s != null) {
                outputStream.write(s.getBytes(charset));
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件下载
     * @param fileContentString 文件内容
     * @param charset 编码
     * @param response HttpServletResponse
     */
    public static void downloadString(String fileContentString, Charset charset, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        }
        try {
            if (outputStream != null) {
                outputStream.write(fileContentString.getBytes(charset));
            }
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            e.printStackTrace();
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭流
     * @param fis FileInputStream
     * @param bis BufferedInputStream
     */
    private static void closeStream(FileInputStream fis, BufferedInputStream bis) {
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭流
     * @param fis FileInputStream
     * @param br BufferedReader
     */
    private static void closeStreamReader(FileInputStream fis, BufferedReader br, InputStreamReader isr) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isr != null) {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
