package com.magareto.yorick.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BaseUtils {
    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }
}
