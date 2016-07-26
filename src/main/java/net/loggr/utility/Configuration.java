package net.loggr.utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Configuration {

    private static final Logger logger = LogManager.getLogger(Configuration.class);

    private static final String API_KEY_PROPERTY_NAME = "apiKey";
    private static final String LOG_KEY_PROPERTY_NAME = "logKey";
    private static final String SERVER_PROPERTY_NAME = "server";
    private static final String DEFAULT_SERVER_PROPERTY = "post.loggr.net";
    private static final String VERSION_PROPERTY_NAME = "version";
    private static final String DEFAULT_VERSION_PROPERTY = "1";
    private static final String TAGS_PROPERTY_NAME = "tags";
    private static final String SOURCE_PROPERTY_NAME = "source";

    private static final Properties loadedProperties = new Properties();

    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceStream = loader.getResourceAsStream("app.properties");
            loadedProperties.load(resourceStream);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    private Configuration() {
    }

    private static String _apiKey = "";
    private static String _logKey = "";
    private static String _server = "";
    private static String _version = "";
    private static String _tags = "";
    private static String _source = "";

    public static String getApiKey() {
        if (Common.isNullOrEmpty(_apiKey)) {
            _apiKey = getFromEnvOrSystemProperties(API_KEY_PROPERTY_NAME);
            checkIfPresent(API_KEY_PROPERTY_NAME, _apiKey);
        }
        return _apiKey;
    }

    public static String getLogKey() {
        if (Common.isNullOrEmpty(_logKey)) {
            _logKey = getFromEnvOrSystemProperties(LOG_KEY_PROPERTY_NAME);
            checkIfPresent(LOG_KEY_PROPERTY_NAME, _logKey);
        }
        return _logKey;
    }

    public static String getServer() {
        if (Common.isNullOrEmpty(_server)) {
            _server = getFromEnvOrSystemProperties(SERVER_PROPERTY_NAME);
            if (StringUtils.isBlank(_server)) {
                _server = DEFAULT_SERVER_PROPERTY;
            }
        }
        return _server;
    }

    public static String getVersion() {
        if (Common.isNullOrEmpty(_version)) {
            _version = getFromEnvOrSystemProperties(VERSION_PROPERTY_NAME);
            if (StringUtils.isBlank(_version)) {
                _version = DEFAULT_VERSION_PROPERTY;
            }
        }
        return _version;
    }

    public static String getTags() {
        if (Common.isNullOrEmpty(_tags)) {
            _tags = "";

            _tags = getFromEnvOrSystemProperties(TAGS_PROPERTY_NAME);
        }
        return (_tags);
    }

    public static String getSource() {
        if (Common.isNullOrEmpty(_source)) {
            _source = "";

            _source = getFromEnvOrSystemProperties(SOURCE_PROPERTY_NAME);
        }
        return (_source);
    }

    private static String getFromEnvOrSystemProperties(String propName) {
        String propValue = System.getenv(propName);
        if (StringUtils.isBlank(propValue)) {
            propValue = System.getProperty(propName);
        }

        if (StringUtils.isBlank(propValue) && loadedProperties.containsKey(propName)) {
            propValue = loadedProperties.getProperty(propName);
        }

        return propValue;
    }

    private static void checkIfPresent(String propertyName, String propertyValue) {
        if (isBlank(propertyValue)) {
            throw new IllegalArgumentException("Please specify correct \'" + propertyName + "\' property.");
        }
    }

}
