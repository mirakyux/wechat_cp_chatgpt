package cn.mirakyux.wx_cp_bot.core.event;

/**
 * SendWxCpEvent
 *
 * @author mirakyux
 * @since 2023.03.24
 */
public class SendWxCpEvent extends BaseEvent {
    public SendWxCpEvent(String message, String to) {
        super(message, to);
    }
}
