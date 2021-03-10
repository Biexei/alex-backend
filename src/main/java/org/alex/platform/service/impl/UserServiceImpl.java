package org.alex.platform.service.impl;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.UserMapper;
import org.alex.platform.pojo.UserDO;
import org.alex.platform.pojo.UserVO;
import org.alex.platform.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


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
    public PageInfo<UserVO> findUserList(UserDO userDO, Integer pageNum, Integer pageSize) {
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
        Integer userId = userDO.getUserId();
        if (userId == 1) {
            throw new BusinessException("内置用户禁止修改");
        }
        userDO.setUpdateTime(new Date());
        userMapper.updateUser(userDO);
    }

    /**
     * 添加用户
     * @param userDO
     * @return
     */
    @Override
    public void saveUser(UserDO userDO) throws BusinessException {
        String username = userDO.getUsername();
        String password = userDO.getPassword();
        Integer roleId = userDO.getRoleId();
        Byte sex = userDO.getSex();
        Date date = new Date();
        userDO.setCreatedTime(date);
        userDO.setUpdateTime(date);
        userDO.setIsEnable((byte) 1);
        if (sex == null || roleId == null || password == null) {
            throw new BusinessException("请完善用户信息");
        } else {
            ValidUtil.length(password, 3, 20, "密码长度必须为3~20");
            if (userMapper.selectUserByName(username) != null) {
                throw new BusinessException("用户名已存在");
            } else {
                userMapper.insertUser(userDO);
            }
        }
    }

    /**
     * 用户注册
     * @param userDO 对象信息
     * @throws BusinessException
     */
    @Override
    public void registerUser(UserDO userDO) throws BusinessException {
        String username = userDO.getUsername();
        String password = userDO.getPassword();
        Date date = new Date();
        userDO.setCreatedTime(date);
        userDO.setUpdateTime(date);
        userDO.setIsEnable((byte) 1);
        userDO.setRoleId(null);
        userDO.setSex((byte) 1);
        ValidUtil.notNUll(password, "请完善用户信息");
        ValidUtil.length(password, 3, 20, "密码长度必须为3~20");
        if (userMapper.selectUserByName(username) != null) {
            throw new BusinessException("用户名已存在");
        } else {
            userMapper.insertUser(userDO);
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
    public UserVO findUserById(Integer userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 删除用户
     * @param userId 用户编号
     */
    @Override
    public void removeUserById(Integer userId) throws BusinessException {
        if (userId == 1) {
            throw new BusinessException("内置用户禁止删除");
        }
        userMapper.deleteUser(userId);
    }
}
