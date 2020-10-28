package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.PostProcessorLogDO;
import org.alex.platform.pojo.PostProcessorLogDTO;
import org.alex.platform.pojo.PostProcessorLogVO;

import java.util.List;

public interface PostProcessorLogService {
    PostProcessorLogVO findPostProcessorLogById(Integer id);

    PageInfo<PostProcessorLogVO> findPostProcessorLogList(PostProcessorLogDTO postProcessorLogDTO, Integer pageNum, Integer pageSize);

    List<PostProcessorLogVO> findPostProcessorLogListAll(PostProcessorLogDTO postProcessorLogDTO);

    PostProcessorLogDO savePostProcessorLog(PostProcessorLogDO postProcessorLogDO);
}
