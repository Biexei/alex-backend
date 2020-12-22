package org.alex.platform.enums;

/**
 * 用例等级枚举
 */
public enum CaseLevel {

    HIGH("高", 0),
    MID("中", 1),
    LOW("低", 2);

    private final String levelName;
    private final Integer levelNum;

    CaseLevel(String levelName, Integer levelNum) {
        this.levelName = levelName;
        this.levelNum = levelNum;
    }
}
