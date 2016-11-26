package yotkaz.mas.mp.emanalea.io;

/**
 * Created on 11.04.16.
 */
@FunctionalInterface
public interface Reader<T> {

    T read() throws Exception;

}
