package org.alex.platform.service;

import org.alex.platform.pojo.User;
import com.github.pagehelper.PageInfo;


public interface UserService {
    PageInfo<User> findUserList(User user, Integer pageNum, Integer pageSize);
    void modifyUser(User user);
    Boolean saveUser(User user);
    User findUserToLogin(User user);
    User findUserById(Integer userId);
}
