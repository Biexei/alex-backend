package org.alex.platform.mapper;

import org.alex.platform.pojo.PostProcessorDO;
import org.alex.platform.pojo.PostProcessorLogDO;
import org.alex.platform.pojo.PostProcessorLogDTO;
import org.alex.platform.pojo.PostProcessorLogVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostProcessorLogMapper {
    PostProcessorLogVO selectPostProcessorLogById(Integer id);

    List<PostProcessorLogVO> selectPostProcessorLogList(PostProcessorLogDTO postProcessorLogDTO);

    Integer insertPostProcessorLog(PostProcessorLogDO PostProcessorLogDO);
}
