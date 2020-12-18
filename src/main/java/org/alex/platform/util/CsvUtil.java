package org.alex.platform.util;

import com.csvreader.CsvReader;
import org.alex.platform.exception.BusinessException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvUtil {
    private static final char SEPARATOR = ',';

    private final static String CSV = "csv";

    /**
     * CSV解析
     * @param fullPath 全路径（含文件名称）
     * @param ignoreEmptyRecord 是否忽略空记录
     * @param charset 字符编码，推荐gb2312，否则乱码
     * @return 二维list
     * @throws IOException 解析异常
     * @throws BusinessException 格式错误
     */
    public static ArrayList<List<String>> read(String fullPath, boolean ignoreEmptyRecord, Charset charset) throws IOException, BusinessException {
        String fileType = fullPath.substring(fullPath.lastIndexOf(".") + 1).toLowerCase();
        if (!fileType.equalsIgnoreCase(CSV)) {
            throw new BusinessException("格式错误");
        }
        CsvReader csvReader = new CsvReader(fullPath, SEPARATOR, charset);
        ArrayList<List<String>> result = new ArrayList<>();
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
            if (!ignoreEmptyRecord) {
                result.add(Arrays.asList(csvReader.getValues()));
            } else {
                List<String> collect = Arrays.stream(csvReader.getValues()).filter(
                        (e) -> !e.isEmpty()).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    result.add(collect);
                }
            }
        }
        return result;
    }
}
