package cn.hydee.platform.service;

import cn.hydee.platform.pojo.User;
import com.github.pagehelper.PageInfo;



public interface UserService {
    PageInfo<User> listUsers(Integer pageNum, Integer pageSize);
    Integer updateUser(User user);
    Boolean saveUser(User user);
    User getUserById(Integer userId);
}
