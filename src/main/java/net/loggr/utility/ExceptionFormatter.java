package net.loggr.utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ExceptionFormatter {

    private ExceptionFormatter() {
    }

    private static String formatType(String type) {
        if (StringUtils.isBlank(type)) {
            return "";
        }

        int lastDotIndex = Common.getLastIndexOf(type, '.');

        if (lastDotIndex > 0) {
            type = type.substring(lastDotIndex + 1);
        }

        final String conventionalSuffix = "Exception";

        if (type.length() > conventionalSuffix.length()) {
            int suffixIndex = type.length() - conventionalSuffix.length();

            if (type.substring(suffixIndex).equalsIgnoreCase(conventionalSuffix)) {
                type = type.substring(0, suffixIndex);
            }
        }

        return type;
    }

    public static String formatType(Exception ex) {
        if (ex == null) {
            throw new IllegalArgumentException("error");
        }

        return formatType(ex.getClass().getCanonicalName());
    }

    public static String format(Exception ex, Object traceObject) {
        String res = "";

        // send basic info
        res += String.format("<b>Exception</b>: %s<br />", ex.getMessage());
        res += String.format("<b>Type</b>: %s<br />", ex.getClass().getCanonicalName());
        res += String.format("<b>Machine</b>: %s<br />", System.getProperty("user.name"));

        res += "<br />";

        // see if we have web details
        ///TODO: We need to get last error from Apache or something, asking Dave in the AM
//        if (HttpContext.Current != null)
//        {
//            res += string.format("<b>Request URL</b>: %s<br />", HttpContext.Current.Request.Url.ToString());
//            res += string.format("<b>Is Authenticated</b>: %s<br />", (HttpContext.Current.User.Identity.IsAuthenticated ? "True" : "False"));
//            res += string.format("<b>User</b>: %s<br />", (HttpContext.Current.User.Identity.IsAuthenticated ? HttpContext.Current.User.Identity.Name : "anonymous"));
//            res += string.format("<b>User host address</b>: %s<br />", HttpContext.Current.Request.ServerVariables["REMOTE_ADDR"]);
//            res += string.format("<b>Request Method</b>: %s<br />", HttpContext.Current.Request.ServerVariables["REQUEST_METHOD"]);
//            res += string.format("<b>User Agent</b>: %s<br />", HttpContext.Current.Request.ServerVariables["HTTP_USER_AGENT"]);
//            res += string.format("<b>Referer</b>: %s<br />", HttpContext.Current.Request.ServerVariables["HTTP_REFERER"]);
//            res += string.format("<b>Script Name</b>: %s<br />", HttpContext.Current.Request.ServerVariables["SCRIPT_NAME"]);
//        }

        if (traceObject != null) {
            res += "<br />";
            res += "<b>Traced Object(s)</b><br />";
            res += "<br />";
            res += ObjectDumper.dumpObject(traceObject, 1);
        }

        res += "<br />";
        res += "<b>Stack Trace</b><br />";
        res += "<br />";

        res += formatStack(ex, res);

        return res;
    }

    private static String formatStack(Exception ex, String buffer) {
        String retVal = buffer;

        if (ex.getCause() != null) {
            formatStack((Exception) ex.getCause(), retVal);
        }
        retVal += String.format("[%s: %s]<br />", ex.getClass().getCanonicalName(), ex.getMessage());
        if (ex.getStackTrace() != null) {
            retVal += getStackTrace(ex);
        } else {
            retVal += "No stack trace";
        }
        retVal += "<br/>";
        retVal += "<br/>";
        return (retVal);
    }

    private static String getStackTrace(Throwable t) {
        return ExceptionUtils.getStackTrace(t);
    }
}
