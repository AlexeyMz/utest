package ru.eltech.utest.diagram.sequence;

import java.util.ArrayList;
import java.util.List;

public class Links {
	private List<Message> links = new ArrayList<Message>();
	
	public void setLink(Message message) {
		links.add(message);
	}
	
	public Message getLink(int i) {
		return links.get(i);
	}
}
