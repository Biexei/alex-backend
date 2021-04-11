package org.alex.platform.service.impl;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.service.MailService;
import org.alex.platform.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;
    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

    /**
     * 发送邮件
     *
     * @param subject 标题
     * @param text    内容
     * @param to      收件地址
     * @throws BusinessException BusinessException
     */
    public void send(String subject, String text, String... to) throws BusinessException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        try {
            LOG.info("--------------------开始发送邮件---------------------");
            javaMailSender.send(simpleMailMessage);
            LOG.info("--------------------发送邮件成功---------------------");
        } catch (MailException e) {
            LOG.info("--------------------发送邮件失败，errorMsg={}---------------------", ExceptionUtil.msg(e));
            throw new BusinessException("发送邮件失败," + ExceptionUtil.msg(e));
        }
    }
}
