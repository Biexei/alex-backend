package org.alex.platform.util;

import org.alex.platform.enums.FileWhiteList;
import org.alex.platform.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class UploadUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UploadUtil.class);

    private final static String[] IMPORT_CASE = {"xls", "xlsx", "json", "csv", "yaml"};

    private final static String CSV = "csv";

    private final static String JSON = "json";

    private final static String[] EXCEL = {"xls", "xlsx"};

    private final static String[] IMAGE = {"jpg", "jpeg", "gif", "png", "raw"};

    private final static String YAML = "yaml";

    /**
     * 文件上传
     *
     * @param file          file
     * @param filePath      路径
     * @param fileName      文件名称，可为空。为空时获取源文件名称
     * @param fileWhiteList 白名单类型
     * @return 成功or失败
     */
    public static boolean upload(MultipartFile file, String filePath, String fileName, FileWhiteList fileWhiteList) {
        if (fileName == null || fileName.isEmpty()) {
            fileName = file.getOriginalFilename();
        }
        if (fileName == null) {
            return false;
        }
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (validateFileType(fileType, fileWhiteList)) {
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
        } else {
            return false;
        }
    }

    /**
     * 获取文件上传流
     * @param file 文件
     * @param fileWhiteList 白名单
     * @return 上传文件流
     * @throws Exception 解析异常/格式错误
     */
    public static FileInputStream getUploadFileStream(MultipartFile file, FileWhiteList fileWhiteList) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new BusinessException("文件解析异常");
        }
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (validateFileType(fileType, fileWhiteList)) {
            InputStream inputStream = file.getInputStream();
            if (inputStream instanceof FileInputStream) {
                return (FileInputStream)inputStream;
            } else {
                throw new BusinessException("文件解析异常");
            }
        } else {
            throw new BusinessException("文件格式错误");
        }
    }

    /**
     * 文件类型校验
     * @param fileType 文件类型
     * @param fileWhiteList 白名单类型
     * @return 是否校验通过
     */
    private static boolean validateFileType(String fileType, FileWhiteList fileWhiteList) {
        fileType = fileType.toLowerCase();
        if (fileWhiteList == FileWhiteList.IMPORT_CASE) {
            return Arrays.asList(IMPORT_CASE).contains(fileType);
        } else if (fileWhiteList == FileWhiteList.CSV) {
            return CSV.equals(fileType);
        } else if (fileWhiteList == FileWhiteList.JSON) {
            return JSON.equals(fileType);
        } else if (fileWhiteList == FileWhiteList.EXCEL) {
            return Arrays.asList(EXCEL).contains(fileType);
        } else if (fileWhiteList == FileWhiteList.YAML) {
            return YAML.equals(fileType);
        } else if (fileWhiteList == FileWhiteList.IMAGE) {
            return Arrays.asList(IMAGE).contains(fileType);
        } else {
            return false;
        }
    }
}