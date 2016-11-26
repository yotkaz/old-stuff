package yotkaz.arduino.gui.sm.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import yotkaz.arduino.sm.domain.Message;

/**
 * @author yotkaz
 *
 */
@Repository
public class SerialMonitorMemoryRepository implements SerialMonitorRepository {

	private List<Message> repositoryList = new ArrayList<Message>();
	
	@Override
	public List<Message> getAllMessages() {
		return new ArrayList<Message>(repositoryList);
	}

	@Override
	public List<Message> getMessages(int offset, int count) {
		return new ArrayList<Message>(repositoryList.subList(offset, offset + count));
	}

	@Override
	public void add(Message message) {
		repositoryList.add(message);
	}

	@Override
	public void clear() {
		repositoryList.clear();
	}

}