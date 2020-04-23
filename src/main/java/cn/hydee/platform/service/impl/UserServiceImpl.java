package cn.hydee.platform.service.impl;

import cn.hydee.platform.mapper.UserMapper;
import cn.hydee.platform.pojo.User;
import cn.hydee.platform.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public PageInfo<User> listUsers(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(userMapper.listUsers());
    }

    @Override
    public Integer updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public Boolean saveUser(User user) {
        String username = user.getUsername();
        Integer jobNumber = user.getJobNumber();
        if (userMapper.getUserByJobNumber(jobNumber) != null){
            return false;
        }
        if (userMapper.getUserByName(username) != null){
            return false;
        }
        else{
            userMapper.insertUser(user);
            return true;
        }
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.getUserById(userId);
    }
}
