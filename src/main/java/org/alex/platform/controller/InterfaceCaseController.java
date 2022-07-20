package org.alex.platform.controller;

import com.alibaba.fastjson.JSONArray;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.enums.CaseRule;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceCaseDTO;
import org.alex.platform.pojo.InterfaceCaseListDTO;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.alex.platform.service.InterfaceCaseImportService;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.util.FileUtil;
import org.alex.platform.util.NoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
public class InterfaceCaseController {
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;
    @Autowired
    LoginUserInfo loginUserInfo;
    @Autowired
    InterfaceCaseImportService interfaceCaseImportService;
    @Value("${myself.path.readme}")
    private String readmeRoot;
    @Value("${myself.path.template}")
    private String templateRoot;

    /**
     * 插入接口测试用例
     *
     * @param interfaceCaseDTO interfaceCaseDTO
     * @return Result
     * @throws BusinessException 字段校验
     */
    @PostMapping("/interface/case/save")
    public Result saveInterfaceCase(@RequestBody @Validated InterfaceCaseDTO interfaceCaseDTO) throws BusinessException {
        interfaceCaseDTO.setSource((byte)0);
        interfaceCaseDTO.setImportNo(null);
        interfaceCaseService.saveInterfaceCaseAndAssertAndPostProcessorAndPreCase(interfaceCaseDTO);
        return Result.success("新增成功");
    }

    /**
     * 导入接口测试用例
     * @param file file
     * @param request request
     * @param suiteId suiteId
     * @return Result
     * @throws BusinessException 业务异常
     */
    @PostMapping("/interface/case/import")
    public Result importInterfaceCase(@RequestParam MultipartFile file, @RequestParam Integer type,
                                      @RequestParam Integer projectId, @RequestParam Integer moduleId,
                                      @RequestParam(required = false) Integer suiteId, HttpServletRequest request) throws BusinessException {
        if (projectId == null) {
            throw new BusinessException("请选择项目编号");
        }
        if (moduleId == null) {
            throw new BusinessException("请选择模块编号");
        }
        HashMap<String, Integer> result = interfaceCaseImportService.importCase(file, projectId, moduleId, type, suiteId, request);
        return Result.success(result);
    }

    /**
     * 上传约束文件自动生成测试用例并下载
     * @param file  约束文件
     * @param genRule  1正交法 2笛卡尔积 3仅生成单属性
     * @param dataType  1静态数据 2尽可能动态数据
     * @param response 文件下载输入
     * @throws BusinessException 业务异常
     */
    @PostMapping("/interface/case/generator")
    public Result generatorInterfaceCase(@RequestParam MultipartFile file, @RequestParam Integer genRule,
                                         @RequestParam Integer dataType, HttpServletResponse response) throws Exception {
        CaseRule caseRule = null;
        boolean isReturnMix = true;
        if (genRule == 1) {
            caseRule = CaseRule.ORT;
        } else if (genRule == 2) {
            caseRule = CaseRule.CARTESIAN;
        } else {
            isReturnMix = false;
        }
        JSONArray array = interfaceCaseImportService.generatorInterfaceCase(file, caseRule, response, isReturnMix, dataType);
        return Result.success(String.format("成功生成%s条用例", array.size()), array);
    }

    /**
     * 接口用例模版下载
     * @param type 类型
     * @param response response
     */
    @GetMapping("/interface/template/download/{type}")
    public void downloadTemplate(@PathVariable Integer type, HttpServletResponse response) {
        // String basePath = "src\\main\\resources\\template\\";
        String basePath = templateRoot;
        String excel = "interface_case_template.xlsx";
        String json = "interface_case_template.json";
        String csv = "interface_case_template.csv";
        String yaml = "interface_case_template.yaml";
        String caseGeneratorTemplate = "用户注册接口自动生成用例约束示例.json";

        // String readMeBasePath = "src\\main\\resources\\readme\\";
        String readMeBasePath = readmeRoot;
        String caseGeneratorIntroduce = "用例生成介绍.md";
        if (type == 1) {
            response.setHeader("Content-Disposition", "attachment;filename=" + excel);
            FileUtil.download(basePath + excel, StandardCharsets.ISO_8859_1, response);
        } else if (type == 2) {
            response.setHeader("Content-Disposition", "attachment;filename=" + csv);
            FileUtil.download(basePath + csv, StandardCharsets.ISO_8859_1, response);
        } else if (type == 3) {
            response.setHeader("Content-Disposition", "attachment;filename=" + json);
            FileUtil.download(basePath + json, StandardCharsets.UTF_8, response);
        } else if (type == 4) {
            response.setHeader("Content-Disposition", "attachment;filename=" + yaml);
            FileUtil.download(basePath + yaml, StandardCharsets.UTF_8, response);
        } else if (type == 10) {
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String(caseGeneratorTemplate.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            FileUtil.download(basePath + caseGeneratorTemplate, StandardCharsets.UTF_8, response);
        } else if (type == 11) {
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    new String(caseGeneratorIntroduce.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            FileUtil.download(readMeBasePath + caseGeneratorIntroduce, StandardCharsets.UTF_8, response);
        }
    }

    /**
     * 修改接口测试用例
     *
     * @param interfaceCaseDTO interfaceCaseDTO
     * @return Result
     * @throws BusinessException 字段校验
     */
    @PostMapping("/interface/case/modify")
    public Result modifyInterfaceCase(@RequestBody @Validated InterfaceCaseDTO interfaceCaseDTO) throws BusinessException {
        // 由于是动态sql，手动置为null，防止导入方式被篡改
        interfaceCaseDTO.setSource(null);
        interfaceCaseDTO.setImportNo(null);
        interfaceCaseService.modifyInterfaceCase(interfaceCaseDTO);
        return Result.success("修改成功");
    }

    /**
     * 删除接口测试用例
     *
     * @param interfaceCaseId 测试用例编号
     * @return Result
     */
    @GetMapping("/interface/case/remove/{interfaceCaseId}")
    public Result removeInterfaceCase(@PathVariable Integer interfaceCaseId) throws BusinessException {
        interfaceCaseService.removeInterfaceCase(interfaceCaseId);
        return Result.success("删除成功");
    }

    /**
     * 懒加载树型展示用例列表
     *
     * @param level 树层级0代表project级别、1代表module级别、2代表case级别
     * @param id level为0时，不接受该参数，默认返回全部项目列表
     *           level为1时，id为项目编号，返回该项目下所有模块
     *           level为2时，id为模块编号，返回该模块下所有用例
     * @return Result
     */
    @GetMapping("/interface/case/tree")
    public Result caseTree(Integer level, Integer id) {
        return Result.success(interfaceCaseService.caseTree(level, id));
    }

    /**
     * 获取接口测试用例列表
     *
     * @param interfaceCaseListDTO interfaceCaseListDTO
     * @return Result
     */
    @GetMapping("/interface/case/list")
    public Result listInterfaceCase(InterfaceCaseListDTO interfaceCaseListDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(interfaceCaseService.findInterfaceCaseList(interfaceCaseListDTO, num, size));
    }

    /**
     * 查看接口测试用例详情
     *
     * @param caseId 用例编号
     * @return Result
     */
    @GetMapping("/interface/case/info/{caseId}")
    public Result findInterfaceCaseByCaseId(@PathVariable Integer caseId) {
        return Result.success(interfaceCaseService.findInterfaceCaseByCaseId(caseId));
    }

    /**
     * 执行接口测试用例
     *
     * @param caseId 接口用例编号
     * @return Result
     */
    @GetMapping("/interface/case/execute/{caseId}")
    public Result executeInterfaceCase(@PathVariable Integer caseId, HttpServletRequest request) {
        String executor = loginUserInfo.getRealName(request);
        try {
            Integer executeLog = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId, executor,
                    null, NoUtil.genChainNo(), null, (byte) 1,
                    null, null, null, null, (byte)0, null, false));
            Byte status = executeLogService.findExecute(executeLog).getStatus();
            if (status == 0) {
                return Result.success("执行成功");
            } else if (status == 1) {
                return Result.fail("执行失败");
            } else {
                return Result.fail("执行错误");
            }
        } catch (BusinessException e) {
            return Result.fail("执行错误");
        }
    }
}
