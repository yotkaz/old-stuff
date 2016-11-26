package yotkaz.arduino.sm.service;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import yotkaz.arduino.sm.domain.Message;
import yotkaz.arduino.sm.domain.Message.MessageType;

/**
 * @author yotkaz
 *
 */
public class SerialWriter implements Runnable {

	Queue<Message> queue = new LinkedList<Message>();

	private final OutputStream output;
	private final MessageHandler handler;

	public SerialWriter(OutputStream output, MessageHandler handler) {
		this.output = output;
		this.handler = handler;
	}

	public void addMessageToSend(String messageText) {
		Message message = new Message();
		message.setText(messageText);
		message.setType(MessageType.SEND);
		queue.add(message);
	}
	
	public void run() {
		while (true) {
			try {
				if (!queue.isEmpty()) {
					Message message = queue.poll();
					output.write(message.getText().getBytes());
					handler.handle(message);
				}
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
