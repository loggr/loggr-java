package net.loggr.utility;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class Common {
    private Common() {
    }

    public static boolean isNullOrEmpty(String str) {
        return StringUtils.isBlank(str) || StringUtils.isBlank(str.trim());
    }

    public static int getLastIndexOf(String type, char c) {

        if (StringUtils.isBlank(type)) {
            return -1;
        }

        int lastIndex = -1;

        int curIdx;


        for (int i = 0; i < type.length(); i++) {

            String tmp = type.substring(i);

            curIdx = tmp.indexOf(c);

            if (curIdx == -1)
                break;

            lastIndex = curIdx;
        }


        return lastIndex;
    }

    public static String join(String separator, String[] values) {
        return StringUtils.join(values, separator);
    }

    public static String[] removeEmptyEntries(String[] tokens) {
        ArrayList<String> values = new ArrayList<String>();
        for (String token : tokens) {
            if (StringUtils.isNotBlank(token)
                    && StringUtils.isNotBlank(token.trim())) {
                values.add(token);
            }
        }

        String[] retVal = new String[values.size()];
        retVal = values.toArray(retVal);

        return (retVal);
    }
}
