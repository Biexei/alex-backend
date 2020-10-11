package org.alex.platform.mapper;

import org.alex.platform.pojo.AnalysisCountVO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public interface AnalysisMapper {
    Integer userCount();
    Integer suiteCount();
    Integer caseCount();
    Integer assertCount();

    ArrayList<AnalysisCountVO> registerWeek();
    ArrayList<AnalysisCountVO> caseWeek();
    ArrayList<AnalysisCountVO> assertWeek();
    ArrayList<AnalysisCountVO> suiteWeek();

    ArrayList<String> selectExecuteLog(String ymd);
    ArrayList<String> selectAssertLog(String ymd);
}
