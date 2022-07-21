package org.alex.platform.enums;

public enum AssertOperator {

    EQ("=",(byte)0),
    LT("<",(byte)1),
    GT(">",(byte)2),
    LE("<=",(byte)3),
    GE(">=",(byte)4),
    IN("in",(byte)5),
    NE("!=",(byte)6),
    RE("re",(byte)7),

    //兼容模式
    EQ_("eq",(byte)0),
    EQ__("==",(byte)0),
    LT_("lt",(byte)1),
    GT_("gt",(byte)2),
    LE_("le",(byte)3),
    GE_("ge",(byte)4),
    IN_("in",(byte)5),
    NE_("ne",(byte)6),
    RE_("re",(byte)7),

    IS_NULL("isNull",(byte)8),
    IS_NULL_("is_null",(byte)8),
    NOT_NULL("notNull",(byte)9),
    NOT_NULL_("not_null",(byte)9),
    CONTAINS("contains",(byte)10),
    IS_EMPTY("isEmpty",(byte)11),
    IS_EMPTY_("is_empty",(byte)11),
    IS_NOT_EMPTY("isNotEmpty",(byte)12),
    IS_NOT_EMPTY_("is_not_empty",(byte)12);

    private final String assertOperatorName;
    private final Byte assertOperatorType;

    public static Byte getAssertOperatorKey(String assertOperatorName) {
        for(AssertOperator assertOperator : AssertOperator.values()) {
            if (assertOperator.assertOperatorName.equalsIgnoreCase(assertOperatorName)) {
                return assertOperator.assertOperatorType;
            }
        }
        return 0;
    }

    AssertOperator(String assertOperatorName, Byte assertOperatorType) {
        this.assertOperatorName = assertOperatorName;
        this.assertOperatorType = assertOperatorType;
    }
}
