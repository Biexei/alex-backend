package org.alex.platform;

import com.sun.javafx.fxml.builder.URLBuilder;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {
    @Autowired
    MailService mailService;
    @Test
    public void testSendMail() throws BusinessException, MalformedURLException {
        URL url = new URL("123");
    }
}
