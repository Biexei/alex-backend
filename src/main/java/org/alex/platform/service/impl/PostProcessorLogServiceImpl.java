package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.PostProcessorLogMapper;
import org.alex.platform.pojo.PostProcessorLogDO;
import org.alex.platform.pojo.PostProcessorLogDTO;
import org.alex.platform.pojo.PostProcessorLogVO;
import org.alex.platform.service.PostProcessorLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostProcessorLogServiceImpl implements PostProcessorLogService {
    @Autowired
    private PostProcessorLogMapper postProcessorLogMapper;

    private static final Logger LOG = LoggerFactory.getLogger(PostProcessorLogServiceImpl.class);

    /**
     * 获取调用后置处理器详情
     *
     * @param id 后置处理器调用日志编号
     * @return PostProcessorLogVO
     */
    @Override
    public PostProcessorLogVO findPostProcessorLogById(Integer id) {
        return postProcessorLogMapper.selectPostProcessorLogById(id);
    }

    /**
     * 获取后置处理器调用日志列表
     *
     * @param postProcessorLogDTO postProcessorLogDTO
     * @param pageNum             pageNum
     * @param pageSize            pageSize
     * @return PageInfo<PostProcessorLogVO>
     */
    @Override
    public PageInfo<PostProcessorLogVO> findPostProcessorLogList(PostProcessorLogDTO postProcessorLogDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(postProcessorLogMapper.selectPostProcessorLogList(postProcessorLogDTO));
    }

    /**
     * 获取后置处理器调用日志列表不分页
     *
     * @param postProcessorLogDTO postProcessorLogDTO
     * @return List<PostProcessorLogVO>
     */
    @Override
    public List<PostProcessorLogVO> findPostProcessorLogListAll(PostProcessorLogDTO postProcessorLogDTO) {
        return postProcessorLogMapper.selectPostProcessorLogList(postProcessorLogDTO);
    }

    /**
     * 新增后置调用处理器调用日志，并返回自增对象
     *
     * @param postProcessorLogDO postProcessorLogDO
     * @return 自增对象
     */
    @Override
    public PostProcessorLogDO savePostProcessorLog(PostProcessorLogDO postProcessorLogDO) {
        postProcessorLogDO.setCreatedTime(new Date());
        postProcessorLogMapper.insertPostProcessorLog(postProcessorLogDO);
        LOG.info("后置处理器日志新增成功");
        return postProcessorLogDO;
    }
}
