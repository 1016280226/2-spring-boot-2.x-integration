package org.springboot.example.mp.config;

import com.yingding.daikou.enums.EmployeeStateEnum;
import com.yingding.daikou.model.DkEmployee;
import com.yingding.daikou.mybatis.mapper.DkEmployeeMapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 微信公众号配置器
 * <p>版权：广东赢鼎
 * <p>日期：2017/12/12</p>
 * <p>作者：Eddie</p>
 */
@Component
@Slf4j
public class WechatMpConfig {
    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private DkEmployeeMapper employeeMapper;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId(wechatConfig.getAppId());
        wxMpConfigStorage.setSecret(wechatConfig.getAppSecret());
        wxMpConfigStorage.setToken(wechatConfig.getToken());
        return wxMpConfigStorage;
    }


    @Bean
    public WxMpMessageRouter wxMpMessageRouter(){
        WxMpMessageHandler handler = new WxMpMessageHandler() {
            @Override
            public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
                WxMpXmlOutTextMessage m=null;
                log.info("【微信触发类型】{}", wxMpXmlMessage);
                switch (wxMpXmlMessage.getMsgType()){
                    case "event":
                        if(wxMpXmlMessage.getEvent().equalsIgnoreCase("SCAN")){
                            m = processSCAN(wxMpXmlMessage);
                        }else if(wxMpXmlMessage.getEvent().equalsIgnoreCase("subscribe")){
                            m = processSCAN(wxMpXmlMessage);
                        }else if(wxMpXmlMessage.getEvent().equalsIgnoreCase("LOCATION")){
                            return WxMpXmlOutMessage.TEXT().build();
                        }
                        else
                            m=processText(wxMpXmlMessage);
                        break;
                    case "text":
                        m=processText(wxMpXmlMessage);
                        break;
                    default:
                        m=processText(wxMpXmlMessage);
                        break;
                }
                return m;
            }
        };
        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxMpService());
        wxMpMessageRouter
                .rule()
                .async(false)
                .handler(handler)
                .end();
        return wxMpMessageRouter;
    }

    private WxMpXmlOutTextMessage processText(WxMpXmlMessage wxMpXmlMessage) {
        String key = wxMpXmlMessage.getEventKey();//参数为用户ID
        String openid=wxMpXmlMessage.getFromUser();//微信用户的openid;
        String content="";
        if(null!=wxMpXmlMessage.getContent())
            content=wxMpXmlMessage.getContent();
        StringBuilder msg=new StringBuilder();
        switch (wxMpXmlMessage.getEventKey()){
            case "default":
                msg.append(wechatConfig.getDefaultMpMessage().replace("\\n", "\n"));
                break;
            default:
                msg.append("未找到关键字");
                break;
        }
        log.info("【回复内容】{}", msg.toString());
        return WxMpXmlOutMessage.TEXT().content(msg.toString())
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }

    private WxMpXmlOutTextMessage processSCAN(WxMpXmlMessage wxMpXmlMessage){
        String key = wxMpXmlMessage.getEventKey();//参数为用户ID
        String openid=wxMpXmlMessage.getFromUser();//微信用户的openid;
        String content="";

        // 根据openid 查出当前业务员是否存在。
        DkEmployee employee= employeeMapper.selectOne(DkEmployee.builder().openid(openid).build());

        // 根据key先查出当前业务员是否存在
        DkEmployee employee1 = employeeMapper.selectOne(DkEmployee.builder().code(key).build());
        if(employee == null && employee1 != null){
            if(StringUtils.isNotBlank(employee1.getOpenid())){
                content="用户："+employee.getName()+" 已经绑定微信，请勿重复绑定";
            }else {
                employee1.setOpenid(openid);
                employee1.setState(EmployeeStateEnum.ENABLED.getCode());
                employeeMapper.updateByPrimaryKey(employee1);//保存
                content = "绑定成功";
            }
        }else {
            content="该微信已绑定："+employee.getName();
        }
        WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
        return m;
    }

}
