package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSON;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.UserMapper;
import org.alex.platform.pojo.UserDO;
import org.alex.platform.pojo.UserVO;
import org.alex.platform.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.util.MD5Util;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    LoginUserInfo loginUserInfo;

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
                userDO.setPassword(MD5Util.md5ForLoginPassword(password));
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
        userDO.setRealName(username);
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
            userDO.setPassword(MD5Util.md5ForLoginPassword(password));
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
        String password = userDO.getPassword();
        userDO.setPassword(MD5Util.md5ForLoginPassword(password));
        return userMapper.selectUserToLogin(userDO);
    }

    /**
     * 查询用户信息
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
    public void removeUserById(HttpServletRequest request, Integer userId) throws BusinessException {
        if (userId == 1) {
            throw new BusinessException("内置用户禁止删除");
        }
        userMapper.deleteUser(userId);
        Object operator = this.loginUserInfo.getLoginUserInfo(request);
        LOG.warn("删除用户, 操作人信息为：{}", JSON.toJSONString(operator));
    }

    /**
     * 查询用户密码
     * @param userId 用户id
     * @return 密码
     */
    @Override
    public String findPwdByUserId(Integer userId) {
        return userMapper.selectPwdByUserId(userId);
    }

    /**
     * 修改用户密码
     * @param request request
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     */
    @Override
    public void changePwd(HttpServletRequest request, String oldPwd, String newPwd) throws BusinessException {
        if (oldPwd == null || newPwd == null) {
            throw new ValidException("密码错误");
        }
        ValidUtil.length(newPwd, 3, 20, "密码长度必须为3~20");
        HashMap<String, Object> map = (HashMap) loginUserInfo.getLoginUserInfo(request);
        Integer userId = (Integer)map.get("userId");
        String password = findPwdByUserId(userId);
        if (!MD5Util.md5ForLoginPassword(oldPwd).equals(password)) {
            throw new BusinessException("旧密码错误");
        }
        userMapper.updatePassword(userId, MD5Util.md5ForLoginPassword(newPwd));
    }

    /**
     * 重置密码
     * @param userId 用户id
     * @param password 密码
     * @throws BusinessException 异常
     */
    @Override
    public void pwdReset(Integer userId, String password) throws BusinessException {
        if (userId == 1) {
            throw new BusinessException("内置用户禁止修改");
        }
        userMapper.pwdReset(userId, password);
    }
}
