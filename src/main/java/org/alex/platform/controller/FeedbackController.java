package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.FeedbackDO;
import org.alex.platform.pojo.FeedbackDTO;
import org.alex.platform.pojo.FeedbackVO;
import org.alex.platform.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    LoginUserInfo loginUserInfo;

    /**
     * 新增反馈信息
     * @param request request
     * @param feedbackDO feedbackDO
     * @return Result
     */
    @PostMapping("/feedback/save")
    public Result saveFeedback(HttpServletRequest request, FeedbackDO feedbackDO) {
        int userId = loginUserInfo.getUserId(request);
        String realName = loginUserInfo.getRealName(request);
        feedbackDO.setStatus((byte) 0); // 0未读 1已读未回 2已回待阅  3已回已阅
        feedbackDO.setCreatorId(userId);
        feedbackDO.setCreatorName(realName);
        feedbackService.saveFeedback(feedbackDO);
        return Result.success("新增成功");
    }

    /**
     * 回复反馈消息
     * @param feedbackDO feedbackDO
     * @return Result
     */
    @PostMapping("/feedback/reply")
    public Result replyFeedback(FeedbackDO feedbackDO) {
        Integer id = feedbackDO.getId();
        String reply = feedbackDO.getReply();
        Byte solution = feedbackDO.getSolution();
        FeedbackDO update = new FeedbackDO();
        byte status; // 0未读 1已读未回 2已回待阅  3已回已阅
        if (reply == null || "".equals(reply)) {
            status = 1;
            solution = 3;
        } else {
            update.setReply(reply);
            update.setReplyTime(new Date());
            status = 2;
        }
        update.setId(id);
        update.setSolution(solution);
        update.setStatus(status);
        feedbackService.modifyFeedback(update);
        return Result.success("回复成功");
    }

    /**
     * 修改反馈消息
     * @param feedbackDO feedbackDO
     * @return Result
     */
    @PostMapping("/feedback/modify")
    public Result modifyFeedback(FeedbackDO feedbackDO) {
        feedbackDO.setStatus((byte)0);
        feedbackDO.setSolution((byte)3);
        feedbackService.modifyFeedback(feedbackDO);
        return Result.success("修改成功");
    }

    /**
     * 查看反馈列表
     * @param feedbackDTO feedbackDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return Result
     */
    @GetMapping("/feedback")
    public Result findFeedback(FeedbackDTO feedbackDTO, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return Result.success(feedbackService.findFeedback(feedbackDTO, pageNum, pageSize));
    }

    /**
     * 查看我的反馈列表
     * @param feedbackDTO feedbackDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return Result
     */
    @GetMapping("/myFeedback")
    public Result findMyFeedback(HttpServletRequest request, FeedbackDTO feedbackDTO, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        int userId = loginUserInfo.getUserId(request);
        feedbackDTO.setCreatorId(userId);
        return Result.success(feedbackService.findFeedback(feedbackDTO, pageNum, pageSize));
    }

    /**
     * 查看反馈详情
     * @param id id
     * @return Result
     */
    @GetMapping("/feedback/{id}")
    public Result findFeedbackById(@PathVariable Integer id) {
        Byte status; // 0未读 1已读未回 2已回待阅 3已回已阅
        FeedbackVO feedback = feedbackService.findFeedbackById(id);
        if (feedback != null) {
            status = feedback.getStatus();
            if (status == 0) {
                status = 1;
            }
            FeedbackDO feedbackDO = new FeedbackDO();
            feedbackDO.setId(id);
            feedbackDO.setStatus(status);
            feedbackService.modifyFeedback(feedbackDO);
        }
        return Result.success(feedbackService.findFeedbackById(id));
    }

    /**
     * 查看我的反馈详情
     * @param id id
     * @return Result
     */
    @GetMapping("/myFeedback/{id}")
    public Result findMyFeedbackById(@PathVariable Integer id) {
        Byte status; // 0未读 1已读未回 2已回待阅 3已回已阅
        FeedbackVO feedback = feedbackService.findFeedbackById(id);
        if (feedback != null) {
            status = feedback.getStatus();
            if (status == 2) {
                status = 3;
            }
            FeedbackDO feedbackDO = new FeedbackDO();
            feedbackDO.setId(id);
            feedbackDO.setStatus(status);
            feedbackService.modifyFeedback(feedbackDO);
        }
        return Result.success(feedbackService.findFeedbackById(id));
    }

    /**
     * 删除反馈
     * @param id id
     * @return Result
     */
    @GetMapping("/feedback/remove/{id}")
    public Result removeFeedback(HttpServletRequest request, @PathVariable Integer id) throws BusinessException {
        int userId = loginUserInfo.getUserId(request);
        FeedbackVO feedback = feedbackService.findFeedbackById(id);
        if (feedback != null) {
            if (userId == feedback.getCreatorId()) {
                // 只准删自己的反馈
                feedbackService.removeFeedback(id);
            } else {
                throw new BusinessException("权限不足");
            }
        }
        return Result.success();
    }
}
