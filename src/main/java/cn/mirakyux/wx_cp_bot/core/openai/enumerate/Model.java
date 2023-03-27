package cn.mirakyux.wx_cp_bot.core.openai.enumerate;

import lombok.Getter;

/**
 * Model
 *
 * @author mirakyux
 * @since 2023.03.27
 */
public enum Model {
    GPT_3_5_TURBO("gpt-3.5-turbo");

    @Getter
    private final String model;

    Model(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return model;
    }
}
