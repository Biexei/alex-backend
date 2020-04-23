package cn.hydee.platform.mapper;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface UserMapper {
    ArrayList<HashMap> getAllUser();
}
