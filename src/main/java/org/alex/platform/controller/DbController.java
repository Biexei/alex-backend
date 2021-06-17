package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.DbDO;
import org.alex.platform.pojo.DbDTO;
import org.alex.platform.pojo.DbVO;
import org.alex.platform.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbController {
    @Autowired
    DbService dbService;

    /**
     * 添加数据源
     *
     * @param dbDO dbDO
     * @return Result
     * @throws BusinessException 数据源名称重复检测
     */
    @PostMapping("/db/save")
    public Result saveDb(@Validated DbDO dbDO) throws BusinessException {
        dbService.saveDb(dbDO);
        return Result.success("新增成功");
    }

    /**
     * 修改数据源
     *
     * @param dbDO dbDO
     * @return Result
     * @throws BusinessException 数据源名称重复检测
     */
    @PostMapping("/db/modify")
    public Result modifyDb(@Validated DbDO dbDO) throws BusinessException {
        dbService.modifyDb(dbDO);
        return Result.success("修改成功");
    }

    /**
     * 获取数据源编号详情
     *
     * @param id 数据源编号
     * @return Result
     */
    @GetMapping("/db/{id}")
    public Result findDbById(@PathVariable Integer id) {
        DbVO db = dbService.findDbById(id);
        db.setPassword(null);
        db.setDevPassword(null);
        db.setProdPassword(null);
        db.setStgPassword(null);
        db.setTestPassword(null);
        return Result.success(db);
    }

    /**
     * 获取数据源列表
     *
     * @param dbDTO    dbDTO
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return Result
     */
    @GetMapping("/db")
    public Result findDbList(DbDTO dbDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(dbService.findDbList(dbDTO, num, size));
    }

    /**
     * 删除数据源
     *
     * @param id 数据源编号
     * @return Result
     * @throws BusinessException 数据源若被依赖数据使用不允许删除
     */
    @GetMapping("/db/remove/{id}")
    public Result removeDb(@PathVariable Integer id) throws BusinessException {
        dbService.removeDbById(id);
        return Result.success();
    }

    /**
     * 数据源连通性检查
     *
     * @param id 数据源编号
     * @return Result
     */
    @GetMapping("/db/check/{id}")
    public Result checkDb(@PathVariable Integer id) {
        String msg = dbService.dbConnectInfo(id);
        return Result.success(msg);
    }
}
