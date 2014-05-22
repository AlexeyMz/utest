package ru.eltech.utest.diagram.sequence;

import ru.eltech.utest.diagram.type.Class;

/**
 * Сообщение.
 */
public class Message {
	private String message;
	private Class sender;
	private Class receiver;
	private String invariant;
	
	public Message(String message) {
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setSender(Class sender) {
		this.sender = sender;
	}

	public void setReceiver(Class receiver) {
		this.receiver = receiver;
	}

	public Class getSender() {
		return sender;
	}

	public Class getReceiver() {
		return receiver;
	}
}
