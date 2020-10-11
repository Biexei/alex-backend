package org.alex.platform.service.impl;

import org.alex.platform.mapper.AnalysisMapper;
import org.alex.platform.pojo.AnalysisCountVO;
import org.alex.platform.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class AnalysisServiceImpl implements AnalysisService {
    @Autowired
    AnalysisMapper analysisMapper;
    @Override
    public Integer userCount() {
        return analysisMapper.userCount();
    }

    @Override
    public Integer suiteCount() {
        return analysisMapper.suiteCount();
    }

    @Override
    public Integer caseCount() {
        return analysisMapper.caseCount();
    }

    @Override
    public Integer assertCount() {
        return analysisMapper.assertCount();
    }

    @Override
    public ArrayList<AnalysisCountVO> registerWeek() {
        return analysisMapper.registerWeek();
    }

    @Override
    public ArrayList<AnalysisCountVO> caseWeek() {
        return analysisMapper.caseWeek();
    }

    @Override
    public ArrayList<AnalysisCountVO> assertWeek() {
        return analysisMapper.assertWeek();
    }

    @Override
    public ArrayList<AnalysisCountVO> suiteWeek() {
        return analysisMapper.suiteWeek();
    }

    @Override
    public ArrayList<String> selectExecuteLog(String ymd) {
        return analysisMapper.selectExecuteLog(ymd);
    }

    @Override
    public ArrayList<String> selectAssertLog(String ymd) {
        return analysisMapper.selectAssertLog(ymd);
    }


}
