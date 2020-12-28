package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDTO;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
public class InterfaceSuiteCaseRefController {
    @Autowired
    InterfaceSuiteCaseRefService refService;
    @Autowired
    LoginUserInfo loginUserInfo;

    /**
     * 测试套件新增测试用例
     *
     * @param interfaceSuiteCaseRefDOList interfaceSuiteCaseRefDOList
     * @return Result
     */
    @PostMapping("/interface/suite/case/save")
    public Result saveSuiteCase(@RequestBody List<InterfaceSuiteCaseRefDO> interfaceSuiteCaseRefDOList) {
        refService.saveSuiteCase(interfaceSuiteCaseRefDOList);
        return Result.success("新增成功");
    }

    /**
     * 测试套件修改测试用例
     *
     * @param interfaceSuiteCaseRefDO interfaceSuiteCaseRefDO
     * @return Result
     */
    @PostMapping("/interface/suite/case/modify")
    public Result modifySuiteCase(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO) {
        refService.modifySuiteCase(interfaceSuiteCaseRefDO);
        return Result.success("修改成功");
    }

    /**
     * 删除测试套件内的用例
     *
     * @param id 关联表主键
     * @return Result
     */
    @GetMapping("/interface/suite/case/remove/{id}")
    public Result removeSuiteCase(@PathVariable Integer id) {
        refService.removeSuiteCase(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除测试套件内的用例
     *
     * @param interfaceSuiteCaseRefDO interfaceSuiteCaseRefDO
     * @return Result
     */
    @PostMapping("/interface/suite/case/remove")
    public Result removeSuiteCaseByObject(InterfaceSuiteCaseRefDO interfaceSuiteCaseRefDO) {
        refService.removeSuiteCaseByObject(interfaceSuiteCaseRefDO);
        return Result.success("删除成功");
    }

    /**
     * 获取测试套件内的测试用例列表
     *
     * @param interfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO
     * @param pageNum                  pageNum
     * @param pageSize                 pageSize
     * @return Result
     */
    @GetMapping("/interface/suite/case")
    public Result findSuiteCaseList(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(refService.findSuiteCaseList(interfaceSuiteCaseRefDTO, num, size));
    }

    /**
     * 获取测试套件内的所有用例（不分页）
     *
     * @param interfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO
     * @return Result
     */
    @GetMapping("/interface/suite/case/all")
    public Result findAllSuiteCase(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO) {
        return Result.success(refService.findAllSuiteCase(interfaceSuiteCaseRefDTO));
    }

    /**
     * 执行测试套件
     *
     * @param suiteId 测试套件编号
     * @return Result
     * @throws BusinessException BusinessException
     */
    @GetMapping("/interface/suite/execute/{suiteId}")
    public Result executeSuiteCase(@PathVariable Integer suiteId, HttpServletRequest request) throws BusinessException {
        String executor = loginUserInfo.getRealName(request);
        refService.executeSuiteCaseById(suiteId, executor);
        return Result.success("执行成功");
    }

    /**
     * 执行测试套件中某一个用例
     * @param request request
     * @return Result
     * @throws BusinessException BusinessException
     */
    @GetMapping("interface/suite/execute-case")
    public Result executeCaseInSuite(@RequestParam Integer suiteId, @RequestParam Integer caseId, HttpServletRequest request) throws BusinessException {

        if (caseId == null || suiteId == null) {
            throw new BusinessException("参数错误");
        }

        String executor = loginUserInfo.getRealName(request);
        Byte status = refService.executeCaseInSuite(suiteId, caseId, executor);
        if (status == 0) {
            return Result.success("执行成功");
        } else if (status == 1) {
            return Result.fail("执行失败");
        } else {
            return Result.fail("执行错误");
        }
    }
}
