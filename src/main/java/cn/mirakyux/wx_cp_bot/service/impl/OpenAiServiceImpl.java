package cn.mirakyux.wx_cp_bot.service.impl;

import cn.hutool.core.lang.generator.Generator;
import cn.hutool.core.lang.generator.UUIDGenerator;
import cn.hutool.http.HttpRequest;
import cn.mirakyux.wx_cp_bot.core.configuration.OpenAiConfig;
import cn.mirakyux.wx_cp_bot.core.constant.BaseConstant;
import cn.mirakyux.wx_cp_bot.core.openai.context.MessageCache;
import cn.mirakyux.wx_cp_bot.core.openai.enumerate.Model;
import cn.mirakyux.wx_cp_bot.core.openai.enumerate.Role;
import cn.mirakyux.wx_cp_bot.core.openai.model.Completion;
import cn.mirakyux.wx_cp_bot.core.openai.model.Error;
import cn.mirakyux.wx_cp_bot.core.openai.model.Message;
import cn.mirakyux.wx_cp_bot.service.OpenAiService;
import cn.mirakyux.wx_cp_bot.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
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

        String response = null;
        try {
            response = HttpRequest.post(drawUrl).addHeaders(header).body(JsonUtil.toJsonString(body)).cookie(cookie).execute().body();

            if (StringUtils.isBlank(response)) {
                return BaseConstant.NOTICE_TIMEOUT;
            }
            Completion completion = JsonUtil.parseObject(response, Completion.class);

            String result;
            Error error = completion.getError();
            if (error != null) {
                log.error("[{}] Call OpenAi Error, response: {}", id, response);
                result = error.getCode().getTips();
                MessageCache.clear(fromUser);
            } else {
                result = completion.getMessage().getContent();

                messages.add(Message.of(Role.ASSISTANT, result));
                MessageCache.put(fromUser, messages);
                log.info("[{}] gptNewComplete result:{}", id, result);
            }

            return StringUtils.trimToEmpty(result);
        } catch (Exception e) {
            log.error("[" + id + "] Call OpenAi Failed", e);
            log.error("[{}] response:{}", id, response);
            return BaseConstant.NOTICE_TIMEOUT;
        }
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
            return BaseConstant.NOTICE_TIMEOUT;
        }
        JsonNode node = JsonUtil.fromJson(response);

        return "total: " + node.get("total_granted").asText() +
                "\ntotal used: " + node.get("total_used").asText() +
                "\ntotal available: " + node.get("total_available").asText();
    }
}
