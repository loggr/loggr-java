package net.loggr;

import net.loggr.utility.ExceptionFormatter;
import net.loggr.utility.ObjectDumper;

public class Events {
    private Events() {

    }

    public static FluentEvent create() {
        return new FluentEvent();
    }


    public static FluentEvent createFromException(Exception ex) {
        return createFromException(ex, null);
    }

    private static FluentEvent createFromException(Exception ex, Object traceObject) {
        return create()
                .text(ex.getMessage())
                .tags("error " + ExceptionFormatter.formatType(ex))
                .source(ex.getClass() == null ? "" : ex.getClass().toString())
                .data(ExceptionFormatter.format(ex, traceObject))
                .dataType(DataType.html);
    }

    public static FluentEvent createFromVariable(Object traceObject) {
        return createFromVariable(traceObject, 1);
    }

    private static FluentEvent createFromVariable(Object traceObject, int depth) {
        return create()
                .data(ObjectDumper.dumpObject(traceObject, depth))
                .dataType(DataType.html);
    }
}
