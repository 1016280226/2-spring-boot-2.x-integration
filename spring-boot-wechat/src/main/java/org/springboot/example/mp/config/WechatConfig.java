package com.yingding.daikou.employee.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>版权：广东赢鼎
 * <p>日期：2018/4/20</p>
 * <p>作者：Eddie</p>
 */
@Component
@ConfigurationProperties("wechat")
@Data
public class WechatConfig {
    private String appId;
    private String appSecret;
    private String token;
    private String oauthType;
    private String authorizeUrl;

    /**
     * 默认回复消息内容
     */
    private String defaultMpMessage;
}
