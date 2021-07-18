package org.alex.platform.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {
    public static String encode(String text) {
        return Base64Util.encode(text, StandardCharsets.UTF_8);
    }

    public static String encode(String text, Charset charset) {
        return Base64.getEncoder().encodeToString(text.getBytes(charset));
    }

    public static String decode(String text) {
        return Base64Util.decode(text, StandardCharsets.UTF_8);
    }

    public static String decode(String text, Charset charset) {
        return new String(Base64.getDecoder().decode(text), charset);
    }
}
