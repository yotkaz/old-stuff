package yotkaz.mas.mp.emanalea.io;

/**
 * Created on 11.04.16.
 */
@FunctionalInterface
public interface Writer<T> {

    void write(T object) throws Exception;

}
