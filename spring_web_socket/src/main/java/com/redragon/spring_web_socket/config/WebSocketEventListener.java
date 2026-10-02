package com.redragon.spring_web_socket.config;

import com.redragon.spring_web_socket.chat.ChatMessage;
import com.redragon.spring_web_socket.chat.MessageTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageTemplate;
    @EventListener
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getSessionAttributes().get("username").toString();
        if(username!=null){
            log.info("user Disconnect: {}", username);
            var chatMessage = ChatMessage.builder()
                    .type(MessageTypes.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
