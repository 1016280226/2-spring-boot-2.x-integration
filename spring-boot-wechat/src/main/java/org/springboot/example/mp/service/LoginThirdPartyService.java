package org.springboot.example.mp.service;


import me.chanjar.weixin.common.exception.WxErrorException;
import org.springboot.example.mp.body.LoginWechatRequestBody;

import java.util.HashMap;

@Transactional
public interface LoginThirdPartyService {

    StatefulBody loginByWxMpCode(LoginWechatRequestBody body) throws WxErrorException;

    StatefulBody loginByWxMaCode(LoginWechatRequestBody body);

    StatefulBody bindByWxma(UserBindWxRequestBody body);

    HashMap getLoginInfoByUser(User user);

}
