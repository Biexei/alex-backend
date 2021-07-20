package org.alex.platform.util;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.alex.platform.enums.SqlType;
import org.alex.platform.exception.SqlException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.io.StringReader;
import java.sql.*;
import java.util.*;

@SuppressWarnings({"rawtypes", "uncheck"})
public class JdbcUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcUtil.class);

    private JdbcUtil() {

    }

    /**
     * 数据库预检
     * @param url 数据库连接地址
     * @param username 用户名
     * @param password 密码
     * @return 是否连接
     */
    public static String checkJdbcConnection(String url, String username, String password) {
        String msg = "连接成功";
        DruidDataSource ds = null;
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("init","false");
            map.put("url", url);
            map.put("username", username);
            map.put("password", password);
            ds = (DruidDataSource) DruidDataSourceFactory.createDataSource(map);
            // 连通性检查
            JdbcTemplate jdbc = new JdbcTemplate(initDataSourceConfig(ds, SqlType.SELECT));
            jdbc.queryForList("select 1");
        } catch (Exception e) {
            msg = "连接失败，" + e.getMessage();
            LOG.error("JDBC TEMPLATE 连接失败， errorMsg={}", ExceptionUtil.msg(e));
        } finally {
            if (ds != null) {
                ds.close();
            }
        }
        return msg;
    }

    /**
     * @param url 数据库url
     * @param username 连接用户名
     * @param password 连接密码
     * @return spring template
     */
    public static DruidDataSource getDruidDataSource(String url, String username, String password, SqlType sqlType) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("init","false");
        map.put("url", url);
        map.put("username", username);
        map.put("password", password);
        DruidDataSource ds = (DruidDataSource) DruidDataSourceFactory.createDataSource(map);
        // 配置数据源
        initDataSourceConfig(ds, sqlType);
        return ds;
    }

    /**
     * 查询首行首列，带参数
     * @param url 数据库url
     * @param username 连接用户名
     * @param password 连接密码
     * @param sql sql预计
     * @param params 参数，第一个参数为json path表达式，如果为空或者大小为0，那么返回首行首列结果
     * @return 查询结果
     * @throws SqlException 数据库异常
     */
    public static String selectFirst(String url, String username, String password, String sql, Object[] params) throws SqlException {
        String resultStr;
        DruidDataSource druidDataSource = null;
        try {
            resultStr = "";

            druidDataSource = getDruidDataSource(url, username, password, SqlType.SELECT);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(druidDataSource);

            if (null == params || params.length == 0) {
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);
                if (list.isEmpty()) {
                    throw new SqlException("查询结果为空");
                }
                Map result = list.get(0);
                for (Object key : result.keySet()) {
                    resultStr = result.get(key).toString();
                    break;
                }
            } else {
                String jsonPath = (String) params[0];
                Object[] removeAfterParams = ArrayUtils.remove(params, 0);
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, removeAfterParams);
                ArrayList sqlResultArray = JSONObject.parseObject(ParseUtil.parseJson(JSON.toJSONString(list, SerializerFeature.WriteMapNullValue), jsonPath), ArrayList.class);
                if (sqlResultArray.isEmpty()) {
                    LOG.warn("sql语句提取结果为空, sql={}, json path={}, params={}", sql, jsonPath, removeAfterParams);
                    throw new SqlException(String.format("sql语句提取结果为空, sql=%s, json path=%s, params=%s", sql, jsonPath, Arrays.toString(removeAfterParams)));
                }
                if (sqlResultArray.size() == 1) {
                    Object o = sqlResultArray.get(0);
                    if (o == null) {
                        LOG.warn("sql语句提取结果为空, sql={}, json path={}, params={}", sql, jsonPath, removeAfterParams);
                        throw new SqlException(String.format("sql语句提取结果为空, sql=%s, json path=%s, params=%s", sql, jsonPath, Arrays.toString(removeAfterParams)));
                    }
                    return o.toString();
                } else {
                    return JSON.toJSONString(sqlResultArray);
                }
            }
        } catch (Exception e) {
            LOG.error("JDBC TEMPLATE 查询失败， errorMsg={}", ExceptionUtil.msg(e));
            throw new SqlException("查询失败，" + e.getMessage());
        } finally {
            if (druidDataSource != null) {
                druidDataSource.close();
            }
        }
        return resultStr;
    }

    /**
     * 新增并获取自增主键
     * @param url url
     * @param username username
     * @param password password
     * @param sql sql
     * @param params 参数
     * @return 自增主键
     * @throws SqlException 执行异常
     */
    public static long insert(String url, String username, String password, String sql, Object[] params) throws SqlException {
        KeyHolder hold = new GeneratedKeyHolder();
        DruidDataSource druidDataSource = null;
        PreparedStatementCreator preparedStatementCreator = conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    // 以下判断参考spring jdbc 源码
                    // org.springframework.jdbc.core.ArgumentPreparedStatementSetter.doSetValue
                    if (params[i] instanceof SqlParameterValue) {
                        SqlParameterValue paramValue = (SqlParameterValue) params[i];
                        StatementCreatorUtils.setParameterValue(ps, i + 1, paramValue, paramValue.getValue());
                    }
                    else {
                        StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, params[i]);
                    }
                }
            }
            return ps;
        };
        try {
            druidDataSource = getDruidDataSource(url, username, password, SqlType.INSERT);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(druidDataSource);
            jdbcTemplate.update(preparedStatementCreator, hold);
            Number key = hold.getKey();
            if (key == null) {
                throw new SqlException("新增失败");
            }
            return key.longValue();
        } catch (Exception e) {
            LOG.error("JDBC TEMPLATE 新增失败， errorMsg={}", ExceptionUtil.msg(e));
            throw new SqlException("新增失败，请检查类型与SQL语句类型是否匹配。" + e.getMessage());
        } finally {
            if (druidDataSource != null) {
                druidDataSource.close();
            }
        }
    }

    /**
     * 修改语句
     * @param url url
     * @param username username
     * @param password password
     * @param sql sql
     * @param params params
     * @return ""
     */
    public static String update(String url, String username, String password, String sql, Object[] params) throws SqlException {
        DruidDataSource druidDataSource = null;
        try {
            druidDataSource = getDruidDataSource(url, username, password, SqlType.UPDATE);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(druidDataSource);
            jdbcTemplate.update(sql, params);
        } catch (Exception e) {
            LOG.error("JDBC TEMPLATE 修改失败， errorMsg={}", ExceptionUtil.msg(e));
            throw new SqlException("修改失败，请检查类型与SQL语句类型是否匹配。" + e.getMessage());
        } finally {
            if (druidDataSource != null) {
                druidDataSource.close();
            }
        }
        return "";
    }

    /**
     * 删除语句
     * @param url url
     * @param username username
     * @param password password
     * @param sql sql
     * @param params params
     * @return ""
     */
    public static String delete(String url, String username, String password, String sql, Object[] params) throws SqlException {
        DruidDataSource druidDataSource = null;
        try {
            druidDataSource = getDruidDataSource(url, username, password, SqlType.DELETE);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(druidDataSource);
            jdbcTemplate.update(sql, params);
        } catch (Exception e) {
            LOG.error("JDBC TEMPLATE 删除失败， errorMsg={}", ExceptionUtil.msg(e));
            throw new SqlException("删除失败，请检查类型与SQL语句类型是否匹配。" + e.getMessage());
        } finally {
            if (druidDataSource != null) {
                druidDataSource.close();
            }
        }
        return "";
    }

    /**
     * 批量执行SQL脚本
     * @param sql sql
     * @param url url
     * @param username username
     * @param password password
     * @param stopOnError stopOnError
     * @throws SqlException SqlException
     */
    public static String script(String sql, String url, String username, String password, boolean stopOnError) throws SqlException {
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
            throw new SqlException("执行SQL脚本异常");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOG.error("执行SQL脚本异常,url={},username={},password={},errorMsg={}", url, username, password, e);
            }
        }
        return "";
    }

    /**
     * 查询单列结果值
     * @param url jdbc url
     * @param username 用户名
     * @param password 密码
     * @param sql sql
     * @param elementType 元素类型
     * @param <T> 枚举
     * @return list
     * @throws Exception 异常
     */
    public static <T> List<T> queryForList(String url, String username, String password, String sql, Class<T> elementType) throws Exception {
        DruidDataSource druidDataSource = null;
        JdbcTemplate jdbcTemplate;
        try {
            druidDataSource = getDruidDataSource(url, username, password, SqlType.SELECT);
            jdbcTemplate = new JdbcTemplate(druidDataSource);
            return jdbcTemplate.queryForList(sql, elementType);
        } catch (Exception e) {
            LOG.error("执行SQL异常,url={},username={},password={},errorMsg={}", url, username, password, e);
            throw new Exception("执行SQL异常");
        } finally {
            if (druidDataSource != null) {
                druidDataSource.close();
            }
        }
    }

    /**
     * 初始化数据源配置
     * @param ds 数据源
     * @return 数据源
     * @throws SQLException SQLException
     */
    private static DruidDataSource initDataSourceConfig(DruidDataSource ds, SqlType sqlType) throws SQLException {

        WallFilter wallFilter = new WallFilter();
        WallConfig wallConfig = new WallConfig();
        // 配置仅允许查询
        if(sqlType == SqlType.SELECT) {
            wallConfig.setDeleteAllow(false);
            wallConfig.setUpdateAllow(false);
            wallConfig.setInsertAllow(false);
            wallConfig.setDropTableAllow(false);
            wallConfig.setAlterTableAllow(false);
        } else if (sqlType == SqlType.UPDATE) {
            wallConfig.setDeleteAllow(false);
            wallConfig.setInsertAllow(false);
            wallConfig.setDropTableAllow(false);
            wallConfig.setAlterTableAllow(false);
            wallConfig.setSelelctAllow(false);
        } else if (sqlType == SqlType.INSERT) {
            wallConfig.setDeleteAllow(false);
            wallConfig.setUpdateAllow(false);
            wallConfig.setSelelctAllow(false);
            wallConfig.setDropTableAllow(false);
            wallConfig.setAlterTableAllow(false);
        } else if (sqlType == SqlType.DELETE) {
            wallConfig.setSelelctAllow(false);
            wallConfig.setUpdateAllow(false);
            wallConfig.setInsertAllow(false);
            wallConfig.setDropTableAllow(false);
            wallConfig.setAlterTableAllow(false);
        }
        // 将配置加入过滤器
        wallFilter.setConfig(wallConfig);
        List<Filter> wallFilters = new ArrayList<>();
        wallFilters.add(wallFilter);

        ds.setProxyFilters(wallFilters);
        ds.setFailFast(true);
        ds.setConnectionErrorRetryAttempts(1);
        ds.setBreakAfterAcquireFailure(true);
        ds.init();
        return ds;
    }

}
