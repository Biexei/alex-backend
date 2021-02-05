package org.alex.platform;

import org.alex.platform.exception.ValidException;
import org.alex.platform.generator.Valid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastJsonTest {
    @Autowired
    Valid valid;
    @Test
    public void doTest() throws ValidException {
        BigDecimal minimum = new BigDecimal(1);
        BigDecimal maximum = new BigDecimal(1);

        Integer minIntLen = 1;
        Integer maxIntLen = 1;
        Integer minDecLen = 1;
        Integer maxDecLen = 1;
        valid.valid4Number(minimum, maximum, minIntLen, maxIntLen, minDecLen, maxDecLen);
    }
}
