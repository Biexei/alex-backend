package org.alex.platform.mapper;

import org.alex.platform.pojo.FeedbackDO;
import org.alex.platform.pojo.FeedbackDTO;
import org.alex.platform.pojo.FeedbackVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackMapper {
    void insertFeedback(FeedbackDO feedbackDO);

    void updateFeedback(FeedbackDO feedbackDO);

    List<FeedbackVO> selectFeedback(FeedbackDTO feedbackDTO);

    FeedbackVO selectFeedbackById(Integer id);

    void deleteFeedback(Integer id);
}
