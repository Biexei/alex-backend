package org.alex.platform.enums;

/**
 * 用例等级枚举
 */
public enum CaseLevel {

    HIGH("高", (byte)0),
    MID("中", (byte)1),
    LOW("低", (byte)2),

    HIGH_("0", (byte)0),
    MID_("1", (byte)1),
    LOW_("2", (byte)2);

    private final String levelName;
    private final Byte levelNum;

    CaseLevel(String levelName, Byte levelNum) {
        this.levelName = levelName;
        this.levelNum = levelNum;
    }

    public static Byte getLevelKey(String levelName) {
        for(CaseLevel caseLevel : CaseLevel.values()) {
            if (caseLevel.levelName.equalsIgnoreCase(levelName)) {
                return caseLevel.levelNum;
            }
        }
        return 0; //默认等级为0
    }

    public static void main(String[] args) {
        System.out.println(CaseLevel.HIGH.levelName);
    }
}
