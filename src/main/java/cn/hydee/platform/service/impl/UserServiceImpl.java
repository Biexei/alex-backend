package cn.hydee.platform.service.impl;

import cn.hydee.platform.mapper.UserMapper;
import cn.hydee.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public ArrayList<HashMap> getAllUser() {
        return userMapper.getAllUser();
    }
}
