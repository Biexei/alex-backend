package org.alex.platform.util;

import org.alex.platform.enums.WhiteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class UploadUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UploadUtil.class);

    private final static String[] IMPORT_CASE= {"xls", "xlsx", "json", "csv", "yaml"};

    private final static String CSV = "csv";

    private final static String JSON = "json";

    private final static String[] EXCEL = {"xls", "xlsx"};

    private final static String[] IMAGE = {"jpg", "jpeg", "gif", "png", "raw"};

    private final static String YAML = "yaml";

    /**
     * 文件上传
     * @param file file
     * @param filePath 路径
     * @param fileName 文件名称，可为空。为空时获取源文件名称
     * @param whiteList 白名单类型
     * @return 成功or失败
     */
    public static boolean upload(MultipartFile file, String filePath, String fileName, WhiteList whiteList) {
        if (fileName == null || fileName.isEmpty()) {
            fileName = file.getOriginalFilename();
        }
        if (fileName == null) {
            return false;
        }
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (whiteList == WhiteList.IMPORT_CASE) {
            if (!Arrays.asList(IMPORT_CASE).contains(fileType)) {
                return false;
            }
        } else if (whiteList == WhiteList.CSV) {
            if (!CSV.equals(fileType)) {
                return false;
            }
        } else if (whiteList == WhiteList.JSON) {
            if (!JSON.equals(fileType)) {
                return false;
            }
        } else if (whiteList == WhiteList.EXCEL) {
            if (!Arrays.asList(EXCEL).contains(fileType)) {
                return false;
            }
        } else if (whiteList == WhiteList.YAML) {
            if (!YAML.equals(fileType)) {
                return false;
            }
        } else if (whiteList == WhiteList.IMAGE) {
            if (!Arrays.asList(IMAGE).contains(fileType)) {
                return false;
            }
        } else {
            return false;
        }
        String fullPath = filePath + fileName;
        File f = new File(fullPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            file.transferTo(f);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error(ExceptionUtil.msg(e));
            return false;
        }
        return true;
    }
}
