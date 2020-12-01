package org.alex.platform;

import org.alex.platform.service.InterfacePreCaseService;
import org.alex.platform.service.impl.InterfacePreCaseServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterfacePreCaseServiceTest {
    @Autowired
    InterfacePreCaseService service;

    @Test
    public void testPreCaseList() {
        List<Integer> list = service.recursionPreCase(new ArrayList<Integer>(), 126);
        System.out.println(list);
    }
}
