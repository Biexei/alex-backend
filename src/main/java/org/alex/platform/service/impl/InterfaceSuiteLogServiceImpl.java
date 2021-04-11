package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.InterfaceAssertLogMapper;
import org.alex.platform.mapper.InterfaceSuiteLogMapper;
import org.alex.platform.pojo.InterfaceSuiteLogDO;
import org.alex.platform.pojo.InterfaceSuiteLogDTO;
import org.alex.platform.pojo.InterfaceSuiteLogVO;
import org.alex.platform.pojo.InterfaceSuiteSummaryDTO;
import org.alex.platform.service.InterfaceSuiteLogService;
import org.alex.platform.util.NoUtil;
import org.alex.platform.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterfaceSuiteLogServiceImpl implements InterfaceSuiteLogService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    InterfaceSuiteLogMapper interfaceSuiteLogMapper;
    @Autowired
    InterfaceAssertLogMapper interfaceAssertLogMapper;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceSuiteLogServiceImpl.class);

    /**
     * 查询测试套件执行日志列表
     *
     * @param interfaceSuiteLogDTO interfaceSuiteLogDTO
     * @param pageNum              pageNum
     * @param pageSize             pageSize
     * @return PageInfo
     */
    @Override
    public PageInfo<InterfaceSuiteLogVO> findIfSuiteLog(InterfaceSuiteLogDTO interfaceSuiteLogDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(interfaceSuiteLogMapper.selectIfSuiteLog(interfaceSuiteLogDTO));
    }

    /**
     * 根据测试套件执行日志编号查询
     *
     * @param suiteLogNo suiteLogNo
     * @return InterfaceSuiteLogVO
     */
    @Override
    public InterfaceSuiteLogVO findIfSuiteLogByNo(String suiteLogNo) {
        return interfaceSuiteLogMapper.selectIfSuiteLogByNo(suiteLogNo);
    }

    /**
     * 根据测试套件执行日志ID查询
     *
     * @param id id
     * @return InterfaceSuiteLogVO
     */
    @Override
    public InterfaceSuiteLogVO findIfSuiteLogById(Integer id) {
        return interfaceSuiteLogMapper.selectIfSuiteLogById(id);
    }

    /**
     * 新增测试套件执行日志
     *
     * @param interfaceSuiteLogDO interfaceSuiteLogDO
     * @return InterfaceSuiteLogDO
     */
    @Override
    public InterfaceSuiteLogDO saveIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO) {
        interfaceSuiteLogMapper.insertIfSuiteLog(interfaceSuiteLogDO);
        return interfaceSuiteLogDO;
    }

    /**
     * 修改测试套件执行日志
     * @param interfaceSuiteLogDO interfaceSuiteLogDO
     */
    @Override
    public void modifyIfSuiteLog(InterfaceSuiteLogDO interfaceSuiteLogDO) {
        interfaceSuiteLogMapper.updateIfSuiteLog(interfaceSuiteLogDO);
    }

    /**
     * 根据项目 模块统计套件运行情况
     *
     * @param suiteLogNo suiteLogNo
     * @return ArrayList
     */
    @Override
    public ArrayList<HashMap<String, Object>> findSuiteLogSummary(String suiteLogNo) {
        // 获取包含的项目和模块
        HashMap<String, Object> resultMap;
        ArrayList<HashMap<String, Object>> resultList = new ArrayList<>();
        ArrayList<HashMap<String, Object>> list = interfaceSuiteLogMapper.selectSuiteLogProjectModule(suiteLogNo);
        for (HashMap<String, Object> map : list) {
            resultMap = new HashMap<>();
            Integer projectId = (Integer) map.get("project_id");
            Integer moduleId = (Integer) map.get("module_id");
            String projectName = (String) map.get("project_name");
            String moduleName = (String) map.get("module_name");
            resultMap.put("projectId", projectId);
            resultMap.put("moduleId", moduleId);
            resultMap.put("projectName", projectName);
            resultMap.put("moduleName", moduleName);
            // 查询每个项目和模块的运行情况
            InterfaceSuiteSummaryDTO summaryDTO = new InterfaceSuiteSummaryDTO();
            summaryDTO.setModuleId(moduleId);
            summaryDTO.setProjectId(projectId);
            summaryDTO.setSuiteLogNo(suiteLogNo);
            ArrayList<HashMap<String, Object>> runDetailList = interfaceSuiteLogMapper.selectSuiteLogSummary(summaryDTO);
            Long pass = 0L;
            Long failure = 0L;
            Long error = 0L;
            for (HashMap<String, Object> runDetail : runDetailList) {
                Integer status = (Integer) runDetail.get("status");
                if (status == 0) {
                    pass = (Long) runDetail.get("count");
                } else if (status == 1) {
                    failure = (Long) runDetail.get("count");
                } else {
                    error = (Long) runDetail.get("count");
                }
            }
            resultMap.put("pass", pass);
            resultMap.put("failure", failure);
            resultMap.put("error", error);
            resultList.add(resultMap);
        }
        return resultList;
    }

    /**
     * 统计测试报告首页测试套件执行情况
     *
     * @param suiteLogNo 测试套件日志编号
     * @return HashMap
     */
    @Override
    public HashMap<String, Object> findSuiteReportAssert(String suiteLogNo) {
        HashMap<String, Object> resultMap = new HashMap<>();
        Long pass = 0L;
        Long failed = 0L;
        Long error = 0L;
        ArrayList<HashMap<String, Object>> list = interfaceAssertLogMapper.selectSuiteReportAssert(suiteLogNo);
        for (HashMap<String, Object> map : list) {
            Integer status = (Integer) map.get("status");
            if (status == 0) {
                pass = (Long) map.get("count");
            } else if (status == 1) {
                failed = (Long) map.get("count");
            } else {
                error = (Long) map.get("count");
            }
        }
        resultMap.put("pass", pass);
        resultMap.put("failed", failed);
        resultMap.put("error", error);
        return resultMap;
    }
}
