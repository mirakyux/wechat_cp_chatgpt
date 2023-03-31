package cn.mirakyux.wx_cp_bot.core.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * OpenAiConfig
 *
 * @author mirakyux
 * @since 2023.03.21
 */
@Getter
@Setter
@Component
@ConfigurationProperties(
        prefix = "open-ai"
)
public class OpenAiConfig {
    private String apiKey;

    private Integer maxContext;

    private Integer holdingTime;

    private Long maxTokens;

    private BigDecimal temperature;
}
