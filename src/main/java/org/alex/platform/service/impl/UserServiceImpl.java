package org.alex.platform.service.impl;

import org.alex.platform.mapper.UserMapper;
import org.alex.platform.pojo.User;
import org.alex.platform.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public PageInfo<User> findUserList(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(userMapper.selectUserList(user));
    }

    @Override
    public void modifyUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public Boolean saveUser(User user) {
        String username = user.getUsername();
        if (userMapper.selectUserByName(username) != null){
            return false;
        }
        else{
            userMapper.insertUser(user);
            return true;
        }
    }

    @Override
    public User findUserToLogin(User user) {
        return userMapper.selectUserToLogin(user);
    }

    @Override
    public User findUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }
}
