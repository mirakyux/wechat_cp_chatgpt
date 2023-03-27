package cn.mirakyux.wx_cp_bot.core.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WxMpConfig
 *
 * @author mirakyux
 * @since 2023.03.21
 */
@Getter
@Setter
@Component
@ConfigurationProperties(
        prefix = "wechat",
        ignoreUnknownFields = true)
public class WxMpConfig {
    private String token;
    private String corpID;
    private String encodingAESKey;

    private String corpSecret;

    private String agentId;
}
