package cn.mirakyux.wx_cp_bot.service.impl;

import cn.hutool.http.HttpRequest;
import cn.mirakyux.wx_cp_bot.core.configuration.WxMpConfig;
import cn.mirakyux.wx_cp_bot.core.cp.crypto.WXBizMsgCrypt;
import cn.mirakyux.wx_cp_bot.core.cp.model.WxCpTextMessage;
import cn.mirakyux.wx_cp_bot.service.WxMpService;
import cn.mirakyux.wx_cp_bot.utils.CacheHelper;
import cn.mirakyux.wx_cp_bot.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * WxMpServiceImpl
 *
 * @author mirakyux
 * @since 2023.03.21
 */
@Slf4j
@Service
public class WxMpServiceImpl implements WxMpService {
    @Resource
    WxMpConfig wxMpConfig;

    private static final String WECHAT_TOKEN = "WECHAT_TOKEN";

    private String getAccessToken() {

        String data = CacheHelper.get(WECHAT_TOKEN);
        if (StringUtils.isNotEmpty(data)) {
            log.info("cache data:{}", data);
            return data;
        }
        String corpId = wxMpConfig.getCorpID();
        String corpSecret = wxMpConfig.getCorpSecret();
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId +
                "&corpsecret=" + corpSecret;

        String jsonData = HttpRequest.get(url).execute().body();

        JsonNode json = JsonUtil.fromJson(jsonData);
        String accessToken = json.get("access_token").asText();
        CacheHelper.set(WECHAT_TOKEN, accessToken);
        return accessToken;
    }

    /**
     * 获取加解密实例
     *
     * @return WXBizMsgCrypt
     */
    @Override
    public WXBizMsgCrypt getCrypt() {
        return new WXBizMsgCrypt(wxMpConfig.getToken(), wxMpConfig.getEncodingAESKey(), wxMpConfig.getCorpID());
    }

    /**
     * send msg
     *
     * @param msg msg
     * @param to  to
     */
    @Override
    public void sendMsg(String msg, String to) {
        String accessToken;
        try {
            accessToken = getAccessToken();
        } catch (Exception e) {
            log.error("sendMsg getAccessToken error", e);
            return;
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

        WxCpTextMessage message = WxCpTextMessage.generator(to, msg, wxMpConfig.getAgentId());

        try {
            String response = HttpRequest.post(url).body(JsonUtil.toJsonString(message)).execute().body();
            log.info("Response: {}", response);
        } catch (Exception e) {
            log.error("sendMsg error", e);
        }
    }
}
