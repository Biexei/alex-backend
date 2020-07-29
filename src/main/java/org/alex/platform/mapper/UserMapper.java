package org.alex.platform.mapper;

import org.alex.platform.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> selectUserList(User user);
    void updateUser(User user);
    void insertUser(User user);
    User selectUserToLogin(User user);
    User selectUserById(Integer userId);
    User selectUserByName(String username);
    User selectUserByJobNumber(Integer jobNumber);
}
