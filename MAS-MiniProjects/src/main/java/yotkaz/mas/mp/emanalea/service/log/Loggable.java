package yotkaz.mas.mp.emanalea.service.log;

/**
 * Created on 04.04.16.
 */
public interface Loggable {

    void original(String message);

    void error(String message);

    void info(String message);

    void debug(String message);

    enum MessageType {ORIGINAL, ERROR, INFO, DEBUG}

}
