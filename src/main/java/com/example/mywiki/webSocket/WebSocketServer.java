package com.example.mywiki.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    private String token;
    private static final HashMap<String, Session> map = new HashMap<>();

    /*
    *服务端和客服端建立连接后调用的方法，将每个连接的生成的token
    * 和session对应的存入到websocket中的hashMap集合中
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        map.put(token, session);
        this.token = token;
        LOG.info("建立连接：token：{}，session id: {}, 当前连接数： {}", token, session.getId(), map.size());

    }

    /*
    * 关不连接时的方法，移除保存的信息
    * */
    @OnClose
    public void onClose(Session session) {
        map.remove(this.token);
        LOG.info("关闭连接: token：{}，session id: {}, 当前连接数： {}", this.token, session.getId(), map.size());
    }

    /*
    * 收到消息时调用的方法
    * */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("收到消息：token：{}，session id: {}, 当前连接数： {}", token,session.getId(), map.size());

    }

    /*
    * 连接错误时的调用
    * */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("发生错误", error);
    }

    /*
    * 群发消息，循环遍历map集合中的连接并检查是否有更新
    * */
    public void sendInfo(String message) {
        for (String token : map.keySet()) {
            Session session = map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("推送消息失败：{}，内容：{}", token, message);
            }
            LOG.info("推送消息：{},内容：{}", token, message);
        }
    }

}
