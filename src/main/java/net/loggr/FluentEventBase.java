package net.loggr;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class FluentEventBase<T> implements IFluentEvent<T> {
    protected Event _event = new Event();
    protected LogClient _client;

    protected FluentEventBase() {
    }

    protected FluentEventBase<T> create(Class<T> _class) throws InstantiationException, IllegalAccessException {
        return this;
    }


    public Event getEvent() {
        return _event;
    }

    public T post() {
        this.post(true);
        return getReturnClass();
    }

    public T post(boolean async) {
        if (_client == null) {
            _client = new LogClient();
        }
        _client.post(this._event, async);
        return getReturnClass();
    }

    public T clear() {
        _event = new Event();
        return getReturnClass();
    }

    public T text(String text) {
        this._event.setText(assignWithMacro(text, this._event.getText()));
        return getReturnClass();
    }

    public T text(String formatString, Object... formatArguments) {
        return text(String.format(formatString, formatArguments));
    }

    public T addText(String Text) {
        this._event.setText(this._event.getText() + assignWithMacro(Text, this._event.getText()));
        return getReturnClass();
    }

    public T addText(String formatString, Object... formatArguments) {
        return addText(String.format(formatString, formatArguments));
    }

    public T link(String link) {
        this._event.setLink(assignWithMacro(link, this._event.getLink()));
        return getReturnClass();
    }

    public T link(String formatString, Object... formatArguments) {
        return link(String.format(formatString, formatArguments));
    }

    public T source(String source) {
        this._event.setSource(assignWithMacro(source, this._event.getSource()));
        return getReturnClass();
    }

    public T source(String formatString, Object... formatArguments) {
        return source(String.format(formatString, formatArguments));
    }

    public T user(String user) {
        this._event.setUser(assignWithMacro(user, this._event.getUser()));
        return getReturnClass();
    }

    public T user(String formatString, Object... formatArguments) {
        return user(String.format(formatString, formatArguments));
    }

    public T tags(String tags) {
        this._event.setTags(new ArrayList<String>());
        return this.addTags(tags);
    }

    public T tags(String[] tags) {
        this._event.setTags(new ArrayList<String>());
        return this.addTags(tags);
    }

    public T addTags(String tags) {
        this._event.getTags().addAll(Arrays.asList(net.loggr.utility.Tags.tokenizeAndFormat(tags)));
        return getReturnClass();
    }

    public T addTags(String[] tags) {
        this._event.getTags().addAll(Arrays.asList(net.loggr.utility.Tags.tokenizeAndFormat(tags)));
        return getReturnClass();
    }

    public T value(double value) {
        this._event.setValue(value);
        return getReturnClass();
    }

    public T valueClear() {
        this._event.setValue(new Double(0));
        return getReturnClass();
    }

    public T data(String data) {
        this._event.setData(assignWithMacro(data, this._event.getData()));
        return getReturnClass();
    }

    public T data(String formatString, Object... formatArguments) {
        return data(String.format(formatString, formatArguments));
    }

    public T addData(String data) {
        this._event.setData(this._event.getData() + assignWithMacro(data, this._event.getData()));
        return getReturnClass();
    }

    public T addData(String formatString, Object... formatArguments) {
        return addData(String.format(formatString, formatArguments));
    }

    public T dataType(DataType type) {
        this._event.setDataType(type);
        return getReturnClass();
    }

    public T geo(double lat, double lon) {
        this._event.setGeo(String.format("%s,%s", lat, lon));
        return getReturnClass();
    }

    public T geo(String lat, String lon) {
        double latDouble = 0, lonDouble = 0;
        try {
            latDouble = Double.parseDouble(lat);
        } catch (NumberFormatException ex) {
            // no worries
        }

        try {
            lonDouble = Double.parseDouble(lon);
        } catch (NumberFormatException ex) {
            // no worries
        }

        return this.geo(latDouble, lonDouble);
    }

    public T geoIP(String IPAddress) {
        this._event.setGeo(String.format("ip:%s", IPAddress));
        return getReturnClass();
    }

    public T geoClear() {
        this._event.setGeo(null);
        return getReturnClass();
    }

    public T useLog(String logKey, String apiKey) {
        _client = new LogClient(logKey, apiKey);
        return getReturnClass();
    }

    @SuppressWarnings("unchecked")
    protected T getReturnClass() {
        return (T) (this);
    }

    protected String assignWithMacro(String input, String base) {
        return input.replace("$$", base);
    }


}
