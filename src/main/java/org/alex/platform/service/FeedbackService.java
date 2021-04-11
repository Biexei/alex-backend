package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.FeedbackDO;
import org.alex.platform.pojo.FeedbackDTO;
import org.alex.platform.pojo.FeedbackVO;

public interface FeedbackService {
    void saveFeedback(FeedbackDO feedbackDO);

    void modifyFeedback(FeedbackDO feedbackDO);

    PageInfo<FeedbackVO> findFeedback(FeedbackDTO feedbackDTO, Integer pageNum, Integer pageSize);

    FeedbackVO findFeedbackById(Integer id);

    void removeFeedback(Integer id);
}
