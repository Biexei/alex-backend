package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListDTO;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.alex.platform.util.NoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterfaceCaseExecuteLogController {
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;

    /**
     * 获取接口执行日志详情
     *
     * @param executeId 执行编号
     * @return Result
     */
    @GetMapping("/interface/log/{executeId}")
    public Result findInterfaceCaseExecuteLog(@PathVariable Integer executeId) {
        return Result.success(executeLogService.findExecute(executeId));
    }

    /**
     * 获取接口执行日志列表
     *
     * @param executeLogListDTO executeLogListDTO
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @return Result
     */
    @GetMapping("/interface/log/list")
    public Result findInterfaceCaseExecuteLogList(InterfaceCaseExecuteLogListDTO executeLogListDTO, Integer pageNum,
                                                  Integer pageSize) {
        Integer num = pageNum == null ? 1 : pageNum;
        Integer size = pageSize == null ? 10 : pageSize;
        return Result.success(executeLogService.findExecuteList(executeLogListDTO, num, size));
    }

    /**
     * 获取接口执行日志列表(测试报告专用)
     *
     * @param executeLogListDTO executeLogListDTO
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @return Result
     */
    @GetMapping("/interface/report/log/list")
    public Result findInterfaceCaseExecuteLogForReportList(InterfaceCaseExecuteLogListDTO executeLogListDTO, Integer pageNum,
                                                  Integer pageSize, Integer showDetail) throws BusinessException {
        if (executeLogListDTO.getSuiteLogNo() == null) {
            throw new BusinessException("SuiteLogNo参数错误");
        }
        boolean isShowDetail = false;
        if (showDetail != null) {
            isShowDetail = showDetail == 0;
        }
        // 筛选执行接口用例中间的依赖用例
        if (isShowDetail) {
            executeLogListDTO.setSuiteLogDetailNo(NoUtil.genSuiteLogDetailNo(executeLogListDTO.getSuiteLogNo()));
            executeLogListDTO.setSuiteLogNo(null);
        }
        Integer num = pageNum == null ? 1 : pageNum;
        Integer size = pageSize == null ? 10 : pageSize;
        return Result.success(executeLogService.findExecuteList(executeLogListDTO, num, size));
    }

    /**
     * 获取执行用例的调用链
     *
     * @param executeId executeId
     * @return Result
     */
    @GetMapping("/interface/log/chain/{executeId}")
    public Result caseExecuteLogChain(@PathVariable Integer executeId) {
        return Result.success(executeLogService.caseExecuteLogChain(executeId));
    }

    /**
     * 获取指定suiteLogNo的执行记录，不分页
     * @param suiteLogNo suiteLogNo
     * @return Result
     * @throws BusinessException 入参suiteLogNo必填
     */
    @GetMapping("/interface/log/list/chain/{suiteLogNo}")
    public Result findReportChain(@PathVariable String suiteLogNo) throws BusinessException {
        if (suiteLogNo == null) {
            throw new BusinessException("请求参数错误"); // 只准查指定suiteLogNo,该接口主要用于前端测试报告用例调用链tab页面展示
        }
        InterfaceCaseExecuteLogListDTO dto = new InterfaceCaseExecuteLogListDTO();
        dto.setSuiteLogNo(suiteLogNo);
        return Result.success(executeLogService.findExecuteListAll(dto));
    }
}
