package org.alex.platform;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileTypeCount {
    int white = 0;
    int other = 0;
    LinkedList<String> whiteFileList = new LinkedList<>();
    LinkedList<String> otherFileList = new LinkedList<>();
    String[] whiteList = {"ts"};

    @Test
    public void scanFileType() {
        String path = "C:\\Users\\beix\\Desktop\\ceshijianguan";
        recursionScan(path);
        System.out.println(Arrays.toString(whiteList) + " count:" + white);
        System.out.println("other file count:" + other);
    }

    /**
     * 统计白名单文件数量
     * @param basePath 路径
     */
    public void recursionScan(String basePath) {
        List<String> whiteArray = Arrays.asList(whiteList);
        File file = new File(basePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (!f.isDirectory()) {
                        String fileName = f.getName();
                        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                        if (whiteArray.contains(fileType.toLowerCase())) {
                            whiteFileList.add(f.getAbsolutePath());
                            white++;
                        } else {
                            otherFileList.add(f.getAbsolutePath());
                            other++;
                        }
                    } else {
                        recursionScan(f.getAbsolutePath());
                    }
                }
            }
        }
    }












    @Test
    public void doTest() {
        String path = "C:\\Users\\beix\\Desktop\\ceshijianguan";
        String oldType = "ts";
        String newType = "js";
        typeConvert(path, oldType, newType);
    }

    /**
     * 文件格式转换
     * @param path 根路径，会递归遍历更改
     * @param oldType 原类型
     * @param newType 新类型
     */
    public void typeConvert(String path, String oldType, String newType) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (!f.isDirectory()) {
                        String fileName = f.getName();
                        String absPath = f.getAbsolutePath();
                        String asbPathWithoutType = absPath.substring(0, absPath.lastIndexOf("."));
                        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                        if (oldType.equals(fileType)) {
                            f.renameTo(new File(asbPathWithoutType + newType));
                        }
                    } else {
                        typeConvert(f.getAbsolutePath(), oldType, newType);
                    }
                }
            }
        }
    }
}
