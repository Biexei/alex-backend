package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.InterfaceCaseSuiteDO;
import org.alex.platform.pojo.InterfaceCaseSuiteDTO;
import org.alex.platform.pojo.InterfaceSuiteInfoDTO;
import org.alex.platform.service.InterfaceCaseSuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@RestController
public class InterfaceCaseSuiteController {
    @Autowired
    InterfaceCaseSuiteService interfaceCaseSuiteService;
    @Autowired
    LoginUserInfo loginUserInfo;

    /**
     * 新增测试套件
     *
     * @param interfaceSuiteInfoDTO interfaceSuiteInfoDTO
     * @return Result
     */
    @PostMapping("/interface/suite/save")
    public Result saveInterfaceCaseSuiteAndProcessor(@RequestBody @Validated InterfaceSuiteInfoDTO interfaceSuiteInfoDTO) throws ValidException {
        Date date = new Date();
        interfaceSuiteInfoDTO.setCreatedTime(date);
        interfaceSuiteInfoDTO.setUpdateTime(date);
        interfaceCaseSuiteService.saveInterfaceCaseSuiteAndProcessor(interfaceSuiteInfoDTO);
        return Result.success("新增成功");
    }

    /**
     * 修改测试套件
     *
     * @param interfaceSuiteInfoDTO interfaceSuiteInfoDTO
     * @return Result
     */
    @PostMapping("/interface/suite/modify")
    public Result modifyInterfaceCaseSuite(@RequestBody @Validated InterfaceSuiteInfoDTO interfaceSuiteInfoDTO) throws ValidException {
        interfaceSuiteInfoDTO.setUpdateTime(new Date());
        interfaceCaseSuiteService.modifyInterfaceCaseSuite(interfaceSuiteInfoDTO);
        return Result.success("修改成功");
    }

    /**
     * 删除测试套件
     *
     * @param suiteId 测试套件编号
     * @return Result
     */
    @GetMapping("/interface/suite/remove/{suiteId}")
    public Result removeInterfaceCaseSuiteById(@PathVariable Integer suiteId) throws BusinessException {
        interfaceCaseSuiteService.removeInterfaceCaseSuiteById(suiteId);
        return Result.success("删除成功");
    }

    /**
     * 查看测试套件详情
     *
     * @param suiteId 测试套件编号
     * @return Result
     */
    @GetMapping("/interface/suite/{suiteId}")
    public Result findInterfaceCaseSuiteById(@PathVariable Integer suiteId) {
        return Result.success(interfaceCaseSuiteService.findInterfaceCaseSuiteInfoById(suiteId));
    }

    /**
     * 查看测试套件列表
     *
     * @param interfaceCaseSuiteDTO interfaceCaseSuiteDTO
     * @param pageNum               pageNum
     * @param pageSize              pageSize
     * @return Result
     */
    @GetMapping("/interface/suite")
    public Result findInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(interfaceCaseSuiteService.findInterfaceCaseSuite(interfaceCaseSuiteDTO, num, size));
    }

    /**
     * 查看测试套件列表，不分页
     *
     * @param interfaceCaseSuiteDTO interfaceCaseSuiteDTO
     * @return Result
     */
    @GetMapping("/interface/suiteAll")
    public Result findInterfaceCaseSuiteAll(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO) {
        return Result.success(interfaceCaseSuiteService.findInterfaceCaseSuiteAll(interfaceCaseSuiteDTO));
    }

    /**
     * 复制测试套件
     * @param suiteId 被复制测试套件编号
     * @param request 获取测试套件添加人
     * @return Result
     */
    @GetMapping("/interface/suite/copy/{suiteId}")
    public Result copyInterfaceCaseSuiteById(@PathVariable Integer suiteId, HttpServletRequest request) {
        String creator = loginUserInfo.getRealName(request);
        return Result.success(interfaceCaseSuiteService.copyInterfaceCaseSuiteById(suiteId, creator));
    }
}
