package org.alex.platform.mapper;

import org.alex.platform.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> listUsers();
    Integer updateUser(User user);
    Integer insertUser(User user);
    User getUserById(Integer userId);
    User getUserByName(String username);
    User getUserByJobNumber(Integer jobNumber);
}
