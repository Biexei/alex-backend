package org.alex.platform;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.mapper.InterfaceCaseExecuteLogMapper;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.pojo.InterfaceCaseExecuteLogDO;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExecuteLogTest {
    @Autowired
    InterfaceCaseExecuteLogMapper mapper;

    @Autowired
    InterfaceCaseMapper interfaceCaseMapper;

    @Test
    public void testInfo(){
        HashMap map = new HashMap();
        map.put("a", "1");
        map.put("b", "2");
        InterfaceCaseExecuteLogDO logDO = new InterfaceCaseExecuteLogDO();
        logDO.setCaseDesc("case desc");
        logDO.setRequestHeaders(JSONObject.toJSONString(map));
        logDO.setCreatedTime(new Date());
        //mapper.insertExecuteLog(logDO);

        System.out.println(JSONObject.toJSONString(interfaceCaseMapper.selectInterfaceCaseByCaseId(6)));
    }
}
