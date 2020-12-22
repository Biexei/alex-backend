package org.alex.platform.enums;

public enum AssertOperator {

    EQ("=",0),
    LT("<",1),
    GT(">",2),
    LE("<=",3),
    GE(">=",4),
    IN("in",5),
    NE("!=",6),
    RE("re",7),
    IS_NULL("isNull",8),
    NOT_NULL("notNull",9);

    private final String assertOperatorName;
    private final Integer assertOperatorType;

    AssertOperator(String assertOperatorName, Integer assertOperatorType) {
        this.assertOperatorName = assertOperatorName;
        this.assertOperatorType = assertOperatorType;
    }
}
