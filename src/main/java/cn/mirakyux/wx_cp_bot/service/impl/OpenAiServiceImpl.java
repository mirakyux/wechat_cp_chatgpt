package cn.mirakyux.wx_cp_bot.service.impl;

import cn.hutool.core.lang.generator.Generator;
import cn.hutool.core.lang.generator.UUIDGenerator;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.mirakyux.wx_cp_bot.core.configuration.OpenAiConfig;
import cn.mirakyux.wx_cp_bot.core.constant.BaseConstant;
import cn.mirakyux.wx_cp_bot.core.openai.context.MessageCache;
import cn.mirakyux.wx_cp_bot.core.openai.enumerate.Model;
import cn.mirakyux.wx_cp_bot.core.openai.enumerate.Role;
import cn.mirakyux.wx_cp_bot.core.openai.enumerate.Message;
import cn.mirakyux.wx_cp_bot.service.OpenAiService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * OpenAiServiceImpl
 *
 * @author mirakyux
 * @since 2023.03.21
 */
@Slf4j
@Service
public class OpenAiServiceImpl implements OpenAiService {
    @Resource
    OpenAiConfig openAiConfig;

    private static final Generator<String> generator = new UUIDGenerator();

    /**
     * openai GPT 3.5 complete功能
     * 接口文档：<a href="https://platform.openai.com/docs/api-reference/completions/create">...</a>
     *
     * @param text text
     * @param fromUser fromUser
     * @return result
     */
    @Override
    public String gptNewComplete(String text, String fromUser) {
        String id = generator.next();
        log.info("[{}] user: {}, text: {}", id, fromUser, text);
        Map<String, String> header = Maps.newHashMap();
        String drawUrl = "https://api.openai.com/v1/chat/completions";
        String cookie = "";
        header.put("Authorization", "Bearer " + openAiConfig.getApiKey());
        Map<String, Object> body = Maps.newHashMap();
        List<Message> messages = MessageCache.get(fromUser);

        messages.add(Message.of(Role.USER, text));
        body.put("model", Model.GPT_3_5_TURBO);
        body.put("messages", messages);
        body.put("max_tokens", openAiConfig.getMaxTokens());
        body.put("temperature", openAiConfig.getTemperature());

        String response;
        try {
            response = HttpRequest.post(drawUrl).addHeaders(header).body(JSONUtil.toJsonStr(body)).cookie(cookie).execute().body();
        } catch (Exception e) {
            log.error("[" + id + "] Call OpenAi Failed", e);
            return BaseConstant.NOTICE_TIMEOUT;
        }

        if (StringUtils.isBlank(response)) {
            return BaseConstant.NOTICE_TIMEOUT;
        }
        JSONObject jsonObject = JSONUtil.parseObj(response);
        JSONArray jsonArray = jsonObject.getJSONArray("choices");
        JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
        JSONObject jsonObject2 = (JSONObject) jsonObject1.get("message");
        String result = (String) jsonObject2.get("content");
        log.info("[{}] gptNewComplete result:{}", id, result);

        messages.add(Message.of(Role.ASSISTANT, result));
        MessageCache.put(fromUser, messages);
        return StringUtils.trimToEmpty(result);
    }

    /**
     * 余额
     *
     * @return 余额
     */
    @Override
    public String getBalance() {
        Map<String, String> header = Maps.newHashMap();
        String url = "https://api.openai.com/dashboard/billing/credit_grants";
        header.put("Authorization", "Bearer " + openAiConfig.getApiKey());

        String response;
        try {
            response = HttpRequest.get(url).addHeaders(header).execute().body();
        } catch (Exception e) {
            return "访问超时";
        }
        JSONObject jsonObject = JSONUtil.parseObj(response);

        return "total: " + jsonObject.getStr("total_granted") +
                "\ntotal used: " + jsonObject.getStr("total_used") +
                "\ntotal available: " + jsonObject.getStr("total_available");
    }
}
