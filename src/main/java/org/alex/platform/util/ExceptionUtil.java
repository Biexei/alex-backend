package org.alex.platform.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ExceptionUtil {
    public static String msg(Exception e) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(stream);
        e.printStackTrace(printStream);
        String s = new String(stream.toByteArray());
        printStream.close();
        try {
            stream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return s;
    }
}
