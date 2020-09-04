package org.alex.platform;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JDBCTest {
    @Test
    public void testConn(){
        String url = "jdbc:mysql://localhost:3306/platform?useUnicode=true&characterEncoding=utf-8";
        String username = "root11";
        String password = "root";
        DruidDataSource ds = new DruidDataSource();
        ds.setUsername(username);
        ds.setFailFast(true);
        ds.setConnectionErrorRetryAttempts(1);
        ds.setPassword(password);
        ds.setUrl(url);
        JdbcTemplate jdbc = new JdbcTemplate(ds);
        Map result = jdbc.queryForList("select username, password from t_user").get(0);
        for (Object key : result.keySet()) {
            System.out.println(result.get(key));
            break;
        }
    }
}
