package com.anranyus.control.socket;

import com.anranyus.control.entity.Rest;
import com.anranyus.control.pool.DevicePool;
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
        String message = gson.toJson(new Rest( CONNECTED,"连接完成",true,Rest.DEVICE_TYPE_SERVER));
        session.sendMessage(new TextMessage(message));
    }

    @Override
    protected void handleTextMessage(@Nonnull WebSocketSession session,@Nonnull TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        Rest rest;
        try {
            rest = gson.fromJson(message.getPayload(),Rest.class);
            if (rest.getCommand()!= CONNECTED){//连接完成
                Rest command = new Rest(rest.command, rest.getMessage(), true,rest.getType());
                if (rest.getType() == Rest.DEVICE_TYPE_CONTROL){
                    //信息来自控制器，应该转发给客户端
                    DevicePool.CLIENT.sendMessage(new TextMessage(gson.toJson(command)));

                }else if (rest.getType() == Rest.DEVICE_TYPE_CLIENT){

                    //信息来自客户端，返回执行结果
                    DevicePool.CONTROL.sendMessage(new TextMessage(gson.toJson(command)));
                }

            }else {
                //进行连接初始化
                if (rest.getType()==Rest.DEVICE_TYPE_CLIENT){
                    DevicePool.CLIENT = session;
                }else {
                    DevicePool.CONTROL = session;
                }
            }


        }catch (NullPointerException e){
            e.printStackTrace();
            session.sendMessage(new TextMessage(gson.toJson(Rest.fail(400,"设备未连接:"+e.getMessage(),Rest.DEVICE_TYPE_SERVER))));

        } catch (Exception e){
            e.printStackTrace();
            session.sendMessage(new TextMessage(gson.toJson(Rest.fail(400,"错误:"+e.getMessage(),Rest.DEVICE_TYPE_SERVER))));
        }

    }
}
