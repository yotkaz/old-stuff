package yotkaz.arduino.gui.sm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yotkaz.arduino.gui.sm.repository.SerialMonitorRepository;
import yotkaz.arduino.sm.domain.Message;
import yotkaz.arduino.sm.service.MessageHandler;
import yotkaz.arduino.sm.service.SerialMonitor;

/**
 * @author yotkaz
 *
 */
@Service
public class SerialMonitorService {

	@Autowired
	SerialMonitorRepository repository;

	private final SerialMonitor sm;

	@Autowired
	public SerialMonitorService(SerialMonitorServiceConfiguration conf) throws Exception {
		MessageHandler handler = (message) -> repository.add(message);
		sm = new SerialMonitor(conf.getPort(), conf.getDataRate(), conf.getTimeout(), conf.getName(), handler);
		sm.init();
	}

	public void send(String messageText) {
		sm.getWriter().addMessageToSend(messageText);
	}

	public List<Message> getAllMessages() {
		return repository.getAllMessages();
	}

}
