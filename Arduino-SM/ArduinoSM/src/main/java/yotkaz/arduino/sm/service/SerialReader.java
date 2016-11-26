package yotkaz.arduino.sm.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import yotkaz.arduino.sm.domain.Message;
import yotkaz.arduino.sm.domain.Message.MessageType;

/**
 * @author yotkaz
 *
 */
public class SerialReader extends Thread implements SerialPortEventListener {

	private final InputStream inputStream;
	private final MessageHandler handler;
	private final BufferedReader br;

	public SerialReader(InputStream inputStream, MessageHandler handler) {
		this.inputStream = inputStream;
		this.handler = handler;
		this.br = new BufferedReader(new InputStreamReader(this.inputStream));
	}

	public void serialEvent(SerialPortEvent event) {
		try {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				String messageText;
				while ((messageText = br.readLine()) != null) {
					Message message = new Message();
					message.setText(messageText);
					message.setType(MessageType.RECEIVED);
					handler.handle(message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
