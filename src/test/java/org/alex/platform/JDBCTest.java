package org.alex.platform;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.ModuleMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.*;
import org.alex.platform.util.JdbcUtil;
import org.alex.platform.util.ParseUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JDBCTest {
    @Autowired
    InterfaceCaseService interfaceCaseService;
    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;
    @Autowired
    ModuleMapper moduleMapper;
    @Autowired
    InterfaceAssertService interfaceAssertService;
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;
    @Autowired
    ProjectService projectService;
    @Autowired
    InterfaceAssertLogService assertLogService;
    @Autowired
    InterfaceCaseRelyDataService ifCaseRelyDataService;
    @Autowired
    RelyDataService relyDataService;
    @Autowired
    DbService dbService;
    @Test
    public void testConn() throws ParseException {
        String url = "jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false";
        String username = "root";
        String password = "root";
//        JdbcTemplate jdbc = JdbcUtil.getInstance(url, username, password);
//        List<Map<String, Object>> query = jdbc.queryForList("select * from `t_user`");
//        System.out.println(this.getValue(query, 0, 0));

        System.out.println(JdbcUtil.checkJdbcConnection(url, username, password));

        String url1 = "jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false";
        String username1 = "root";
        String password1 = "root1";
        System.out.println(JdbcUtil.checkJdbcConnection(url1, username1, password1));
    }

    public String getValue(List<Map<String, Object>> queryResult, int rowIndex, int colIndex) throws ParseException {
        if (rowIndex >= queryResult.size()) {
            throw new ParseException("请重新确定行大小");
        }
        Map<String, Object> map = queryResult.get(rowIndex);
        if (colIndex >= map.size()) {
            throw new ParseException("请重新确定列大小");
        }
        String result = "";
        int index = 0;
        for (Map.Entry entry :
             map.entrySet()) {
            if (index == colIndex) {
                result = entry.getValue().toString();
                break;
            }
            index ++;
        }
        return result;
    }

    @Test
    public void doSqlScript() throws Exception {
        String url = "jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false";
        String username = "root";
        String password = "root";
        String params[] = {"xiebei", "123456"};
        String sql = "INSERT INTO `platform`.`t_user` (`username`, `password`, `job_number`, `sex`, `is_enable`, `created_time`, `update_time`, `real_name`, `role_id`) VALUES (?, ?, '', '1', '1', NULL, '2021-03-10 16:08:51', '123', '1');\n";
        long s = JdbcUtil.insert(url, username, password, sql, params);
        System.out.println(s);
    }

    @Test
    public void doTets1() throws Exception {
        String url = "jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&transformedBitIsBoolean=false";
        String username = "root";
        String password = "root";
        List<Integer> result = JdbcUtil.queryForList(url, username, password, "select `desc` from t_db where id = 1", Integer.class);
        System.out.println(result);
    }
}
