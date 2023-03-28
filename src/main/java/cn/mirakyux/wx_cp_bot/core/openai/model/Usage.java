
package cn.mirakyux.wx_cp_bot.core.openai.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Usage
 *
 * @author mirakyux
 * @since 2023.03.28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usage {

    @JsonProperty("completion_tokens")
    private Long completionTokens;
    @JsonProperty("prompt_tokens")
    private Long promptTokens;
    @JsonProperty("total_tokens")
    private Long totalTokens;
}
