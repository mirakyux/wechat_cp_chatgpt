package cn.mirakyux.wx_cp_bot.core.openai.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Model
 *
 * @author mirakyux
 * @since 2023.03.27
 */
public enum Model {
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
    ;

    @Getter
    @JsonValue
    private final String model;

    Model(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return model;
    }
}
