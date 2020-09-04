package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.DbMapper;
import org.alex.platform.pojo.DbDO;
import org.alex.platform.pojo.DbDTO;
import org.alex.platform.pojo.DbVO;
import org.alex.platform.service.DbService;
import org.alex.platform.util.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DbServiceImpl implements DbService {
    @Autowired
    DbMapper dbMapper;

    @Override
    public void saveDb(DbDO dbDO) throws BusinessException {
        String name = dbDO.getName();
        Date date = new Date();
        dbDO.setCreatedTime(date);
        dbDO.setUpdateTime(date);
        if (null != this.findDbByName(name)) {
            throw new BusinessException("name已存在");
        }
        dbMapper.insertDb(dbDO);
    }

    @Override
    public void modifyDb(DbDO dbDO) throws BusinessException {
        dbDO.setUpdateTime(new Date());
        dbMapper.updateDb(dbDO);
    }

    @Override
    public void removeDbById(Integer dbId) {
        dbMapper.deleteDbById(dbId);
    }

    @Override
    public DbVO findDbById(Integer id) {
        return dbMapper.selectDbById(id);
    }

    @Override
    public DbVO findDbByName(String name) {
        return dbMapper.selectDbByName(name);
    }

    @Override
    public PageInfo<DbVO> findDbList(DbDTO dbDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(dbMapper.selectDbList(dbDTO));
    }

    @Override
    public String dbConnectInfo(Integer dbId) {
        DbVO dbVO = this.findDbById(dbId);
        String url = dbVO.getUrl();
        String username = dbVO.getUsername();
        String password = dbVO.getPassword();
        return JdbcUtil.checkJdbcConnection(url, username, password);
    }
}
