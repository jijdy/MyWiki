package com.example.mywiki.service;

import com.example.mywiki.webSocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WebSocketService {

    //异步化的使用，调用该方法时会重新开启一个线程，使得点赞和消息推送异步执行
    @Async
    public void sendInfo(String message) {
        webSocketServer.sendInfo(message);
    }

    @Resource
    public WebSocketServer webSocketServer;
}
