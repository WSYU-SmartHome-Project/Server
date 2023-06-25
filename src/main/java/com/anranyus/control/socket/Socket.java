package com.anranyus.control.socket;

import com.anranyus.control.entity.Rest;
import com.google.gson.Gson;
import jakarta.annotation.Nonnull;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@ResponseBody
public class Socket extends TextWebSocketHandler {
    public static final int CONNECTED = 200;
    private final Gson gson = new Gson();

    @Override
    public void afterConnectionEstablished(@Nonnull WebSocketSession session) throws Exception {
        String message = gson.toJson(new Rest( CONNECTED,"连接完成",true));
        session.sendMessage(new TextMessage(message));
    }g

    @Override
    protected void handleTextMessage(@Nonnull WebSocketSession session,@Nonnull TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        Rest rest;
        try {
            rest = gson.fromJson(message.getPayload(),Rest.class);
            if (rest.getCommand()!= CONNECTED){//连接完成
                Rest commend = new Rest(rest.command, "执行" + rest.getMessage(), true);
                session.sendMessage(new TextMessage(gson.toJson(commend)));
            }


        }catch (Exception e){
            e.printStackTrace();
            session.sendMessage(new TextMessage(gson.toJson(Rest.fail(400,"格式错误"))));
        }

    }
}
