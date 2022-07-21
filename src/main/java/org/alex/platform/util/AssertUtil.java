package org.alex.platform.util;

import org.alex.platform.exception.BusinessException;

import java.math.BigDecimal;

public class AssertUtil {

    //  操作符0/=、1/< 、2/>、3/<=、4/>=、5/in、6/!=、7/re、8/isNull、9/notNull、10/contains、11/isEmpty、12/isNotEmpty

    /**
     * 断言工具类
     *
     * @param result 结果
     * @param operator 操作符
     * @param exceptedResult 预期结果
     * @return 断言结果
     * @throws BusinessException 字符串不支持<、>、<=、>=操作
     */
    public static boolean asserts(String result, int operator, String exceptedResult) throws BusinessException {
        if (1 <= operator && operator <= 4) {
            try {
                new BigDecimal(result);
                new BigDecimal(exceptedResult);
            } catch (Exception e) {
                throw new BusinessException("不支持该操作符");
            }
        }
        boolean flag;
        switch (operator) {
            // =
            case 0: {
                BigDecimal resultDecimal;
                BigDecimal exceptedResultDecimal;
                // 未捕捉到异常则通过BigDecimal比较, 否则通过String比较, 采用该方式 1 == 1.0
                try {
                    resultDecimal = new BigDecimal(result);
                    exceptedResultDecimal = new BigDecimal(exceptedResult);
                    flag = resultDecimal.compareTo(exceptedResultDecimal) == 0;
                } catch (Exception e) {
                    flag = result.equals(exceptedResult);
                }
                break;
            }
            // <
            case 1: {
                BigDecimal resultDecimal = new BigDecimal(result);
                BigDecimal exceptedResultDecimal = new BigDecimal(exceptedResult);
                flag = resultDecimal.compareTo(exceptedResultDecimal) < 0;
                break;
            }
            // >
            case 2: {
                BigDecimal resultDecimal = new BigDecimal(result);
                BigDecimal exceptedResultDecimal = new BigDecimal(exceptedResult);
                flag = resultDecimal.compareTo(exceptedResultDecimal) > 0;
                break;
            }
            // <=
            case 3: {
                BigDecimal resultDecimal = new BigDecimal(result);
                BigDecimal exceptedResultDecimal = new BigDecimal(exceptedResult);
                flag = resultDecimal.compareTo(exceptedResultDecimal) <= 0;
                break;
            }
            // >=
            case 4: {
                BigDecimal resultDecimal = new BigDecimal(result);
                BigDecimal exceptedResultDecimal = new BigDecimal(exceptedResult);
                flag = resultDecimal.compareTo(exceptedResultDecimal) >= 0;
                break;
            }
            // in
            case 5: {
                flag = exceptedResult.contains(result);
                break;
            }
            // !=
            case 6: {
                flag = !result.equals(exceptedResult);
                break;
            }
            // re
            case 7: {
                flag = result.matches(exceptedResult);
                break;
            }
            // null
            case 8: {
                flag = result == null;
                break;
            }
            // not null
            case 9: {
                flag = result != null;
                break;
            }
            // contains
            case 10: {
                flag = result.contains(exceptedResult);
                break;
            }
            // isEmpty
            case 11: {
                flag = result.isEmpty();
                break;
            }
            // isNotEmpty
            case 12: {
                flag = !result.isEmpty();
                break;
            }
            default:
                throw new BusinessException("不支持该操作符");
        }
        return flag;
    }
}
