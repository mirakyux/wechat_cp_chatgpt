package cn.mirakyux.wx_cp_bot.core.event;

/**
 * OnMessageEvent
 *
 * @author mirakyux
 * @since 2023.03.24
 */
public class CallOpenAiEvent extends BaseEvent {
    public CallOpenAiEvent(String message, String to) {
        super(message, to);
    }
}
