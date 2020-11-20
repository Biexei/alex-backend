package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.InterfaceCaseDTO;
import org.alex.platform.pojo.InterfaceCaseListDTO;
import org.alex.platform.pojo.param.ExecuteInterfaceCaseParam;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.util.NoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class InterfaceCaseController {
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;
    @Autowired
    LoginUserInfo loginUserInfo;

    /**
     * 插入接口测试用例
     *
     * @param interfaceCaseDTO interfaceCaseDTO
     * @return Result
     * @throws BusinessException 字段校验
     */
    @PostMapping("/interface/case/save")
    public Result saveInterfaceCase(@RequestBody @Validated InterfaceCaseDTO interfaceCaseDTO) throws BusinessException {
        interfaceCaseService.saveInterfaceCaseAndAssertAndPostProcessor(interfaceCaseDTO);
        return Result.success("新增成功");
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
        HashMap<String, Object> map = (HashMap)loginUserInfo.getLoginUserInfo(request);
        String executor = null;
        try {
            executor = map.get("realName").toString();
        } catch (Exception e) {
            executor = "";
        }
        Integer executeLog = interfaceCaseService.executeInterfaceCase(new ExecuteInterfaceCaseParam(caseId, executor,
                null, NoUtil.genChainNo(), null, (byte) 1,
                null, null, null, null, (byte)0));
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
