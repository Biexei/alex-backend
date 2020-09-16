package org.alex.platform.service.impl;

import org.alex.platform.mapper.UserMapper;
import org.alex.platform.pojo.UserDO;
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
    public PageInfo<UserDO> findUserList(UserDO userDO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(userMapper.selectUserList(userDO));
    }

    @Override
    public void modifyUser(UserDO userDO) {
        userMapper.updateUser(userDO);
    }

    @Override
    public Boolean saveUser(UserDO userDO) {
        String username = userDO.getUsername();
        if (userMapper.selectUserByName(username) != null) {
            return false;
        } else {
            userMapper.insertUser(userDO);
            return true;
        }
    }

    @Override
    public UserDO findUserToLogin(UserDO userDO) {
        return userMapper.selectUserToLogin(userDO);
    }

    @Override
    public UserDO findUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public void removeUserById(Integer userId) {
        userMapper.deleteUser(userId);
    }
}
