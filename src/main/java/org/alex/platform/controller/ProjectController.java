package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.ProjectDO;
import org.alex.platform.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {

    @Autowired
    ProjectService projectService;

    private static final Logger LOG = LoggerFactory.getLogger(ProjectController.class);

    /**
     * 查询项目列表
     *
     * @param projectDO projectDO
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @return Result
     */
    @GetMapping("project/list")
    public Result getProjectList(ProjectDO projectDO, Integer pageNum, Integer pageSize) {
        Integer num = pageNum == null ? 1 : pageNum;
        Integer size = pageSize == null ? 10 : pageSize;
        return Result.success(projectService.findProjectList(projectDO, num, size));
    }

    /**
     * 获取所有项目信息
     *
     * @param projectDO projectDO
     * @return Result
     */
    @GetMapping("project/all")
    public Result getAllProject(ProjectDO projectDO) {
        return Result.success(projectService.findAllProject(projectDO));
    }

    /**
     * 查看项目下所有模块
     *
     * @param projectId 项目编号
     * @return Result
     */
    @GetMapping("project/modules/{projectId}")
    public Result findModulesById(@PathVariable Integer projectId) {
        return Result.success(projectService.findModulesById(projectId));
    }

    /**
     * 查看项目信息
     *
     * @param projectDO projectDO
     * @return Result
     */
    @GetMapping("project/info")
    public Result getProject(ProjectDO projectDO) {
        Integer projectId = projectDO.getProjectId();
        String projectName = projectDO.getName();
        ProjectDO p = new ProjectDO();
        p.setProjectId(projectId);
        p.setName(projectName);
        return Result.success(projectService.findProject(projectDO));
    }

    /**
     * 新增项目
     *
     * @param projectDO projectDO
     * @return Result
     */
    @PostMapping("project/save")
    public Result saveProject(@Validated ProjectDO projectDO) {
        try {
            projectService.saveProject(projectDO);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
        return Result.success("新增成功");
    }

    /**
     * 修改项目
     *
     * @param projectDO projectDO
     * @return Result
     */
    @PostMapping("project/modify")
    public Result modifyProject(@Validated ProjectDO projectDO) {
        try {
            projectService.modifyProject(projectDO);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
            return Result.fail(e.getMessage());
        }
        return Result.success("修改成功");
    }

    /**
     * 删除项目
     *
     * @param projectId 项目编号
     * @return Result
     */
    @GetMapping("project/remove/{projectId}")
    public Result deleteProject(@PathVariable Integer projectId) throws BusinessException {
        projectService.removeProjectById(projectId);
        return Result.success("删除成功");
    }

}
