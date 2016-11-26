package yotkaz.arduino.sm.service;

import yotkaz.arduino.sm.domain.Message;

/**
 * @author yotkaz
 *
 */
@FunctionalInterface
public interface MessageHandler {
	
	void handle(Message message);
}
