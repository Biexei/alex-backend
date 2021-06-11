package org.alex.platform.common;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.DbVO;
import org.alex.platform.pojo.ProjectVO;
import org.alex.platform.pojo.entity.DbConnection;
import org.alex.platform.service.DbService;
import org.alex.platform.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Env {
    @Autowired
    DbService dbService;
    @Autowired
    ProjectService projectService;

    private static final Logger LOG = LoggerFactory.getLogger(Env.class);

    public String domain(Integer projectId, Byte runEnv) throws BusinessException {
        ProjectVO projectVO = projectService.findProjectById(projectId);
        return domain(projectVO, runEnv);
    }

    public String domain(ProjectVO projectVO, Byte runEnv) throws BusinessException {
        String host;
        String protocol;
        Integer port;
        if (runEnv == 0) {
            host = projectVO.getDevDomain();
            protocol = projectVO.getDevProtocol();
            port = projectVO.getDevPort();
        } else if (runEnv == 1) {
            host = projectVO.getTestDomain();
            protocol = projectVO.getTestProtocol();
            port = projectVO.getTestPort();
        } else if (runEnv == 2) {
            host = projectVO.getStgDomain();
            protocol = projectVO.getStgProtocol();
            port = projectVO.getStgPort();
        } else if (runEnv == 3) {
            host = projectVO.getProdDomain();
            protocol = projectVO.getProdProtocol();
            port = projectVO.getProdPort();
        } else if (runEnv == 4) {
            host = projectVO.getDomain();
            protocol = projectVO.getProtocol();
            port = projectVO.getPort();
        } else {
            LOG.error("运行环境错误，invalid runDev={}", runEnv);
            throw new BusinessException("运行环境错误");
        }
        if (port != null) {
            return protocol + "://" + host + ":" + port;
        }
        return protocol + "://" + host;
    }

    /**
     * 根据数据源编号 运行环境 返回连接信息
     * @param dbId 数据源编号
     * @param runEnv 运行环境 0dev 1test 2stg 3prod 4debug
     * @return 连接信息
     * @throws BusinessException 数据源已被禁用
     */
    public DbConnection datasource(Integer dbId, Byte runEnv) throws BusinessException {
        DbVO dbVO = dbService.findDbById(dbId);
        return datasource(dbVO, runEnv);
    }

    /**
     * 根据数据源编号 运行环境 返回连接信息
     * @param dbVO dbVO
     * @param runEnv 运行环境 0dev 1test 2stg 3prod 4debug
     * @return 连接信息
     * @throws BusinessException 数据源已被禁用
     */
    public DbConnection datasource(DbVO dbVO, Byte runEnv) throws BusinessException {
        // 0启动 1禁用
        int status = dbVO.getStatus();
        if (status == 1) {
            LOG.warn("数据源已被禁用，dbName={}", dbVO.getName());
            throw new BusinessException("数据源已被禁用");
        }
        String url;
        String username;
        String password;
        if (runEnv == 4) {
            url = dbVO.getUrl();
            username = dbVO.getUsername();
            password = dbVO.getPassword();
        } else if (runEnv == 0) {
            url = dbVO.getDevUrl();
            username = dbVO.getDevUsername();
            password = dbVO.getDevPassword();
        } else if (runEnv == 1) {
            url = dbVO.getTestUrl();
            username = dbVO.getTestUsername();
            password = dbVO.getTestPassword();
        } else if (runEnv == 2) {
            url = dbVO.getStgUrl();
            username = dbVO.getStgUsername();
            password = dbVO.getStgPassword();
        } else if (runEnv == 3) {
            url = dbVO.getProdUrl();
            username = dbVO.getProdUsername();
            password = dbVO.getProdPassword();
        } else {
            throw new BusinessException("数据源确定运行环境时出错");
        }
        DbConnection connection = new DbConnection();
        connection.setUrl(url);
        connection.setUsername(username);
        connection.setPassword(password);
        return connection;
    }
}
