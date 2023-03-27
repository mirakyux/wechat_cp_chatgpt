package cn.mirakyux.wx_cp_bot.core.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * BaseEvent
 *
 * @author mirakyux
 * @since 2023.03.27
 */
@Getter
public abstract class BaseEvent extends ApplicationEvent {
    protected String message;

    protected String to;

    public BaseEvent(String message, String to) {
        super(concat(message, to));
        this.message = message;
        this.to = to;
    }

    protected static String concat(String... args) {
        if (args == null || args.length == 0) {
            return "";
        }
        return String.join("_", args);
    }
}
