package yotkaz.mas.mp.emanalea.base;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import yotkaz.mas.mp.emanalea.io.Reader;
import yotkaz.mas.mp.emanalea.io.Writer;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 25.03.16.
 */
public class Extent implements Serializable {

    private static ListMultimap<Class, Object> extent = ArrayListMultimap.create();

    public Extent() {
        extent.put(this.getClass(), this);
    }

    public static <T> List<T> getExtent(Class<T> clazz) {
        return (List<T>) extent.get(clazz);
    }

    public static <T> List<T> getExtent(String clazzName) throws ClassNotFoundException {
        return (List<T>) extent.get(Class.forName(clazzName));
    }

    public static void saveExtents(Writer<ListMultimap<Class, Object>> multiExtentWriter) throws Exception {
        multiExtentWriter.write(extent);
    }

    public static void loadExtents(Reader<ListMultimap<Class, Object>> multiExtentReader) throws Exception {
        extent = multiExtentReader.read();
    }

    public static <T> void saveExtent(Class<T> clazz, Writer<List<T>> extentWriter) throws Exception {
        extentWriter.write(getExtent(clazz));
    }

    public static <T> List<T> readExtent(Reader<List<T>> extentReader) throws Exception {
        return extentReader.read();
    }

    public static void clear() {
        extent.clear();
    }
}
