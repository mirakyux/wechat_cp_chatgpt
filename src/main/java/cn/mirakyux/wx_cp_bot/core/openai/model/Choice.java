
package cn.mirakyux.wx_cp_bot.core.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Choice
 *
 * @author mirakyux
 * @since 2023.03.28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Choice {

    @JsonProperty("finish_reason")
    private String finishReason;

    private Long index;

    private Message message;
}
