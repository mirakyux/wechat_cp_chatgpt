package cn.mirakyux.wx_cp_bot.core.openai.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * RoleEnum
 *
 * @author mirakyux
 * @since 2023.03.27
 */
public enum Role {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    @Getter
    @JsonValue
    private final String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
