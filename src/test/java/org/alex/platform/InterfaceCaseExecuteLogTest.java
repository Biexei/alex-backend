package org.alex.platform;

import org.alex.platform.pojo.InterfaceAssertLogDO;
import org.alex.platform.service.InterfaceAssertLogService;
import org.alex.platform.service.impl.InterfaceAssertLogServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterfaceCaseExecuteLogTest {
    @Autowired
    InterfaceAssertLogService assertLogService;
    @Test
    public void doTest(){
        InterfaceAssertLogDO assertLog = new InterfaceAssertLogDO();
        assertLog.setExecuteLogId(1);
        assertLog.setAssertName("1132");
        assertLog.setCaseId(1);
        assertLog.setActualResult("123");
        assertLogService.saveInterfaceAssertLog(assertLog);
    }
}
