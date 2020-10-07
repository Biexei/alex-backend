package org.alex.platform.mapper;

import org.alex.platform.pojo.UserDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<UserDO> selectUserList(UserDO userDO);

    List<UserDO> checkUser(UserDO userDO);

    void updateUser(UserDO userDO);

    void insertUser(UserDO userDO);

    void deleteUser(Integer userId);

    UserDO selectUserToLogin(UserDO userDO);

    UserDO selectUserById(Integer userId);

    UserDO selectUserByName(String username);

    UserDO selectUserByJobNumber(Integer jobNumber);
}
