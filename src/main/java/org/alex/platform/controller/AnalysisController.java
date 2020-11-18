package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.AnalysisCountVO;
import org.alex.platform.service.AnalysisService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AnalysisController {
    @Autowired
    AnalysisService analysisService;

    /**
     * 统计数据，用户总数，套件总数，断言总数，用例总数
     *
     * @return Result
     */
    @GetMapping("/count")
    public Result countAll() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        Integer userCount = analysisService.userCount();
        Integer suiteCount = analysisService.suiteCount();
        Integer assertCount = analysisService.assertCount();
        Integer caseCount = analysisService.caseCount();
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("type", "用户");
        userMap.put("total", userCount);


        HashMap<String, Object> caseMap = new HashMap<>();
        caseMap.put("type", "用例");
        caseMap.put("total", caseCount);

        HashMap<String, Object> suiteMap = new HashMap<>();
        suiteMap.put("type", "套件");
        suiteMap.put("total", suiteCount);

        HashMap<String, Object> assertMap = new HashMap<>();
        assertMap.put("type", "断言");
        assertMap.put("total", assertCount);


        list.add(userMap);
        list.add(caseMap);
        list.add(assertMap);
        list.add(suiteMap);

        return Result.success(list);
    }

    /**
     * 统计数据，用户总数，套件总数，断言总数，用例总数
     *
     * @return Result
     */
    @GetMapping("/count/group")
    public Result countGroup() {
        Integer userCount = analysisService.userCount();
        Integer suiteCount = analysisService.suiteCount();
        Integer assertCount = analysisService.assertCount();
        Integer caseCount = analysisService.caseCount();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("user", userCount);
        map.put("ifSuite", suiteCount);
        map.put("ifAssert", assertCount);
        map.put("ifCase", caseCount);
        return Result.success(map);
    }

    /**
     * 近7天用户注册数量
     *
     * @return Result
     */
    @GetMapping("/week/register")
    public Result registerWeek() {
        LinkedList<TreeMap<String, Object>> list = new LinkedList<>();
        TreeMap<String, Integer> days = interval7Days();
        ArrayList<AnalysisCountVO> registerWeek = analysisService.registerWeek();
        for (AnalysisCountVO analysisCountVO : registerWeek) {
            String date = analysisCountVO.getDate();
            Integer count = analysisCountVO.getCount();
            days.put(date, count);
        }
        for (Map.Entry entry : days.entrySet()) {
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("日期", entry.getKey());
            map.put("新增用户", entry.getValue());
            list.add(map);
        }
        return Result.success(list);
    }

    /**
     * 近7天用例新增数
     *
     * @return Result
     */
    @GetMapping("/week/case")
    public Result caseWeek() {
        LinkedList<TreeMap<String, Object>> list = new LinkedList<>();
        TreeMap<String, Integer> days = interval7Days();
        ArrayList<AnalysisCountVO> registerWeek = analysisService.caseWeek();
        for (AnalysisCountVO analysisCountVO : registerWeek) {
            String date = analysisCountVO.getDate();
            Integer count = analysisCountVO.getCount();
            days.put(date, count);
        }
        for (Map.Entry entry : days.entrySet()) {
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("日期", entry.getKey());
            map.put("新增用例", entry.getValue());
            list.add(map);
        }
        return Result.success(list);
    }

    /**
     * 近7天断言新增数
     *
     * @return Result
     */
    @GetMapping("/week/assert")
    public Result assertWeek() {
        LinkedList<TreeMap<String, Object>> list = new LinkedList<>();
        TreeMap<String, Integer> days = interval7Days();
        ArrayList<AnalysisCountVO> registerWeek = analysisService.assertWeek();
        for (AnalysisCountVO analysisCountVO : registerWeek) {
            String date = analysisCountVO.getDate();
            Integer count = analysisCountVO.getCount();
            days.put(date, count);
        }
        for (Map.Entry entry : days.entrySet()) {
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("日期", entry.getKey());
            map.put("新增断言", entry.getValue());
            list.add(map);
        }
        return Result.success(list);
    }


    /**
     * 近7天测试套件新增数
     *
     * @return Result
     */
    @GetMapping("/week/suite")
    public Result suiteWeek() {
        LinkedList<TreeMap<String, Object>> list = new LinkedList<>();
        TreeMap<String, Integer> days = interval7Days();
        ArrayList<AnalysisCountVO> registerWeek = analysisService.suiteWeek();
        for (AnalysisCountVO analysisCountVO : registerWeek) {
            String date = analysisCountVO.getDate();
            Integer count = analysisCountVO.getCount();
            days.put(date, count);
        }
        for (Map.Entry entry : days.entrySet()) {
            TreeMap<String, Object> map = new TreeMap<>();
            map.put("日期", entry.getKey());
            map.put("新增测试套件", entry.getValue());
            list.add(map);
        }
        return Result.success(list);
    }

    /**
     * 近7天用例执行情况统计
     *
     * @return Result
     */
    @GetMapping("/week/executeLog")
    public Result executeLogWeek() {
        LinkedList<TreeMap<String, Object>> result = new LinkedList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        for (int i = 6; i >= 0; i--) {
            TreeMap<String, Object> map = new TreeMap<>();
            String ymd = format.format(DateUtils.addDays(date, -i));
            ArrayList<String> list = analysisService.selectExecuteLog(ymd);
            map.put("日期", ymd);
            map.put("执行通过", list.get(0));
            map.put("执行失败", list.get(1));
            map.put("执行错误", list.get(2));
            result.add(map);
        }
        return Result.success(result);
    }

    /**
     * 近7天断言执行情况统计
     *
     * @return Result
     */
    @GetMapping("/week/assertLog")
    public Result assertLogWeek() {
        LinkedList<TreeMap<String, Object>> result = new LinkedList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        for (int i = 6; i >= 0; i--) {
            TreeMap<String, Object> map = new TreeMap<>();
            String ymd = format.format(DateUtils.addDays(date, -i));
            ArrayList<String> list = analysisService.selectAssertLog(ymd);
            map.put("日期", ymd);
            map.put("断言通过", list.get(0));
            map.put("断言失败", list.get(1));
            map.put("断言错误", list.get(2));
            result.add(map);
        }
        return Result.success(result);
    }

    public static TreeMap<String, Integer> interval7Days() {
        TreeMap<String, Integer> map = new TreeMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        for (int i = 6; i >= 0; i--) {
            map.put(format.format(DateUtils.addDays(date, -i)), 0);
        }
        return map;
    }
}
