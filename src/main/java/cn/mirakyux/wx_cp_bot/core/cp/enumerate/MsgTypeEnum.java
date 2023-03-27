package cn.mirakyux.wx_cp_bot.core.cp.enumerate;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MsgTypeEnum
 *
 * @author mirakyux
 * @since 2023.03.22
 */
@Getter
public enum MsgTypeEnum {
    TEXT("text", "文本"),
    IMAGE("image", "图像"),
    VOICE("voice", "语音"),
    VIDEO("video", "视频"),
    LOCATION("location", "地理位置"),
    LINK("link", "链接"),
    UNDEFINED("undefined", "其他");

    private final String msgType;
    private final String desc;

    MsgTypeEnum(String msgType, String desc) {
        this.msgType = msgType;
        this.desc = desc;
    }

    private static final Map<String, MsgTypeEnum> OF_MAP = Arrays.stream(MsgTypeEnum.values()).collect(Collectors.toMap(MsgTypeEnum::getMsgType, Function.identity(), (a, b) -> a));

    public static MsgTypeEnum of(String msgType) {
        return OF_MAP.getOrDefault(msgType, UNDEFINED);
    }

    @Override
    public String toString() {
        return msgType;
    }
}
