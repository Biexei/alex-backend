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
        Integer userCount = analysisService.userCount();
        Integer suiteCount = analysisService.suiteCount();
        Integer assertCount = analysisService.assertCount();
        Integer caseCount = analysisService.caseCount();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("userCount", userCount);
        map.put("suiteCount", suiteCount);
        map.put("assertCount", assertCount);
        map.put("caseCount", caseCount);
        return Result.success(map);
    }

    @GetMapping("/week/register")
    public Result registerWeek() {
        TreeMap<String, Integer> days = interval7Days();
        ArrayList<AnalysisCountVO> registerWeek = analysisService.registerWeek();
        for (AnalysisCountVO analysisCountVO : registerWeek) {
            String date = analysisCountVO.getDate();
            Integer count = analysisCountVO.getCount();
            days.put(date, count);
        }
        return Result.success(days);
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
