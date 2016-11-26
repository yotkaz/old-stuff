package yotkaz.mas.mp.emanalea.app.conf.impl;

import com.google.common.collect.ListMultimap;
import yotkaz.mas.mp.emanalea.app.conf.Configuration;
import yotkaz.mas.mp.emanalea.io.RWCombo;
import yotkaz.mas.mp.emanalea.io.impl.SerializedMultiExtentFileRW;
import yotkaz.mas.mp.emanalea.service.log.Loggable;
import yotkaz.mas.mp.emanalea.service.log.impl.SysOut;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Created on 04.04.16.
 */
public class BaseHardcodedConfiguration implements Configuration {

    private static final String MULTI_EXTENSION_FILE = "data-source.mas";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SSS");

    private Loggable logger;

    public BaseHardcodedConfiguration() throws Exception {
        SysOut sysOut = new SysOut();
        sysOut.setDateTimeFormatter(getDateTimeFormatter());
        logger = sysOut;
    }

    @Override
    public DateTimeFormatter getDateTimeFormatter() throws Exception {
        return DATE_TIME_FORMATTER;
    }

    public Loggable getLogger() throws Exception {
        return logger;
    }

    public RWCombo<ListMultimap<Class, Object>> getMultiExtentRW() throws IOException {
        return new SerializedMultiExtentFileRW(MULTI_EXTENSION_FILE);
    }

}
