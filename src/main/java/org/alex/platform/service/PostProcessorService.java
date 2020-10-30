package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.PostProcessorDO;
import org.alex.platform.pojo.PostProcessorDTO;
import org.alex.platform.pojo.PostProcessorVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostProcessorService {
    PostProcessorVO findPostProcessorByName(String name);

    PostProcessorVO findPostProcessorById(Integer postProcessorId);

    List<PostProcessorVO> checkPostProcessorName(Integer postProcessorId, String name);

    List<PostProcessorVO> findPostProcessorList(PostProcessorDTO postProcessorDTO);

    @Transactional(rollbackFor = Exception.class)
    PostProcessorDO savePostProcessor(PostProcessorDO postProcessorDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void modifyPostProcessor(PostProcessorDO postProcessorDO) throws BusinessException;

    @Transactional(rollbackFor = Exception.class)
    void removePostProcessorById(Integer postProcessorId);

    @Transactional(rollbackFor = Exception.class)
    void removePostProcessorByCaseId(Integer caseId);
}
