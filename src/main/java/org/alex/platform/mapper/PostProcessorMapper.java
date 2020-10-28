package org.alex.platform.mapper;

import org.alex.platform.pojo.PostProcessorDO;
import org.alex.platform.pojo.PostProcessorDTO;
import org.alex.platform.pojo.PostProcessorVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostProcessorMapper {
    PostProcessorVO selectPostProcessorByName(String name);

    PostProcessorVO selectPostProcessorById(Integer postProcessorId);

    List<PostProcessorVO> checkPostProcessorName(@Param("postProcessorId") Integer postProcessorId, @Param("name") String name);

    List<PostProcessorVO> selectPostProcessorList(PostProcessorDTO postProcessorDTO);

    Integer insertPostProcessor(PostProcessorDO postProcessorDO);

    void updatePostProcessor(PostProcessorDO postProcessorDO);

    void deletePostProcessorById(Integer postProcessorId);

}
