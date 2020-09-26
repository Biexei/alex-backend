package org.alex.platform;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.service.InterfaceCaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterfaceCaseTest {
    @Autowired
    InterfaceCaseService service;
    @Test
    public void doTest() {
        Integer integer = new Integer(123);
        System.out.println(integer.toString());
    }
}
