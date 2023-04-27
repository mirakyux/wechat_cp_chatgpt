package cn.mirakyux.wx_cp_bot.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.mirakyux.wx_cp_bot.core.configuration.WxCpConfig;
import cn.mirakyux.wx_cp_bot.core.constant.UrlConstant;
import cn.mirakyux.wx_cp_bot.core.cp.crypto.WXBizMsgCrypt;
import cn.mirakyux.wx_cp_bot.core.cp.model.WxCpTextMessage;
import cn.mirakyux.wx_cp_bot.service.WxMpService;
import cn.mirakyux.wx_cp_bot.utils.CacheHelper;
import cn.mirakyux.wx_cp_bot.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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
    WxCpConfig wxCpConfig;

    private static final String WECHAT_TOKEN = "WECHAT_TOKEN";

    private String getAccessToken() {

        String data = CacheHelper.get(WECHAT_TOKEN);
        if (StringUtils.isNotEmpty(data)) {
            log.info("cache data:{}", data);
            return data;
        }
        
        String url = String.format(UrlConstant.WX_CP_ACCESS_URL, wxCpConfig.getCorpID(), wxCpConfig.getCorpSecret());

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
        return new WXBizMsgCrypt(wxCpConfig.getToken(), wxCpConfig.getEncodingAESKey(), wxCpConfig.getCorpID());
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
        String url = String.format(UrlConstant.WX_CP_REQUEST_URL, accessToken);

        List<String> messages = splitMessage(msg);

        messages.forEach(message -> {
            WxCpTextMessage textMessage = WxCpTextMessage.generator(to, message, wxCpConfig.getAgentId());

            try (HttpResponse response = HttpRequest.post(url).body(JsonUtil.toJsonString(textMessage)).execute()) {
                String result = response.body();
                log.info("Response: {}", result);
                Thread.sleep(wxCpConfig.getWaitTime());
            } catch (Exception e) {
                log.error("sendMsg error", e);
            }
        });
    }

    private List<String> splitMessage(String msg) {
        if (getByteCount(msg) < wxCpConfig.getMessageLength()) {
            return Lists.newArrayList(msg);
        }

        return splitStringByBytes(msg, wxCpConfig.getMessageLength());
    }

    public static List<String> splitStringByBytes(String str, int length) {
        List<String> result = Lists.newArrayList();
        if (str == null || str.isEmpty() || length < 1) {
            return result;
        }
        int i = 0;
        while (i < str.length()) {
            int offset = 1;
            while (i + offset <= str.length() && getByteCount(str.substring(i, i + offset)) <= length) {
                offset++;
            }
            offset--;
            result.add(str.substring(i, i + offset));
            i += offset;
        }
        return result;
    }

    public static int getByteCount(String content) {
        if (StringUtils.isEmpty(content)) {
            // 汉字采用utf-8编码时占3个字节
            return 0;
        }
        return content.getBytes(StandardCharsets.UTF_8).length;
    }
}
