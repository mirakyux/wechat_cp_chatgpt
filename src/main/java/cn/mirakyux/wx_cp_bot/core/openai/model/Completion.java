package cn.mirakyux.wx_cp_bot.core.openai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Completion
 *
 * @author mirakyux
 * @since 2023.03.28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Completion {
    private List<Choice> choices;

    private Long created;

    private String id;

    private String model;

    private String object;

    private Usage usage;

    public Message getMessage() {
        if (CollectionUtils.isEmpty(choices)) {
            return null;
        }
        return choices.get(0).getMessage();
    }
}
