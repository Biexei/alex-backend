package org.alex.platform;

import com.alibaba.fastjson.JSON;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.PostProcessorDO;
import org.alex.platform.pojo.PostProcessorDTO;
import org.alex.platform.service.PostProcessorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostProcessorTest {
    @Autowired
    private PostProcessorService postProcessorService;

    @Test
    public void testSave() throws BusinessException {
        PostProcessorDO postProcessorDO = new PostProcessorDO();
        postProcessorDO.setCaseId(69);
        postProcessorDO.setName("post processor2");
        postProcessorDO.setType((byte)0);
        postProcessorDO.setExpression("expression");
        postProcessorService.savePostProcessor(postProcessorDO);
        System.out.println(postProcessorDO.getPostProcessorId());
    }

    @Test
    public void testModify() throws BusinessException {
        PostProcessorDO postProcessorDO = new PostProcessorDO();
        postProcessorDO.setPostProcessorId(1);
        postProcessorDO.setCaseId(69);
        postProcessorDO.setName("post processor");
        postProcessorDO.setType((byte)1);
        postProcessorDO.setExpression("expression1");
        postProcessorService.modifyPostProcessor(postProcessorDO);
    }

    @Test
    public void testFindByName() {
        String name = "post processor";
        System.out.println(JSON.toJSONString(postProcessorService.findPostProcessorByName(name)));
    }

    @Test
    public void testFindById() {
        System.out.println(JSON.toJSONString(postProcessorService.findPostProcessorById(1)));
    }

    @Test
    public void testFindList() {
        PostProcessorDTO postProcessorDTO = new PostProcessorDTO();
        postProcessorDTO.setCaseId(69);
        postProcessorDTO.setName("post");
        postProcessorDTO.setPostProcessorId(1);
        postProcessorDTO.setType((byte)1);
        System.out.println(JSON.toJSONString(postProcessorService.findPostProcessorList(postProcessorDTO)));
    }
}
