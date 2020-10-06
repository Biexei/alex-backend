package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.DbMapper;
import org.alex.platform.mapper.RelyDataMapper;
import org.alex.platform.pojo.DbDO;
import org.alex.platform.pojo.DbDTO;
import org.alex.platform.pojo.DbVO;
import org.alex.platform.pojo.RelyDataDTO;
import org.alex.platform.service.DbService;
import org.alex.platform.util.JdbcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DbServiceImpl implements DbService {
    @Autowired
    DbMapper dbMapper;
    @Autowired
    RelyDataMapper relyDataMapper;
    private static final Logger LOG = LoggerFactory.getLogger(DbServiceImpl.class);

    /**
     * 添加数据源
     *
     * @param dbDO dbDO
     * @throws BusinessException 数据源名称重复检测
     */
    @Override
    public void saveDb(DbDO dbDO) throws BusinessException {
        String name = dbDO.getName();
        Date date = new Date();
        dbDO.setCreatedTime(date);
        dbDO.setUpdateTime(date);
        if (null != this.findDbByName(name)) {
            LOG.warn("新增数据源，数据源名称重复");
            throw new BusinessException("该数据源名称已存在");
        }
        dbMapper.insertDb(dbDO);
    }

    /**
     * 修改数据源
     *
     * @param dbDO dbDO
     * @throws BusinessException 数据源名称重复检测
     */
    @Override
    public void modifyDb(DbDO dbDO) throws BusinessException {
        dbDO.setUpdateTime(new Date());
        if (!dbMapper.checkDbName(dbDO).isEmpty()) {
            LOG.warn("修改数据源，数据源名称重复");
            throw new BusinessException("该数据源名称已存在");
        }
        dbMapper.updateDb(dbDO);
    }

    /**
     * 删除数据源
     *
     * @param dbId 数据源编号
     * @throws BusinessException 数据源若被依赖数据使用不允许删除
     */
    @Override
    public void removeDbById(Integer dbId) throws BusinessException {
        RelyDataDTO relyDataDTO = new RelyDataDTO();
        relyDataDTO.setDatasourceId(dbId);
        if (!relyDataMapper.selectRelyDataList(relyDataDTO).isEmpty()) {
            LOG.warn("删除数据源，请先删除数据中心相关依赖");
            throw new BusinessException("请先删除数据中心相关依赖");
        }
        dbMapper.deleteDbById(dbId);
    }

    /**
     * 获取数据源详情
     *
     * @param id 数据源编号
     * @return DbVO
     */
    @Override
    public DbVO findDbById(Integer id) {
        return dbMapper.selectDbById(id);
    }

    /**
     * 根据名称查找数据源
     *
     * @param name 数据源名称
     * @return DbVO
     */
    @Override
    public DbVO findDbByName(String name) {
        return dbMapper.selectDbByName(name);
    }

    /**
     * 获取数据源列表
     *
     * @param dbDTO    dbDTO
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return PageInfo<DbVO>
     */
    @Override
    public PageInfo<DbVO> findDbList(DbDTO dbDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(dbMapper.selectDbList(dbDTO));
    }

    /**
     * 检查数据源连通性
     *
     * @param dbId 数据源编号
     * @return 连接提示
     */
    @Override
    public String dbConnectInfo(Integer dbId) {
        DbVO dbVO = this.findDbById(dbId);
        String url = dbVO.getUrl();
        String username = dbVO.getUsername();
        String password = dbVO.getPassword();
        return JdbcUtil.checkJdbcConnection(url, username, password);
    }
}
