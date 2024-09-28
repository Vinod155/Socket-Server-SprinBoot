package com.example.UberSocketServer.controller;

import com.example.UberSocketServer.dto.ChatRequest;
import com.example.UberSocketServer.dto.ChatResponse;
import com.example.UberSocketServer.dto.TestRequest;
import com.example.UberSocketServer.dto.TestResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public TestController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public TestResponse pingCheck(TestRequest message)
    {
        System.out.println("received message from client:"+message.getData());

        return new TestResponse("Recieved");
    }

//    @Scheduled(fixedDelay = 2000)
//    public void sendPeriodMessage() //sending periodic message to destination
//    {
//        simpMessagingTemplate.convertAndSend("/topic/scheduled","Peridodic message sent "+System.currentTimeMillis());
//      }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/message/{roomId}")
    public ChatResponse chatMessage(@DestinationVariable String roomId, ChatRequest request)
    {
          ChatResponse response=ChatResponse.builder()
                  .name(request.getName())
                  .message(request.getMessage())
                  .timeStamp(""+System.currentTimeMillis())
                  .build();
        System.out.println("received chat from client:"+request.getName());

        return response;
    }

    @MessageMapping("/privateChat/{roomId}/{userId}")
   // @SendTo("/topic/privateMessage/{roomId}/{userId}")
    public void chatMessage(@DestinationVariable String roomId,@DestinationVariable String userId, ChatRequest request)
    {
        ChatResponse response=ChatResponse.builder()
                .name(request.getName())
                .message(request.getMessage())
                .timeStamp(""+System.currentTimeMillis())
                .build();
        System.out.println("received chat from client:"+request.getName());

        simpMessagingTemplate.convertAndSendToUser(userId,"/queue/privateMessage/"+roomId,response);

    }

}
