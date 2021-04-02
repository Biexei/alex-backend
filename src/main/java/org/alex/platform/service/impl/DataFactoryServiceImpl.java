package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.common.Env;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.DataFactoryMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.pojo.entity.DbConnection;
import org.alex.platform.service.DataFactoryService;
import org.alex.platform.service.DbService;
import org.alex.platform.service.InterfaceCaseSuiteService;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.alex.platform.util.ValidUtil;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
    @Autowired
    Env env;
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
            System.out.println("待补充");
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
        Byte executeType = factoryVO.getExecuteType(); // 执行方式 0并行1串行
        int times = factoryVO.getTimes();
        if (type == 0) { // sql
            // 获取运行环境
            String sql = factoryVO.getSqlStr();
            Byte runEnv = factoryVO.getSqlRunDev();
            Integer dbId = factoryVO.getSqlDbId();
            DbVO dbVO = dbService.findDbById(dbId);
            DbConnection datasource = env.datasource(dbVO, runEnv);
            String url = datasource.getUrl();
            String username = datasource.getUsername();
            String password = datasource.getPassword();
            long begin = System.currentTimeMillis();
            for (int i = 0; i < times; i++) {
                this.runScript(sql, url, username, password, failedStop);
            }
            long end = System.currentTimeMillis();
            return end - begin;

        } else if (type == 1) { // 接口
            Integer suiteId = factoryVO.getInterfaceSuiteId();
            if (executeType == 1) { // 串行
                long begin = System.currentTimeMillis();
                for (int i = 0; i < times; i++) {
                    try {
                        ifSuite.executeSuiteCaseById(suiteId, executor);
                    } catch (BusinessException e) {
                        if (failedStop) {
                            LOG.error("执行至" + i + "出错");
                            throw new BusinessException("执行至" + i + "出错");
                        }
                    }
                }
                long end = System.currentTimeMillis();
                return end - begin;
            } else { // 并行
                ArrayList<Integer> list = new ArrayList<>();
                for (int i = 0; i < times; i++) {
                    list.add(suiteId);
                }
                long begin = System.currentTimeMillis();
                list.parallelStream().forEach(suite -> {
                    try {
                        ifSuite.executeSuiteCaseById(suite, executor);
                    } catch (BusinessException e) {
                        throw new RuntimeException("测试套件执行出错");
                    }
                });
                long end = System.currentTimeMillis();
                return end - begin;
            }
        } else if (type == 2) { // ui
            System.out.println("暂不支持");
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

    /**
     * 执行sql脚本
     */
    private void runScript(String sql, String url, String username, String password, boolean stopOnError) throws BusinessException {
        Connection conn = null;
        try {
            // 建立连接
            conn = DriverManager.getConnection(url, username, password);
            // 创建ScriptRunner，用于执行SQL脚本
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            runner.setStopOnError(stopOnError);
            // 执行SQL脚本
            runner.runScript(new StringReader(sql));
        } catch (SQLException e) {
            LOG.error("执行SQL脚本异常,url={},username={},password={},errorMsg={}", url, username, password, e);
            throw new BusinessException("执行SQL脚本异常");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOG.error("执行SQL脚本异常,url={},username={},password={},errorMsg={}", url, username, password, e);
            }
        }
    }
}

