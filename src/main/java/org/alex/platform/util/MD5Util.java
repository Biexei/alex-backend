package org.alex.platform.util;

import java.security.MessageDigest;

public class MD5Util {

    private static final String[] HEX_DIG_ITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * MD5加密
     *
     * @param origin      字符
     * @param charsetName 编码
     * @return md5结果
     */
    public static String MD5Encode(String origin, String charsetName) {
        String resultString;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (null == charsetName || "".equals(charsetName)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return resultString;
    }

    /**
     * 登录密码专用MD5
     * @param password 密码明文
     * @return md5结果
     */
    public static String md5ForLoginPassword(String password) {
        return MD5Encode(NoUtil.PWD_SALT + password, "utf-8");
    }


    public static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            sb.append(byteToHexString(value));
        }
        return sb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIG_ITS[d1] + HEX_DIG_ITS[d2];
    }

}