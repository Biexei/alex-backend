package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.StabilityCaseLogDTO;
import org.alex.platform.pojo.StabilityCaseLogVO;
import org.alex.platform.service.StabilityCaseLogService;
import org.alex.platform.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


@RestController
public class StabilityCaseLogController {
    @Autowired
    StabilityCaseLogService stabilityCaseLogService;

    /**
     * 查看稳定性测试用例列表
     * @param stabilityCaseLogDTO stabilityCaseLogDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return Result
     */
    @GetMapping("/stability/log/list")
    Result findStabilityCaseLogList(StabilityCaseLogDTO stabilityCaseLogDTO, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return Result.success(stabilityCaseLogService.findStabilityCaseLogList(stabilityCaseLogDTO, pageNum, pageSize));
    }

    /**
     * 下载稳定性测试用例
     * @param response response
     */
    @GetMapping("/stability/log/download/{logId}")
    public void downloadStabilityLog(@PathVariable Integer logId, HttpServletResponse response) {
        StabilityCaseLogVO log = stabilityCaseLogService.findStabilityCaseLogById(logId);
        if (log != null) {
            String logPath = log.getLogPath();
            String logFileName = logPath.substring(logPath.lastIndexOf("\\") + 1);
            response.setHeader("Content-Disposition", "attachment;filename=" + logFileName);
            FileUtil.download(log.getLogPath(), StandardCharsets.ISO_8859_1, response);
        }
    }
}
