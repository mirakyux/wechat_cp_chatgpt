package cn.mirakyux.wx_cp_bot.core.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * WxMpConfig
 *
 * @author mirakyux
 * @since 2023.03.21
 */
@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "wechat.cp")
public class WxCpConfig {
    @NotNull
    private String token;

    @NotNull
    private String corpID;

    @NotNull
    private String encodingAESKey;

    @NotNull
    private String corpSecret;

    @NotNull
    private String agentId;
}
