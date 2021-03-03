package org.alex.platform.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CommandUtil {
    public static String exec(String cmd) {
        Process process;
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec("cmd /c " + cmd);
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
