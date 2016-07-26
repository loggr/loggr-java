package net.loggr.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class ObjectDumper {

    private static final Logger logger = LogManager.getLogger(ObjectDumper.class);

    public static void write(Object element) {
        write(element, 0);
    }

    private static void write(Object element, int depth) {

        StringWriter log = new StringWriter();

        write(element, depth, log);
        logger.info(log.toString());
    }

    public static String dumpObject(Object element, int depth) {
        StringWriter log = new StringWriter();
        write(element, depth, log);
        return log.toString();
    }

    private static void write(Object element, int depth, StringWriter out) {
        ObjectDumper dumper = new ObjectDumper(depth);
        dumper.writer = out;
        dumper.writeObject("[Root]", element);
    }

    private StringWriter writer;
    private int level;

    private int depth;

    private ObjectDumper(int depth) {
        this.depth = depth;
    }

    private void write(String s) {
        if (s != null) {
            writer.write(s);
        }
    }

    private void writeLine() {
        writer.write("<br/>");
    }

    private void writeTab() {
        write("&nbsp;");
    }

    private void writeObject(String prefix, Object element) {
        if (element == null || element instanceof String) {
            write(prefix);
            writeValue(element);
            writeLine();
        } else {
            @SuppressWarnings("unchecked")
            Iterable<Object> enumerableElement = this.isIterableClass(element.getClass()) && element instanceof Iterable<?> ? (Iterable<Object>) element : null;
            if (enumerableElement != null) {
                for (Object item : enumerableElement) {
                    if (isIterableClass(item.getClass()) && !(item instanceof String)) {
                        write(prefix);
                        write("...");
                        writeLine();
                        if (level < depth) {
                            level += 1;
                            writeObject(prefix, item);
                            level -= 1;
                        }
                    } else {
                        writeObject(prefix, item);
                    }
                }
            } else {
                Class<?> myClass = element.getClass();
                Member[] members = myClass.getDeclaredFields();
                write(prefix);
                writeLine();
                writeTab();
                writeTab();
                boolean propWritten = false;
                for (Member member : members) {
                    Field f = (Field) member;
                    if (f != null) {
                        f.setAccessible(true);
                        if (propWritten) {
                            writeTab();
                        } else {
                            propWritten = true;
                        }
                        write(f.getName());
                        write("=");
                        Type t = f.getType();
                        String tester = "";
                        if (t == tester.getClass()) {
                            try {
                                writeValue(f.get(element));
                            } catch (Exception ex) {
                                writeValue("#ERR");
                            }
                        } else {
                            if (Iterable.class.isAssignableFrom(t.getClass())) {
                                write("...");
                            } else {
                                write("{ }");
                            }
                        }
                        writeLine();
                        writeTab();
                    }
                }
                if (level < depth) {
                    for (Member member : members) {
                        Field f = (Field) member;
                        if (f != null) {
                            Type t = f.getGenericType();
                            if (!(String.class.equals(t))) {
                                Object value;
                                try {
                                    value = f.get(element);
                                } catch (IllegalArgumentException e) {
                                    continue;
                                } catch (IllegalAccessException e) {
                                    continue;
                                }
                                if (value != null) {
                                    level += 1;
                                    writeObject("[" + f.getName() + "]", value);
                                    level -= 1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void writeValue(Object o) {
        if (o == null) {
            write("null");
        } else if (Date.class.equals(o)) {
            write(o.toString());
        } else if (String.class.equals(o.getClass())) {
            String res = (String) o;
            if (res.length() > 150) {
                res = res.substring(0, 150) + "...";
            }
            write(res);
        } else if (isIterableClass(o.getClass())) {
            write("...");
        } else {
            write("{ }");
        }
    }


    // Helpers
    /**
     * Checks whether the specified class parameter is an instance of a collection class.
     *
     * @param clazz <code>Class</code> to check.
     *
     * @return <code>true</code> is <code>clazz</code> is instance of a collection class, <code>false</code> otherwise.
     */
    private boolean isIterableClass(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        computeClassHierarchy(clazz, classes);
        return classes.contains(Iterable.class);
    }

    /**
     * Get all superclasses and interfaces recursively.
     *
     * @param clazz The class to start the search with.
     * @param classes List of classes to which to add all found super classes and interfaces.
     */
    private void computeClassHierarchy(Class<?> clazz, List<Class<?>> classes) {
        for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
            if (classes.contains(current)) {
                return;
        }
            classes.add(current);
            for (Class<?> currentInterface : current.getInterfaces()) {
                computeClassHierarchy(currentInterface, classes);
            }
        }
    }

}
