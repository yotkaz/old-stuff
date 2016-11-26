package yotkaz.mas.mp.emanalea.service.log.impl;

import yotkaz.mas.mp.emanalea.service.log.Loggable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static yotkaz.mas.mp.emanalea.service.log.Loggable.MessageType.*;

/**
 * Created on 04.04.16.
 */
public class SysOut implements Loggable {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:SSS");

    @Override
    public void original(String message) {
        printMessage(ORIGINAL, message);
    }

    @Override
    public void error(String message) {
        printMessage(ERROR, message);
    }

    @Override
    public void info(String message) {
        printMessage(INFO, message);
    }

    @Override
    public void debug(String message) {
        printMessage(DEBUG, message);
    }

    private String messageLabel(MessageType type) {
        return type.toString()
                + " ["
                + LocalDateTime.now().format(dateTimeFormatter)
                + "]: ";
    }

    private void printMessage(MessageType type, String message) {
        System.out.println((type == ORIGINAL ? "" : messageLabel(type)) + message);
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

}
