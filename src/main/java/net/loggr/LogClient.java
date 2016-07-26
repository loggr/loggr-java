package net.loggr;

import net.loggr.utility.Common;
import net.loggr.utility.Configuration;
import net.loggr.utility.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class LogClient {

    private static final Logger logger = LogManager.getLogger(LogWebClient.class);

    private String _apiKey = "";
    private String _logKey = "";
    private String _version = "";
    private String _server = "";

    public LogClient() {
        _logKey = Configuration.getLogKey();
        _apiKey = Configuration.getApiKey();
        _server = Configuration.getServer();
        _version = Configuration.getVersion();
    }

    public LogClient(String LogKey, String ApiKey) {
        _logKey = LogKey;
        _apiKey = ApiKey;
        _server = Configuration.getServer();
        _version = Configuration.getVersion();
    }

    public String getApiKey() {
        return (_apiKey);
    }

    public void setApiKey(String value) {
        _apiKey = value;
    }

    public String getLogKey() {
        return (_logKey);
    }

    public void setLogKey(String value) {
        _logKey = value;
    }

    public String getVersion() {
        return (_version);
    }

    public void setVersion(String value) {
        _version = value;
    }

    public String getServer() {
        return (_server);
    }

    public void setServer(String value) {
        _server = value;
    }

    public void post(final Event event, boolean async) {
        // modify event based on configuration
        mergeConfigurationWithEvent(event);

        // post async or sync
        if (!async) {
            postBase(event);
        } else {
            new Thread(new Runnable() {
                public void run() {
                    postBase(event);
                }
            }).start();
        }
    }

    protected void postBase(Event eventObj) {
        if (this._apiKey != null && this._apiKey != "" && this._logKey != null && this._logKey != "") {
            String url = String.format("http://%s/%s/logs/%s/events", this._server, this._version, this._logKey);
            String postStr = String.format("%s&apikey=%s", createQueryString(eventObj), this._apiKey);
            LogWebClient cli = new LogWebClient();
            cli.addHeader("Content-Type", "application/x-www-form-urlencoded");
            try {
                cli.uploadData(new URL(url), postStr);
            } catch (MalformedURLException e) {
                logger.error(e);
            }
        }
    }


    protected void mergeConfigurationWithEvent(Event event) {
        // merge in default tags from config file
        if (!Common.isNullOrEmpty(Configuration.getTags())) {
            event.getTags().addAll(Arrays.asList(Tags.tokenizeAndFormat(Configuration.getTags())));
        }

        // overwrite default source from config file
        if (!Common.isNullOrEmpty(Configuration.getSource())) {
            event.setSource(Configuration.getSource());
        }
    }

    protected String createQueryString(Event event) {
        String qs = "";
        qs = (String) appendQueryStringNameValue("text", event.getText(), qs);
        qs = (String) appendQueryStringNameValue("link", event.getLink(), qs);
        qs = (String) appendQueryStringNameValueList("tags", event.getTags(), qs);
        qs = (String) appendQueryStringNameValue("source", event.getSource(), qs);
        qs = (String) appendQueryStringNameValue("user", event.getUser(), qs);
        if (event.getDataType() == DataType.html) {
            qs = (String) appendQueryStringNameValue("data", "@html" + System.getProperty("line.separator") + event.getData(), qs);
        } else if (event.getDataType() == DataType.json) {
            qs = (String) appendQueryStringNameValue("data", "@json" + System.getProperty("line.separator") + event.getData(), qs);
        } else {
            qs = (String) appendQueryStringNameValue("data", event.getData(), qs);
        }
        if (event.getValue() != null) {
            qs = (String) appendQueryStringNameValueObject("value", event.getValue(), qs);
        }
        if (event.getGeo() != null) {
            qs = (String) appendQueryStringNameValueObject("geo", event.getGeo(), qs);
        }
        return qs;
    }

    protected Object appendQueryStringNameValue(String name, String value, String queryString) {
        if (Common.isNullOrEmpty(value))
            return queryString;
        if (queryString.length() > 0)
            queryString += "&";
        try {
            queryString += String.format("%s=%s", name, java.net.URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        return queryString;
    }

    protected Object appendQueryStringNameValueObject(String name, Object value, String queryString) {
        if (queryString.length() > 0)
            queryString += "&";
        try {
            queryString += String.format("%s=%s", name, java.net.URLEncoder.encode(value.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        return queryString;
    }

    protected Object appendQueryStringNameValueList(String name, ArrayList<String> value, String queryString) {
        if (value.size() == 0)
            return queryString;
        if (queryString.length() > 0)
            queryString += "&";
        String[] values = new String[value.size()];
        values = value.toArray(values);
        try {
            queryString += String.format("%s=%s", name, java.net.URLEncoder.encode(Common.join(" ", values), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        return queryString;
    }
}
