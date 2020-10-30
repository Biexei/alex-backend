package org.alex.platform.service.impl;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.InterfaceCaseMapper;
import org.alex.platform.mapper.PostProcessorMapper;
import org.alex.platform.pojo.PostProcessorDO;
import org.alex.platform.pojo.PostProcessorDTO;
import org.alex.platform.pojo.PostProcessorVO;
import org.alex.platform.service.PostProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostProcessorServiceImpl implements PostProcessorService {
    private static final Logger LOG = LoggerFactory.getLogger(PostProcessorServiceImpl.class);
    @Autowired
    private PostProcessorMapper postProcessorMapper;
    @Autowired
    private InterfaceCaseMapper interfaceCaseMapper;

    /**
     * 根据后置处理器名称查询详情
     *
     * @param name name
     * @return PostProcessorVO
     */
    @Override
    public PostProcessorVO findPostProcessorByName(String name) {
        return postProcessorMapper.selectPostProcessorByName(name);
    }

    /**
     * 根据后置处理器主键查询详情
     *
     * @param postProcessorId postProcessorId
     * @return PostProcessorVO
     */
    @Override
    public PostProcessorVO findPostProcessorById(Integer postProcessorId) {
        return postProcessorMapper.selectPostProcessorById(postProcessorId);
    }

    /**
     * 检查名称唯一性
     *
     * @param postProcessorId postProcessorId
     * @param name            name
     * @return List<PostProcessorVO>
     */
    @Override
    public List<PostProcessorVO> checkPostProcessorName(Integer postProcessorId, String name) {
        return postProcessorMapper.checkPostProcessorName(postProcessorId, name);
    }

    /**
     * 获取后置处理器列表
     *
     * @param postProcessorDTO postProcessorDTO
     * @return List<PostProcessorVO>
     */
    @Override
    public List<PostProcessorVO> findPostProcessorList(PostProcessorDTO postProcessorDTO) {
        return postProcessorMapper.selectPostProcessorList(postProcessorDTO);
    }

    /**
     * 新增后置处理器
     *
     * @param postProcessorDO postProcessorDO
     * @return PostProcessorDO 自增对象
     * @throws BusinessException name唯一性和caseId存在性检查
     */
    @Override
    public PostProcessorDO savePostProcessor(PostProcessorDO postProcessorDO) throws BusinessException {
        Date date = new Date();
        postProcessorDO.setCreatedTime(date);
        postProcessorDO.setUpdateTime(date);
        // 0.检查缺省值
        Byte haveDefaultValue = postProcessorDO.getHaveDefaultValue();
        String defaultValue = postProcessorDO.getDefaultValue();
        if (haveDefaultValue == 0) { // 存在缺省值, 则缺省值不允许为空
            if (defaultValue == null) {
                throw new BusinessException("缺省值不能为空");
            }
        } else if (haveDefaultValue == 1) { // 不存在缺省值, 将defaultValue置为null
            postProcessorDO.setDefaultValue(null);
        } else {
            throw new BusinessException("haveDefaultValue参数值非法");
        }
        // 1.检查caseId合法性
        Integer caseId = postProcessorDO.getCaseId();
        if (caseId == null) {
            LOG.error("新增后置处理器时，后置处理器用例编号为空");
            throw new BusinessException("后置处理器用例编号不能为空");
        }
        if (interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId) == null) {
            LOG.error("新增后置处理器时，后置处理器用例编号不存在");
            throw new BusinessException("后置处理器用例编号不存在");
        }
        // 2.检查name唯一性
        String name = postProcessorDO.getName();
        if (findPostProcessorByName(name) != null) {
            LOG.info("新增后置处理器时，后置处理器名称重复，name={}", name);
            throw new BusinessException("后置处理器名称重复");
        }
        postProcessorMapper.insertPostProcessor(postProcessorDO);
        return postProcessorDO;
    }

    /**
     * 修改后置处理器
     *
     * @param postProcessorDO postProcessorDO
     * @throws BusinessException name唯一性和caseId存在性检查
     */
    @Override
    public void modifyPostProcessor(PostProcessorDO postProcessorDO) throws BusinessException {
        postProcessorDO.setUpdateTime(new Date());
        // 0.检查缺省值
        Byte haveDefaultValue = postProcessorDO.getHaveDefaultValue();
        String defaultValue = postProcessorDO.getDefaultValue();
        if (haveDefaultValue == 0) { // 存在缺省值, 则缺省值不允许为空
            if (defaultValue == null) {
                throw new BusinessException("缺省值不能为空");
            }
        } else if (haveDefaultValue == 1) { // 不存在缺省值, 将defaultValue置为null
            postProcessorDO.setDefaultValue(null);
        } else {
            throw new BusinessException("haveDefaultValue参数值非法");
        }
        // 1.检查caseId合法性
        Integer caseId = postProcessorDO.getCaseId();
        if (caseId == null) {
            LOG.error("修改后置处理器时，后置处理器用例编号为空");
            throw new BusinessException("后置处理器用例编号不能为空");
        }
        if (interfaceCaseMapper.selectInterfaceCaseByCaseId(caseId) == null) {
            LOG.error("修改后置处理器时，后置处理器用例编号不存在");
            throw new BusinessException("后置处理器用例编号不存在");
        }
        // 2.检查name唯一性
        String name = postProcessorDO.getName();
        Integer postProcessorId = postProcessorDO.getPostProcessorId();
        if (!checkPostProcessorName(postProcessorId, name).isEmpty()) {
            LOG.info("修改后置处理器时，后置处理器名称重复，name={}", name);
            throw new BusinessException("后置处理器名称重复");
        }
        postProcessorMapper.updatePostProcessor(postProcessorDO);
    }

    /**
     * 删除后置处理器
     *
     * @param postProcessorId postProcessorId
     */
    @Override
    public void removePostProcessorById(Integer postProcessorId) {
        postProcessorMapper.deletePostProcessorById(postProcessorId);
        LOG.debug("删除后置处理器，postProcessorId={}", postProcessorId);
    }

    /**
     * 根据caseId删除后置处理器
     *
     * @param caseId caseId
     */
    @Override
    public void removePostProcessorByCaseId(Integer caseId) {
        postProcessorMapper.deletePostProcessorByCaseId(caseId);
        LOG.debug("删除后置处理器，caseId={}", caseId);
    }
}
