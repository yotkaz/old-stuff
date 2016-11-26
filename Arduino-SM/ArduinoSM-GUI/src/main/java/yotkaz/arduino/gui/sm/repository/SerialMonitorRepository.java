package yotkaz.arduino.gui.sm.repository;

import java.util.List;

import yotkaz.arduino.sm.domain.Message;

/**
 * @author yotkaz
 *
 */
public interface SerialMonitorRepository {
	
	List<Message> getAllMessages();
	
	List<Message> getMessages(int offset, int count);
	
	void add(Message message);
	
	void clear();

}
