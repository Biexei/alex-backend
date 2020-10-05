package org.alex.platform;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {
    @Autowired
    MailService mailService;
    @Test
    public void testSendMail() throws BusinessException {
        mailService.send("标题", "内容",  "1014759718@qq.com");
    }
}
