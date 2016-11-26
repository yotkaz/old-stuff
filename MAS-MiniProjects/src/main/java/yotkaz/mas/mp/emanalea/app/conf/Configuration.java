package yotkaz.mas.mp.emanalea.app.conf;

import com.google.common.collect.ListMultimap;
import yotkaz.mas.mp.emanalea.io.RWCombo;
import yotkaz.mas.mp.emanalea.service.log.Loggable;

import java.time.format.DateTimeFormatter;


/**
 * Created on 11.04.16.
 */
public interface Configuration {

    public DateTimeFormatter getDateTimeFormatter() throws Exception;

    public Loggable getLogger() throws Exception;

    public RWCombo<ListMultimap<Class, Object>> getMultiExtentRW() throws Exception;

}
