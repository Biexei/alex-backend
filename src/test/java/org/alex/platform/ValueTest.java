package org.alex.platform;

import org.alex.platform.config.DriverPathConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValueTest {
    @Autowired
    DriverPathConfig driverPathConfig;
    @Test
    public void doTest() {
        driverPathConfig.setChrome("123");
    }
}
