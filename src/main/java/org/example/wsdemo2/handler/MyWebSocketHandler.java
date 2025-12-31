package org.example.wsdemo2.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.wsdemo2.pojo.SessionBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/*webSocket主程序*/
@Slf4j
@Component
public class MyWebSocketHandler extends AbstractWebSocketHandler {
    private static Map<String, SessionBean> sessionBeanMap;
    private static AtomicInteger clientIdMaker;

    static {
        sessionBeanMap = new ConcurrentHashMap<>();
        clientIdMaker = new AtomicInteger(0);
    }

    /*连接建立后*/
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("连接建立成功");
        super.afterConnectionEstablished(session);
        SessionBean sessionBean = new SessionBean(session, clientIdMaker.getAndIncrement());
        sessionBeanMap.put(session.getId(), sessionBean);
    }

    /*收到消息*/
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.info("收到消息");
        super.handleBinaryMessage(session, message);
        log.info(sessionBeanMap.get(session.getId()).getClientId() + ":" + message.getPayload());
    }
    /*出现异常*/

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("出现异常");
        super.handleTransportError(session, exception);
        if (session.isOpen()) {
            session.close();
        }
        sessionBeanMap.remove(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(sessionBeanMap.get(session.getId()).getClientId() + "连接关闭");
        super.afterConnectionClosed(session, status);
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMsg() throws IOException {
        for (SessionBean sessionBean : sessionBeanMap.values()) {
            sessionBean.getSession().sendMessage(new TextMessage("hello"));
        }
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String clientId = sessionBeanMap.get(session.getId()).getClientId().toString();

        log.info("客户端[{}]发送消息: {}", clientId, payload);

        // 发送响应
        session.sendMessage(new TextMessage("Hello from server: " + payload));
    }

}