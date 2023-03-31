package cn.mirakyux.wx_cp_bot.core.cp.enumerate;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * EventEnum
 *
 * @author mirakyux
 * @since 2023.03.31
 */
@Getter
public enum EventEnum {
    SUBSCRIBE("subscribe", "加入"),
    UNSUBSCRIBE("unsubscribe", "退出"),
    UNDEFINED("undefined", "其他");

    private final String event;

    private final String desc;

    EventEnum(String event, String desc) {
        this.event = event;
        this.desc = desc;
    }

    private static final Map<String, EventEnum> OF_MAP = Arrays.stream(EventEnum.values()).collect(Collectors.toMap(EventEnum::getEvent, Function.identity(), (a, b) -> a));

    public static EventEnum of(String msgType) {
        return OF_MAP.getOrDefault(msgType, UNDEFINED);
    }

    @Override
    public String toString() {
        return event;
    }
}
