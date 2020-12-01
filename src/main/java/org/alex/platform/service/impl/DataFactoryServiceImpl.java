package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.DataFactoryMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.DataFactoryService;
import org.alex.platform.service.DbService;
import org.alex.platform.service.InterfaceCaseSuiteService;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.alex.platform.util.JsonUtil;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class DataFactoryServiceImpl implements DataFactoryService {
    @Autowired
    DataFactoryMapper dataFactoryMapper;
    @Autowired
    InterfaceSuiteCaseRefService ifSuite;
    @Autowired
    DbService dbService;
    @Autowired
    InterfaceCaseSuiteService interfaceCaseSuiteService;
    private static final Logger LOG = LoggerFactory.getLogger(DataFactoryServiceImpl.class);

    /**
     * 新增数据工厂
     * @param dataFactoryDO dataFactoryDO
     */
    @Override
    public void saveDataFactory(DataFactoryDO dataFactoryDO) throws ValidException {
        // 数据校验
        this.checkDO(dataFactoryDO);
        // 数据处理
        DataFactoryDO factoryDO = this.dealWithDO(dataFactoryDO);
        Date date = new Date();
        factoryDO.setCreatedTime(date);
        factoryDO.setUpdateTime(date);
        dataFactoryMapper.insertDataFactory(factoryDO);
    }

    /**
     * 修改数据工厂
     * @param dataFactoryDO dataFactoryDO
     * @throws ValidException ValidException
     */
    @Override
    public void modifyDataFactory(DataFactoryDO dataFactoryDO) throws ValidException {
        // 数据校验
        this.checkDO(dataFactoryDO);
        // 数据处理
        DataFactoryDO factoryDO = this.dealWithDO(dataFactoryDO);
        factoryDO.setUpdateTime(new Date());
        dataFactoryMapper.updateDataFactory(factoryDO);
    }

    /**
     * 删除数据工厂
     * @param id id
     */
    @Override
    public void removeDataFactoryById(Integer id) {
        dataFactoryMapper.deleteDataFactoryById(id);
        LOG.info("删除数据工厂编号={}", id);
    }

    /**
     * 获取数据工厂详情
     * @param id id
     * @return DataFactoryVO
     */
    @Override
    public DataFactoryVO findDataFactoryById(Integer id) {
        DataFactoryVO dataFactoryVO = dataFactoryMapper.selectDataFactoryById(id);
        Byte type = dataFactoryVO.getType();
        String name = "";
        if (type == 0) { // sql
            Integer sqlDbId = dataFactoryVO.getSqlDbId();
            DbVO dbVO = dbService.findDbById(sqlDbId);
            name = dbVO.getName();
        } else if (type == 1) { // 接口
            Integer suiteId = dataFactoryVO.getInterfaceSuiteId();
            InterfaceCaseSuiteVO suite = interfaceCaseSuiteService.findInterfaceCaseSuiteById(suiteId);
            name = suite.getSuiteName();
        } else if (type == 2) { // ui

        }
        dataFactoryVO.setElementName(name);
        return dataFactoryVO;
    }

    /**
     * 获取数据工厂列表
     * @param dataFactoryDTO dataFactoryDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo
     */
    @Override
    public PageInfo<DataFactoryVO> findDataFactoryList(DataFactoryDTO dataFactoryDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(dataFactoryMapper.selectDataFactoryList(dataFactoryDTO));
    }

    /**
     * 执行数据工厂
     * @param id id
     * @executor 执行人
     * @return 执行总耗时ms
     */
    @Override
    public long executeDataFactory(Integer id, String executor) throws BusinessException {
        DataFactoryVO factoryVO = this.findDataFactoryById(id);
        Byte type = factoryVO.getType();
        boolean failedStop = factoryVO.getFailedStop() == 0;
        int times = factoryVO.getTimes();
        if (type == 0) { // sql

        } else if (type == 1) { // 接口
            Integer suiteId = factoryVO.getInterfaceSuiteId();
            long begin = System.currentTimeMillis();
            for (int i = 0; i < times; i++) {
                try {
                    ifSuite.executeSuiteCaseById(suiteId, executor);
                } catch (BusinessException e) {
                    if (failedStop) {
                        LOG.warn("执行至" + i + "出错");
                        throw new BusinessException("执行至" + i + "出错");
                    }
                }
            }
            long end = System.currentTimeMillis();
            return end - begin;
        } else if (type == 2) { // ui

        }
        return 0;
    }

    /**
     * 参数校验
     * @param dataFactoryDO dataFactoryDO
     * @throws ValidException ValidException
     */
    private void checkDO(DataFactoryDO dataFactoryDO) throws ValidException {
        Byte type = dataFactoryDO.getType();
        Integer sqlDbId = dataFactoryDO.getSqlDbId();
        Byte sqlRunDev = dataFactoryDO.getSqlRunDev();
        String sqlStr = dataFactoryDO.getSqlStr();
        Integer interfaceSuiteId = dataFactoryDO.getInterfaceSuiteId();
        Integer uiSuiteId = dataFactoryDO.getUiSuiteId();
        if (type == 0) { // sql
            ValidUtil.notNUll(sqlDbId, "请选择数据源编号");
            ValidUtil.notNUll(sqlRunDev, "请选择运行环境");
            ValidUtil.notNUll(sqlStr, "请输入SQL语句");
            ValidUtil.notEmpty(sqlStr, "请输入SQL语句");
        } else if (type == 1) { // 接口
            ValidUtil.notNUll(interfaceSuiteId, "请选择测试套件");
        } else if (type == 2) { // ui
            ValidUtil.notNUll(uiSuiteId, "请选择测试套件");
        }
    }

    /**
     * 数据处理
     * @param dataFactoryDO dataFactoryDO
     * @return dataFactoryDO
     */
    private DataFactoryDO dealWithDO(DataFactoryDO dataFactoryDO) {
        Byte type = dataFactoryDO.getType();
        if (type == 0) { // sql
            dataFactoryDO.setInterfaceSuiteId(null);
            dataFactoryDO.setUiSuiteId(null);
        } else if (type == 1) { // 接口
            dataFactoryDO.setSqlDbId(null);
            dataFactoryDO.setSqlRunDev(null);
            dataFactoryDO.setSqlStr(null);
            dataFactoryDO.setUiSuiteId(null);
        } else if (type == 2) { // ui
            dataFactoryDO.setSqlDbId(null);
            dataFactoryDO.setSqlRunDev(null);
            dataFactoryDO.setSqlStr(null);
            dataFactoryDO.setInterfaceSuiteId(null);
        }
        return dataFactoryDO;
    }
}
