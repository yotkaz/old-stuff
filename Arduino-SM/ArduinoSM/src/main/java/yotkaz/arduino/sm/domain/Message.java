package yotkaz.arduino.sm.domain;

import java.io.Serializable;

/**
 * @author yotkaz
 *
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum MessageType {
		RECEIVED, SEND
	}

	private String text;
	private MessageType type;

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}
}