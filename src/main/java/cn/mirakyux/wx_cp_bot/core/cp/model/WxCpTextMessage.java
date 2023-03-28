package cn.mirakyux.wx_cp_bot.core.cp.model;

import cn.mirakyux.wx_cp_bot.core.cp.enumerate.MsgTypeEnum;
import cn.mirakyux.wx_cp_bot.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * WxCpTextMessage
 *
 * @author mirakyux
 * @since 2023.03.28
 */
@Data
@Builder
@Accessors(chain = true)
public class WxCpTextMessage {
    @JsonProperty("touser")
    private String toUser;

    @JsonProperty("msgtype")
    private MsgTypeEnum msgType;

    @JsonProperty("agentid")
    private String agentId;

    @JsonProperty("text")
    private Text text;

    @JsonProperty("safe")
    private String safe;

    @JsonProperty("enable_id_trans")
    private String enableIdTrans;

    @JsonProperty("enable_duplicate_check")
    private String enableDuplicateCheck;

    @JsonProperty("duplicate_check_interval")
    private String duplicateCheckInterval;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Text {
        @JsonProperty("content")
        private String content;
    }

    public void setContent(String content) {
        this.text = new Text(content);
    }

    public static WxCpTextMessage generator() {
        return WxCpTextMessage.builder()
                .msgType(MsgTypeEnum.TEXT)
                .safe("0")
                .enableIdTrans("0")
                .enableDuplicateCheck("0")
                .duplicateCheckInterval("1800")
                .build();
    }

    public static WxCpTextMessage generator(String agentId) {
        return WxCpTextMessage.builder()
                .msgType(MsgTypeEnum.TEXT)
                .agentId(agentId)
                .safe("0")
                .enableIdTrans("0")
                .enableDuplicateCheck("0")
                .duplicateCheckInterval("1800")
                .build();
    }

    public static WxCpTextMessage generator(String to, String message, String agentId) {
        return WxCpTextMessage.builder()
                .msgType(MsgTypeEnum.TEXT)
                .agentId(agentId)
                .toUser(to)
                .text(new Text(message))
                .safe("0")
                .enableIdTrans("0")
                .enableDuplicateCheck("0")
                .duplicateCheckInterval("1800")
                .build();
    }

    public String toJsonString() {
        return JsonUtil.toJsonString(this);
    }
}
