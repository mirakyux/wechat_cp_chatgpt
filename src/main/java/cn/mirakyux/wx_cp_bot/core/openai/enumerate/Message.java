package cn.mirakyux.wx_cp_bot.core.openai.enumerate;

import lombok.*;

/**
 * GptDto
 *
 * @author mirakyux
 * @since 2023.03.21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Role role;
    private String content;

    public static Message of(Role role, String content) {
        return new Message(role, content);
    }
}
