package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
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
                                      @RequestParam Integer suiteId, HttpServletRequest request) throws BusinessException {
        HashMap<String, Integer> result = interfaceCaseImportService.importCase(file, type, suiteId, request);
        return Result.success(result);
    }

    /**
     * 接口用例模版下载
     * @param type 类型
     * @param response response
     */
    @GetMapping("/interface/template/download/{type}")
    public void downloadTemplate(@PathVariable Integer type, HttpServletResponse response) {
        String basePath = "src\\main\\resources\\template\\";
        String excel = "interface_case_template.xlsx";
        String json = "interface_case_template.json";
        String csv = "interface_case_template.csv";
        String yaml = "interface_case_template.yaml";
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
     * 获取接口测试用例列表
     *
     * @param interfaceCaseListDTO interfaceCaseListDTO
     * @param pageNum              pageNum
     * @param pageSize             pageSize
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
    public Result executeInterfaceCase(@PathVariable Integer caseId, HttpServletRequest request) throws BusinessException {
        String executor = loginUserInfo.getRealName(request);
        Integer executeLog = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId, executor,
                null, NoUtil.genChainNo(), null, (byte) 1,
                null, null, null, null, (byte)0, null));
        Byte status = executeLogService.findExecute(executeLog).getStatus();
        if (status == 0) {
            return Result.success("执行成功");
        } else if (status == 1) {
            return Result.fail("执行失败");
        } else {
            return Result.fail("执行错误");
        }
    }
}
