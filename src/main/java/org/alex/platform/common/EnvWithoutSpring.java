package org.alex.platform.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.enums.SqlType;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.DbVO;
import org.alex.platform.pojo.entity.DbConnection;
import org.alex.platform.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 反射不支持spring ioc
 * 因此使用new的方式创建对象
 */
public class EnvWithoutSpring {
    private static final Logger LOG = LoggerFactory.getLogger(EnvWithoutSpring.class);

    /**
     * 根据数据源编号 运行环境 返回连接信息
     * @param dbId 数据源编号
     * @param runEnv 运行环境 0dev 1test 2stg 3prod 4debug
     * @return 连接信息
     * @throws BusinessException 数据源已被禁用
     */
    public DbConnection datasource(int dbId, byte runEnv) throws Exception {
        DruidDataSource druid = connection();
        JdbcTemplate template = new JdbcTemplate(druid);
        String sql = "select `id`,`name`,`type`,`desc`,`url`,`username`,`password`,`created_time`,`update_time`,`status`,\n" +
                "        `dev_url`,`dev_username`,`dev_password`,`test_url`,`test_username`,`test_password`,`stg_url`,`stg_username`,\n" +
                "        `stg_password`,`prod_url`,`prod_username`,`prod_password`\n" +
                "        from `t_db` where `id` = " + dbId;
        DbVO dbVO = template.queryForObject(sql, new BeanPropertyRowMapper<>(DbVO.class));
        // 0启动 1禁用
        if (dbVO == null) {
            throw new BusinessException("未找到该数据源");
        }
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
        druid.close();
        return connection;
    }

    /**
     * 获取alex数据库连接池
     * @return alex数据库连接池
     * @throws Exception 异常
     */
    private DruidDataSource connection() throws Exception {
        Yaml yaml = new Yaml();
        // 打成jar包读不到src目录
        // FileInputStream fis = new FileInputStream("src\\main\\resources\\application.yml");
        ClassPathResource resource = new ClassPathResource("application.yml");
        InputStream fis = resource.getInputStream();
        JSONObject yamlConfig = yaml.loadAs(fis, JSONObject.class);
        JSONObject db = yamlConfig.getJSONObject("spring").getJSONObject("datasource");
        String url_ = db.getString("url");
        String username_ = db.getString("username");
        String password_ = db.getString("password");
        fis.close();
        return JdbcUtil.getDruidDataSource(url_, username_, password_, SqlType.SELECT);
    }
}
