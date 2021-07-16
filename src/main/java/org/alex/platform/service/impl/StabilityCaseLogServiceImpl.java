package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.StabilityCaseLogMapper;
import org.alex.platform.pojo.StabilityCaseLogDO;
import org.alex.platform.pojo.StabilityCaseLogDTO;
import org.alex.platform.pojo.StabilityCaseLogVO;
import org.alex.platform.service.StabilityCaseLogService;
import org.alex.platform.service.StabilityCaseService;
import org.alex.platform.util.RedisUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class StabilityCaseLogServiceImpl implements StabilityCaseLogService {

    @Autowired
    StabilityCaseLogMapper stabilityCaseLogMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    StabilityCaseService stabilityCaseService;

    @Override
    public Integer saveStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO) {
        return stabilityCaseLogMapper.insertStabilityCaseLog(stabilityCaseLogDO);
    }

    @Override
    public void modifyStabilityCaseLog(StabilityCaseLogDO stabilityCaseLogDO) {
        stabilityCaseLogMapper.updateStabilityCaseLog(stabilityCaseLogDO);
    }

    @Override
    public StabilityCaseLogVO findStabilityCaseLogById(Integer id) {
        return stabilityCaseLogMapper.selectStabilityCaseLogById(id);
    }

    @Override
    public StabilityCaseLogVO findStabilityCaseLogByNo(String no) {
        return stabilityCaseLogMapper.selectStabilityCaseLogByNo(no);
    }

    @Override
    public PageInfo<StabilityCaseLogVO> findStabilityCaseLogByStabilityCaseId(Integer caseId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(stabilityCaseLogMapper.selectStabilityCaseLogByStabilityCaseId(caseId));
    }

    @Override
    public PageInfo<StabilityCaseLogVO> findStabilityCaseLogList(StabilityCaseLogDTO stabilityCaseLogDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(stabilityCaseLogMapper.selectStabilityCaseLogList(stabilityCaseLogDTO));
    }

    @Override
    public Integer countExecuting() {
        return stabilityCaseLogMapper.countExecuting();
    }

    @Override
    public Integer countExecutingByCaseId(Integer stabilityTestId) {
        return stabilityCaseLogMapper.countExecutingByCaseId(stabilityTestId);
    }

    @Override
    public void removeStabilityCaseLogById(Integer id) throws BusinessException {
        // 仅停止、完成状态可删除
        StabilityCaseLogVO log = this.findStabilityCaseLogById(id);
        if (log != null) {
            Byte status = log.getStatus();
            String logPath = log.getLogPath();
            if (status == 1 || status == 2) {
                // 删除日志文件
                File file = new File(logPath);
                try {
                    FileUtils.forceDelete(file);
                } catch (IOException e) {
                    throw new BusinessException("程序文件被占用，请稍后重试");
                }
                // 删除记录
                stabilityCaseLogMapper.deleteStabilityCaseLogById(id);
            } else {
                throw new BusinessException("该任务正在运行");
            }
        }
    }


    /**
     * 响应时间报表
     * @param stabilityTestLogId stabilityTestLogId
     */
    @Override
    public JSONArray chartResponseTime(Integer stabilityTestLogId) {
        JSONArray result = new JSONArray();
        StabilityCaseLogVO log = this.findStabilityCaseLogById(stabilityTestLogId);
        String responseTimeQueue = log.getResponseTimeQueue();
        JSONArray array = JSONArray.parseArray(responseTimeQueue);
        if (array != null && !array.isEmpty()) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject var1 = new JSONObject();
                var1.put("Loop", String.valueOf(i+1)); // 如果是数值型前端会无法显示第一个点
                var1.put("Time", array.getLongValue(i));
                result.add(var1);
            }
        }
        return result;
    }
}
