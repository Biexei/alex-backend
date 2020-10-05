package org.alex.platform;

import com.alibaba.fastjson.JSON;
import org.alex.platform.mapper.TaskMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {
    @Autowired
    TaskMapper taskMapper;

    @Test
    public void testTask() {
        System.out.println(JSON.toJSONString(taskMapper.selectTaskList(null)));
    }
}
