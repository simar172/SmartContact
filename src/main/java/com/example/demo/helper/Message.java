package com.example.demo.helper;

public class Message {
	String content;

	public Message(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Message [content=" + content + ", type=" + type + "]";
	}

	public void setContent(String content) {
		this.content = content;
	}

	String type;
}
