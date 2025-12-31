package org.example.wsdemo2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionBean {
    private WebSocketSession session;
    private Integer clientId;
}
