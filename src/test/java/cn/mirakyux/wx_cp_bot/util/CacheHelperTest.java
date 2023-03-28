package cn.mirakyux.wx_cp_bot.util;

import cn.mirakyux.wx_cp_bot.core.openai.model.Completion;
import cn.mirakyux.wx_cp_bot.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CacheHelperTest
 *
 * @author mirakyux
 * @since 2023.03.27
 */
@SpringBootTest
public class CacheHelperTest {
    @Test
    public void test() {
        String response = "{\"id\":\"chatcmpl-6yyjk7NcZLAiIajr1kbestwSNvpfU\",\"object\":\"chat.completion\",\"created\":1679991172,\"model\":\"gpt-3.5-turbo-0301\",\"usage\":{\"prompt_tokens\":10,\"completion_tokens\":9,\"total_tokens\":19},\"choices\":[{\"message\":{\"role\":\"assistant\",\"content\":\"Hello! How can I assist you today?\"},\"finish_reason\":\"stop\",\"index\":0}]}\n";

        JsonNode node = JsonUtil.fromJson(response);
        JsonNode choices = node.get("choices");

        String result = choices.get(0).get("message").get("content").asText();

        Completion completion = JsonUtil.parseObject(response, Completion.class);
        System.out.println(completion);

    }
}
