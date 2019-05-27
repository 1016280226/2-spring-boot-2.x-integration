package org.springboot.example.mp.endpoint;

import com.yingding.daikou.core.constant.RedisConstant;
import com.yingding.daikou.core.enums.ResultEnum;
import com.yingding.daikou.core.exception.DkException;
import com.yingding.daikou.core.utils.ResultUtil;
import com.yingding.daikou.core.vo.ResultVO;
import com.yingding.daikou.employee.wechat.config.WechatConfig;
import com.yingding.daikou.model.DkEmployee;
import com.yingding.daikou.mybatis.mapper.DkEmployeeMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>版权：广东赢鼎
 * <p>日期：2018/4/22</p>
 * <p>作者：Eddie</p>
 */
@Api(value = "Weixin", tags = "微信 API")
@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("wechat")
@Slf4j
public class WechatController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private DkEmployeeMapper employeeMapper;
    @Autowired
    private WxMpMessageRouter wxMpMessageRouter;

    @ApiIgnore
    @RequestMapping("")
    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
                "raw" :
                request.getParameter("encrypt_type");

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            response.getWriter().write(outMessage.toXml());
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(),wxMpService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            response.getWriter().write(outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage()));
            return;
        }

        response.getWriter().println("不可识别的加密类型");
        return;
    }

    @ApiIgnore
    @RequestMapping("authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
        String urlToken= UUID.randomUUID().toString();
        log.info("【微信网页授权】returnUrl={}", returnUrl);
        redisTemplate.opsForValue().set(urlToken, URLEncoder.encode(returnUrl, "UTF-8"),3, TimeUnit.DAYS);
        String url=wechatConfig.getAuthorizeUrl()+"/wechat/userInfo";
        String redirectUrl=wxMpService.oauth2buildAuthorizationUrl(url,
                WxConsts.OAUTH2_SCOPE_USER_INFO,
                urlToken);
        return "redirect:"+redirectUrl;
    }

    @ApiIgnore
    @RequestMapping("userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String state) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken=new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken=wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("【微信网页授权】{}",e);
            throw new DkException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        String openId=wxMpOAuth2AccessToken.getOpenId();
        log.info("【微信网页授权】openId={}", openId);

        //缓存用户openid
        String token= UUID.randomUUID().toString();
        String redisKey=String.format(RedisConstant.OPENID_PERFIX, token);
        // 2 小时失效
        redisTemplate.opsForValue().set(redisKey, openId, RedisConstant.OPENID_EXPIRE, TimeUnit.SECONDS);

        String returnUrl=redisTemplate.opsForValue().get(state);

        try {
            returnUrl= URLDecoder.decode(returnUrl, "UTF-8");
            log.info("【微信网页授权】returnUrl={}, state={}", returnUrl,state);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(StringUtils.isBlank(returnUrl))
            throw new DkException(ResultEnum.WECHAT_MP_ERROR.getCode(),"微信授权失败");

        if(returnUrl.indexOf("?")>-1)
            returnUrl=returnUrl+"&token="+token;
        else
            returnUrl=returnUrl+"?token="+token;

        return "redirect:"+returnUrl;//用于客户端判断用户是否有关注
    }

    @RequestMapping("jssdk")
    @ResponseBody
    public ResultVO jssdk(@RequestParam("url") String url) throws WxErrorException {
        WxJsapiSignature jsapiSignature = wxMpService.createJsapiSignature(url);
        log.info("JsapiSignature={}", jsapiSignature);
        return ResultUtil.success(jsapiSignature);
    }

    @ApiIgnore
    @RequestMapping("subscribe")
    @ResponseBody
    public ResultVO subscribe(@RequestParam("token") String token){
        String redisKey=String.format(RedisConstant.OPENID_PERFIX, token);
        String openid=redisTemplate.opsForValue().get(redisKey);
        if(StringUtils.isBlank(openid)) {
            log.error("token无效或已过期,key={},={}", token, openid);
            throw new DkException(ResultEnum.ACCESS_TOKEN_INVALID.getCode(), "token无效或已过期");
        }
        Boolean subscribe=false;
        try {
            WxMpUser user =wxMpService.getUserService().userInfo(openid);
            DkEmployee employee = employeeMapper.selectOne(DkEmployee.builder().openid(user.getOpenId()).isDelete(Boolean.FALSE).build());
            if(null == employee){
                subscribe = false;
            }else{
                subscribe = true;
            }
//            subscribe=user.getSubscribe();
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ResultUtil.success(subscribe?1:0);//
    }
}
