package com.magareto.yorick.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class BaseUtils {
    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.toString();
    }

    public static String generateCommaSeparated(List<String> elements, boolean pretty) {
        StringBuilder elementBuilder = new StringBuilder();
        String template = null;

        template = "%s";

        if (pretty) {
            elementBuilder.append("`");
        }


        for (int i = 0; i < elements.size(); i++) {
            String result = elements.get(i);


            if (i > 0) {
                elementBuilder.append(String.format(template, ", " + result));
            } else {
                elementBuilder.append(String.format(template, result));
            }
        }

        if (pretty) {
            elementBuilder.append("`");
        }

        return elementBuilder.toString();

    }


}
