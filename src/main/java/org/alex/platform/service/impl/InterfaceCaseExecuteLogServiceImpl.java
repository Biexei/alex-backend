package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.enums.RelyType;
import org.alex.platform.mapper.InterfaceCaseExecuteLogMapper;
import org.alex.platform.pojo.InterfaceCaseExecuteLogDO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListDTO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListVO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogVO;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class InterfaceCaseExecuteLogServiceImpl implements InterfaceCaseExecuteLogService {
    @Autowired
    InterfaceCaseExecuteLogMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger(InterfaceCaseExecuteLogServiceImpl.class);

    /**
     * 保存执行日志
     *
     * @param executeLogDO executeLogDO
     * @return InterfaceCaseExecuteLogDO自增对象
     */
    @Override
    public InterfaceCaseExecuteLogDO saveExecuteLog(InterfaceCaseExecuteLogDO executeLogDO) {
        mapper.insertExecuteLog(executeLogDO);
        return executeLogDO;
    }

    /**
     * 查看执行日志列表
     *
     * @param executeLogListDTO executeLogListDTO
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @return PageInfo<InterfaceCaseExecuteLogListVO>
     */
    @Override
    public PageInfo<InterfaceCaseExecuteLogListVO> findExecuteList(InterfaceCaseExecuteLogListDTO executeLogListDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(mapper.selectExecuteList(executeLogListDTO));
    }


    /**
     * 查看执行日志，包含请求信息和断言信息列表
     *
     * @param executeId 执行编号
     * @return InterfaceCaseExecuteLogVO
     */
    @Override
    public InterfaceCaseExecuteLogVO findExecute(Integer executeId) {
        return mapper.selectExecute(executeId);
    }

    /**
     * 修改断言日志，主要用来根据断言执行状态来再次修改执行状态
     *
     * @param executeLogDO executeLogDO
     */
    @Override
    public void modifyExecuteLog(InterfaceCaseExecuteLogDO executeLogDO) {
        mapper.updateExecuteLog(executeLogDO);
    }

    /**
     * 获取用例执行调用链
     *
     * @param executeId executeId
     * @return LinkedList<HashMap < String, Object>>
     */
    @Override
    public JSONArray caseExecuteLogChain(Integer executeId) {
        InterfaceCaseExecuteLogVO executeLogVO = this.findExecute(executeId);
        String chain = executeLogVO.getChain();
        return JSONArray.parseArray(chain);
    }

    /**
     * 查看所有的执行日志列表，不分页
     * @param executeLogListDTO executeLogListDTO
     * @return List<InterfaceCaseExecuteLogListVO>
     */
    @Override
    public List<InterfaceCaseExecuteLogListVO> findExecuteListAll(InterfaceCaseExecuteLogListDTO executeLogListDTO) {
        return mapper.selectExecuteList(executeLogListDTO);
    }
}
