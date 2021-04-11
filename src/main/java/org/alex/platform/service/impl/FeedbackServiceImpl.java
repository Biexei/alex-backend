package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.FeedbackMapper;
import org.alex.platform.pojo.FeedbackDO;
import org.alex.platform.pojo.FeedbackDTO;
import org.alex.platform.pojo.FeedbackVO;
import org.alex.platform.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 新增用户反馈
     * @param feedbackDO feedbackDO
     */
    @Override
    public void saveFeedback(FeedbackDO feedbackDO) {
        feedbackDO.setCreatedTime(new Date());
        feedbackMapper.insertFeedback(feedbackDO);
    }

    /**
     * 修改用户反馈
     * @param feedbackDO feedbackDO
     */
    @Override
    public void modifyFeedback(FeedbackDO feedbackDO) {
        if (feedbackDO.getReply() != null) {
            feedbackDO.setReplyTime(new Date());
        }
        feedbackMapper.updateFeedback(feedbackDO);
    }

    /**
     * 查询用户反馈列表
     * @param feedbackDTO feedbackDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo
     */
    @Override
    public PageInfo<FeedbackVO> findFeedback(FeedbackDTO feedbackDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(feedbackMapper.selectFeedback(feedbackDTO));
    }

    /**
     * 查看反馈详情
     * @param id id
     * @return FeedbackVO
     */
    @Override
    public FeedbackVO findFeedbackById(Integer id) {
        return feedbackMapper.selectFeedbackById(id);
    }

    /**
     * 删除用户反馈
     * @param id id
     */
    @Override
    public void removeFeedback(Integer id) {
        feedbackMapper.deleteFeedback(id);
    }
}
