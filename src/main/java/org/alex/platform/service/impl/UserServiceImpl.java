package org.alex.platform.service.impl;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.UserMapper;
import org.alex.platform.pojo.UserDO;
import org.alex.platform.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 查看用户列表
     * @param userDO userDO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo<UserDO>
     */
    @Override
    public PageInfo<UserDO> findUserList(UserDO userDO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(userMapper.selectUserList(userDO));
    }

    /**
     * 修改用户信息
     * @param userDO userDO
     */
    @Override
    public void modifyUser(UserDO userDO) throws BusinessException {
        // 用户名不能重复
        if (!userMapper.checkUser(userDO).isEmpty()) {
            throw new BusinessException("用户名已存在");
        }
        userMapper.updateUser(userDO);
    }

    /**
     * 添加用户
     * @param userDO
     * @return
     */
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

    /**
     * 用户登录
     * @param userDO userDO
     * @return UserDO
     */
    @Override
    public UserDO findUserToLogin(UserDO userDO) {
        return userMapper.selectUserToLogin(userDO);
    }

    /**
     * 修改用户信息
     * @param userId 用户编号
     * @return UserDO
     */
    @Override
    public UserDO findUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 删除用户
     * @param userId 用户编号
     */
    @Override
    public void removeUserById(Integer userId) {
        userMapper.deleteUser(userId);
    }
}
