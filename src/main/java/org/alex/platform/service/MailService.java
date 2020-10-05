package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;

public interface MailService {
    void send(String subject, String text, String... to) throws BusinessException;
}