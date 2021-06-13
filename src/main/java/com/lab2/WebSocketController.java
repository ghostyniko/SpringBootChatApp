package com.lab2;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebSocketController {
	
	@Autowired
    private SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
	
	@MessageMapping("/chat.sendMessagePrivate")
 
    public void sendMessagePrivate(@Payload ChatMessage chatMessage) {
        String rec = chatMessage.getReceiver();
        String sen = chatMessage.getSender();
        messagingTemplate.convertAndSend("/topic/private/"+rec, chatMessage);
        messagingTemplate.convertAndSend("/topic/private/"+sen, chatMessage);
    }
	
    @MessageMapping("/chat.addUser")
    //@SendTo("/topic/publicChatRoom")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    	System.out.println("Dodano");
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    	
    }
    
    
    
    
}
