package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.enums.*;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.generator.Main;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDO;
import org.alex.platform.service.ImportCaseService;
import org.alex.platform.service.InterfaceCaseImportService;
import org.alex.platform.service.InterfaceCaseSuiteService;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.alex.platform.util.ExceptionUtil;
import org.alex.platform.util.FileUtil;
import org.alex.platform.util.NoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InterfaceCaseImportServiceImpl implements InterfaceCaseImportService {
    @Autowired
    ImportCaseService importCaseService;
    @Autowired
    LoginUserInfo userInfo;
    @Autowired
    InterfaceCaseSuiteService interfaceCaseSuiteService;
    @Autowired
    InterfaceSuiteCaseRefService interfaceSuiteCaseRefService;
    @Autowired
    Main main;

    private static final Logger LOG = LoggerFactory.getLogger(InterfaceCaseImportServiceImpl.class);

    @Override
    @SuppressWarnings({"rawtypes"})
    public HashMap<String, Integer> importCase(MultipartFile file,
                                               Integer projectId, Integer moduleId,
                                               Integer requestImportType, Integer suiteId,
                                               HttpServletRequest request) throws BusinessException {
        String creator = userInfo.getRealName(request);
        String importNum = NoUtil.genIfImportNo();

        int totalNum = 0;
        int successNum = 0;
        int failedNum = 0;

        InputStream is;
        FileInputStream fis;

        LOG.info("开始文件导入流程, 文件导入批次={}", importNum);

        try {
            is= file.getInputStream();
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            throw new BusinessException("文件上传异常");
        }
        if (is instanceof FileInputStream) {
            fis = (FileInputStream)is;
        } else {
            throw new BusinessException("文件解析异常");
        }
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new BusinessException("文件解析异常");
        }
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        if ((type.equalsIgnoreCase("xls") || type.equalsIgnoreCase("xlsx")) && requestImportType == 1 ) {
            LOG.info("导入方式：excel");
            ArrayList<List> result;
            try {
                if (type.equalsIgnoreCase("xls")) {
                    result = FileUtil.readExcel(fis, ExcelType.XLS);
                } else {
                    result = FileUtil.readExcel(fis, ExcelType.XLSX);
                }
            } catch (Exception e) {
                LOG.error(ExceptionUtil.msg(e));
                throw new BusinessException("excel文件读取异常");
            }
            totalNum = result.size();
            LOG.info("总行数：{}", totalNum);
            LOG.info("开始按行读取并新增");
            for (int i = 0; i < result.size(); i++) {
                try {
                    Integer caseId = importCaseService.insertCaseByOffice(result.get(i), projectId, moduleId, creator, (byte) 1, importNum);
                    successNum++;
                    addCase2Suite(caseId, suiteId);
                    LOG.info("新增成功，当前索引值：{}，用例编号：{}", i, caseId);
                } catch (Exception e) {
                    failedNum++;
                    LOG.error("新增失败，当前索引值：{}", i);
                    LOG.error(ExceptionUtil.msg(e));
                }
            }
        } else if (type.equalsIgnoreCase("csv") && requestImportType == 2 ) {
            LOG.info("导入方式：csv");
            ArrayList<List<String>> result;
            try {
                result = FileUtil.readCsv(fis, false, true, Charset.forName("gb2312"));
            } catch (Exception e) {
                LOG.error(ExceptionUtil.msg(e));
                throw new BusinessException("csv文件读取异常");
            }
            totalNum = result.size();
            LOG.info("总行数：{}", totalNum);
            LOG.info("开始按行读取并新增");
            for (int i = 0; i < result.size(); i++) {
                try {
                    Integer caseId = importCaseService.insertCaseByOffice(result.get(i), projectId, moduleId, creator, (byte) 2, importNum);
                    successNum++;
                    addCase2Suite(caseId, suiteId);
                    LOG.info("新增成功，当前索引值：{}，用例编号：{}", i, caseId);
                } catch (Exception e) {
                    failedNum++;
                    LOG.error("新增失败，当前索引值：{}", i);
                    LOG.error(ExceptionUtil.msg(e));
                }
            }
        } else if (type.equalsIgnoreCase("json") && requestImportType == 3 ) {
            LOG.info("导入方式：json");
            JSONArray caseArray;
            String fileContent = FileUtil.readByBufferReader(fis, StandardCharsets.UTF_8);
            try {
                caseArray = JSON.parseArray(fileContent);
            } catch (Exception e) {
                LOG.error(ExceptionUtil.msg(e));
                throw new BusinessException("json文件读取异常");
            }
            totalNum = caseArray.size();
            LOG.info("总行数：{}", totalNum);
            LOG.info("开始解析json对象并新增");
            for (int i = 0; i < caseArray.size(); i++) {
                try {
                    Integer caseId = importCaseService.insertCaseByJsonYaml(caseArray.getJSONObject(i), projectId, moduleId, creator, (byte) 3, importNum);
                    successNum++;
                    addCase2Suite(caseId, suiteId);
                    LOG.info("新增成功，当前索引值：{}，用例编号：{}", i, caseId);
                } catch (Exception e) {
                    failedNum++;
                    LOG.error("新增失败，当前索引值：{}", i);
                    LOG.error(ExceptionUtil.msg(e));
                }
            }
        } else if (type.equalsIgnoreCase("yaml")  && requestImportType == 4 ) {
            LOG.info("导入方式：yaml");
            JSONArray caseArray;
            Yaml yaml = new Yaml();
            try {
                ArrayList fileContent = yaml.loadAs(is, ArrayList.class);
                caseArray = JSONArray.parseArray(JSON.toJSONString(fileContent));
            } catch (Exception e) {
                LOG.error(ExceptionUtil.msg(e));
                throw new BusinessException("yaml文件读取异常");
            }
            totalNum = caseArray.size();
            LOG.info("总行数：{}", totalNum);
            LOG.info("开始解析yaml对象并新增");
            for (int i = 0; i < caseArray.size(); i++) {
                try {
                    Integer caseId = importCaseService.insertCaseByJsonYaml(caseArray.getJSONObject(i), projectId, moduleId, creator, (byte) 4, importNum);
                    successNum++;
                    addCase2Suite(caseId, suiteId);
                    LOG.info("新增成功，当前索引值：{}，用例编号：{}", i, caseId);
                } catch (Exception e) {
                    failedNum++;
                    LOG.error("新增失败，当前索引值：{}", i);
                    LOG.error(ExceptionUtil.msg(e));
                }
            }
        } else if (type.equalsIgnoreCase("har") && requestImportType == 5 ) {
            LOG.info("导入方式：har");
            JSONArray entries;
            String fileContent = FileUtil.readByBufferReader(fis, StandardCharsets.UTF_8);
            try {
                entries = JSON.parseObject(fileContent).getJSONObject("log").getJSONArray("entries");
            } catch (Exception e) {
                LOG.error(ExceptionUtil.msg(e));
                throw new BusinessException("har文件读取异常");
            }
            totalNum = entries.size();
            LOG.info("总行数：{}", totalNum);
            LOG.info("开始解析json对象并新增");
            for (int i = 0; i < entries.size(); i++) {
                try {
                    Integer caseId = importCaseService.insertCaseByHar(entries.getJSONObject(i), projectId, moduleId, creator, importNum);
                    successNum++;
                    addCase2Suite(caseId, suiteId);
                    LOG.info("新增成功，当前索引值：{}，用例编号：{}", i, caseId);
                } catch (Exception e) {
                    failedNum++;
                    LOG.error("新增失败，当前索引值：{}", i);
                    LOG.error(ExceptionUtil.msg(e));
                }
            }
        } else {
            throw new BusinessException("不支持此类型文件/文件类型不匹配");
        }
        LOG.info("用例导入流程完成，总记录：{}，成功：{}，失败：{}", totalNum, successNum, failedNum);
        return importResult(totalNum, successNum, failedNum);
    }

    @Override
    public JSONArray generatorInterfaceCase(MultipartFile file, CaseRule caseRule, HttpServletResponse response,
                                            boolean isReturnMix, Integer dataType) throws Exception {

        InputStream is;
        FileInputStream fis;
        LOG.info("开始批量生成测试用例流程");
        try {
            is= file.getInputStream();
        } catch (IOException e) {
            LOG.error(ExceptionUtil.msg(e));
            throw new BusinessException("文件上传异常");
        }
        if (is instanceof FileInputStream) {
            fis = (FileInputStream)is;
        } else {
            throw new BusinessException("文件解析异常");
        }
        String fileContent = FileUtil.readByBufferReader(fis, StandardCharsets.UTF_8);
        JSONObject fileContentObj = JSONObject.parseObject(fileContent);
        JSONArray resultArray = main.generateCase(fileContentObj, caseRule, isReturnMix, dataType);
        String resultString = JSON.toJSONString(resultArray, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat);
        LOG.info("批量生成用例共" + resultArray.size() + "条");
        LOG.info("-----------生成的用例为-----------");
        LOG.info(resultString);
        LOG.info("-----------生成的用例为-----------");
        return resultArray;
    }

    /**
     * 导入结果封装
     * @param totalNum 总数
     * @param successNum 成功数
     * @param failedNum 失败数
     * @return HashMap
     */
    private HashMap<String, Integer> importResult(Integer totalNum, Integer successNum, Integer failedNum){
        HashMap<String, Integer> result = new HashMap<>();
        result.put("total", totalNum);
        result.put("success", successNum);
        result.put("failed", failedNum);
        return result;
    }

    /**
     * 将用例加入测试套件
     * @param caseId 测试用例编号
     * @param suiteId 测试套件编号
     */
    private void addCase2Suite(Integer caseId, Integer suiteId) {
        if (suiteId != null) {
            if (interfaceCaseSuiteService.findInterfaceCaseSuiteById(suiteId) != null) {
                InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO = new InterfaceSuiteCaseRefDO();
                interfaceSuiteCaseRefDO.setSuiteId(suiteId);
                interfaceSuiteCaseRefDO.setCaseId(caseId);
                interfaceSuiteCaseRefDO.setCaseStatus((byte)0);
                interfaceSuiteCaseRefDO.setOrder(1);
                interfaceSuiteCaseRefService.saveSuiteCaseSingle(interfaceSuiteCaseRefDO);
            }
        }
    }
}
