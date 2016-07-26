package net.loggr.utility;

import java.util.ArrayList;

public class Tags {

    public static String[] tokenizeAndFormat(String[] tags) {
        return (tokenizeAndFormat(tags, true));
    }

    private static String[] tokenizeAndFormat(String[] tags, boolean stripSpecialChars) {
        return tokenizeAndFormat(Common.join(" ", tags), stripSpecialChars);
    }

    public static String[] tokenizeAndFormat(String tagstring) {
        return (tokenizeAndFormat(tagstring, true));
    }

    private static String[] tokenizeAndFormat(String tagstring, boolean stripSpecialChars) {
        ArrayList<String> res = new ArrayList<String>();
        String[] tokens = tagstring.toLowerCase().split(" ");
        tokens = Common.removeEmptyEntries(tokens);
        for (int i = 0; i <= (tokens.length - 1); i++) {
            String token;
            if (stripSpecialChars) {
                token = tokens[i].trim().replaceAll("[^a-zA-Z0-9\\-]", "");
            } else {
                token = tokens[i].trim();
            }
            if (token.length() > 0) {
                res.add(token);
            }
        }

        String[] retVal = new String[res.size()];
        retVal = res.toArray(retVal);

        return (retVal);
    }
}
