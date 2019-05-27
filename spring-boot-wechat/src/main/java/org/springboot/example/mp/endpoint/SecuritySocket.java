package org.springboot.example.mp.endpoint;

import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import io.swagger.annotations.Api;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResponseMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Calvin on 2017/11/30.
 */
@Api(tags = "System Login Websocket")
@ServerEndpoint(
        value = "/wss/login/websocket",
        encoders = {MessageEncoder.class},
        decoders = {MessageDecoder.class}
)
@Component
public class SecuritySocket {

    private static int onlineCount = 0;

//    public static CopyOnWriteArraySet<SecuritySocket> webSocketSet = new CopyOnWriteArraySet<>();
    public static ConcurrentHashMap<String,Session> webSocketMap = new ConcurrentHashMap<>();

    public static final Integer EXPIRE_SECONDS=2592000;

    /**
     * 打开连接后获取扫码登录code
     * @param session
     * @return
     */
    @OnOpen
    public void onOpen (Session session) throws WxErrorException, IOException, EncodeException {
        webSocketMap.put(session.getId(),session);
        addOnlineCount();

        String loginCode = "qr_wsl_"+ session.getId();
        WxMpService wxMpService = ContextHolder.getContext().getBean(WxMpService.class);
        WxMpQrCodeTicket ticket=wxMpService.getQrcodeService().qrCodeCreateLastTicket(loginCode);
        String qrcode= wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket(),true);
        this.sendMessage(ResponseMessage.builder().code(SecurityApiCode.LOGIN_QR_CODE+"").message("qr code").payload(qrcode).build(),session);
    }

    @OnClose
    public void onClose (Session session){
        webSocketMap.remove(session.getId());
        subOnlineCount();
    }

    @OnMessage
    public void onMessage (RequestMessage message, Session session) throws IOException, EncodeException {
        if (message.getSubject().equals("1000")) {
            //绑定手机

        } else if (message.getSubject().equals("1001")) {
            //绑定手机

        }
        session.getBasicRemote().sendObject(message);
    }

    public void sendMessage (String message,Session session) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    public void sendMessage (ResponseMessage message, Session session) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(message);
    }

    public static synchronized  int getOnlineCount (){
        return SecuritySocket.onlineCount;
    }

    public static synchronized void addOnlineCount (){
        SecuritySocket.onlineCount++;
    }

    public static synchronized void subOnlineCount (){
        SecuritySocket.onlineCount--;
    }

}
