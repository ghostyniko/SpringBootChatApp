package com.lab2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.lab2.UserMessage.UserMessageType;

@Component
public class WebSocketEventListener {
	
	 @Autowired
	    private SimpMessageSendingOperations messagingTemplate;
	 
	    @EventListener
	    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
	       
	    }
	 
	    @EventListener
	    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
	        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
	 
	        String username = (String) headerAccessor.getSessionAttributes().get("username");
	         
	        if(username != null) {
	 
	           /* ChatMessage chatMessage = new ChatMessage();
	            chatMessage.setType(ChatMessage.MessageType.LEAVE);
	            chatMessage.setSender(username);
	 
	            messagingTemplate.convertAndSend("/topic/publicChatRoom", chatMessage);*/
	            
	            UserMessage userMessage= new UserMessage();
	            userMessage.setUsername(username);
	            userMessage.setType(UserMessageType.LEAVE);
	            
	            messagingTemplate.convertAndSend("/topic/usersObserver", userMessage);
	            

	            UserRepository.deleteUser(username);
	            UserRepository.printUsers();
	        }
	    }

}
