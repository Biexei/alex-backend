package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.DbDO;
import org.alex.platform.pojo.DbDTO;
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

    @PostMapping("/db/save")
    public Result saveDb(@Validated DbDO dbDO) throws BusinessException {
        dbService.saveDb(dbDO);
        return Result.success("新增成功");
    }

    @PostMapping("/db/modify")
    public Result modifyDb(@Validated DbDO dbDO) throws BusinessException {
        dbService.modifyDb(dbDO);
        return Result.success("修改成功");
    }

    @GetMapping("/db/{id}")
    public Result selectDbById(@PathVariable Integer id) {
        return Result.success(dbService.findDbById(id));
    }

    @GetMapping("/db")
    public Result selectDbList(DbDTO dbDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(dbService.findDbList(dbDTO, num, size));
    }

    @GetMapping("/db/remove/{id}")
    public Result removeDb(@PathVariable Integer id) {
        dbService.removeDbById(id);
        return Result.success();
    }
}
