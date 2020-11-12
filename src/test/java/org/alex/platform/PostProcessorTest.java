package org.alex.platform;

import com.alibaba.fastjson.JSON;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceProcessorDO;
import org.alex.platform.pojo.InterfaceProcessorDTO;
import org.alex.platform.service.InterfaceProcessorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostProcessorTest {
    @Autowired
    private InterfaceProcessorService interfaceProcessorService;

    @Test
    public void testSave() throws BusinessException {
        InterfaceProcessorDO interfaceProcessorDO = new InterfaceProcessorDO();
        interfaceProcessorDO.setCaseId(69);
        interfaceProcessorDO.setName("post processor2");
        interfaceProcessorDO.setType((byte)0);
        interfaceProcessorDO.setExpression("expression");
        interfaceProcessorService.saveInterfaceProcessor(interfaceProcessorDO);
        System.out.println(interfaceProcessorDO.getProcessorId());
    }

    @Test
    public void testModify() throws BusinessException {
        InterfaceProcessorDO interfaceProcessorDO = new InterfaceProcessorDO();
        interfaceProcessorDO.setProcessorId(1);
        interfaceProcessorDO.setCaseId(69);
        interfaceProcessorDO.setName("post processor");
        interfaceProcessorDO.setType((byte)1);
        interfaceProcessorDO.setExpression("expression1");
        interfaceProcessorService.modifyInterfaceProcessor(interfaceProcessorDO);
    }

    @Test
    public void testFindByName() {
        String name = "post processor";
        System.out.println(JSON.toJSONString(interfaceProcessorService.findInterfaceProcessorByName(name)));
    }

    @Test
    public void testFindById() {
        System.out.println(JSON.toJSONString(interfaceProcessorService.findInterfaceProcessorById(1)));
    }

    @Test
    public void testFindList() {
        InterfaceProcessorDTO interfaceProcessorDTO = new InterfaceProcessorDTO();
        interfaceProcessorDTO.setCaseId(69);
        interfaceProcessorDTO.setName("post");
        interfaceProcessorDTO.setProcessorId(1);
        interfaceProcessorDTO.setType((byte)1);
        System.out.println(JSON.toJSONString(interfaceProcessorService.findInterfaceProcessorList(interfaceProcessorDTO)));
    }
}
