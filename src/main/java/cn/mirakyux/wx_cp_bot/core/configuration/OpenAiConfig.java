package cn.mirakyux.wx_cp_bot.core.configuration;

import cn.mirakyux.wx_cp_bot.core.constant.UrlConstant;
import cn.mirakyux.wx_cp_bot.core.openai.enumerate.Model;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * OpenAiConfig
 *
 * @author mirakyux
 * @since 2023.03.21
 */
@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "open-ai")
public class OpenAiConfig {
    private String url = UrlConstant.OPEN_AI_CHAT_COMPLETIONS;

    @NotNull
    private String apiKey;

    private Integer maxContext = 16;

    private Integer holdingTime = 30;

    private Long maxTokens = 1024L;

    private BigDecimal temperature = BigDecimal.ONE;

    private Model model = Model.GPT_3_5_TURBO;
}
