package com.lab2;

public class ChatMessage {
	// private MessageType type;
	    private String content;
	    private String sender;
	    private String receiver;
	 
	    public String getContent() {
	        return content;
	    }
	 
	    public void setContent(String content) {
	        this.content = content;
	    }
	 
	    public String getSender() {
	        return sender;
	    }
	 
	    public void setSender(String sender) {
	        this.sender = sender;
	    }

		public String getReceiver() {
			return receiver;
		}

		public void setReceiver(String receiver) {
			this.receiver = receiver;
		}
	    
}