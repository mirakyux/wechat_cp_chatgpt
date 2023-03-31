package cn.mirakyux.wx_cp_bot.core.openai.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ErrorCode
 *
 * @author mirakyux
 * @since 2023.03.29
 */
public enum ErrorCode {
    CONTEXT_LENGTH_EXCEEDED("context_length_exceeded", "ᓚᘏᗢ 刚才中了失忆术, 之前说的什么我都忘了! 再说一次吧(超出上下文大小限制, 所以清空了, 尽量精简一下刚才的提问, 整理一下信息重新提问吧)"),
    NONE("none", "ᓚᘏᗢ 我也不知道发生了啥, 总之再试一次吧(未知错误, 需要看日志)");

    private static final Map<String, ErrorCode> OF_MAP = Arrays.stream(ErrorCode.values()).collect(Collectors.toMap(ErrorCode::getCode, Function.identity(), (a, b) -> a));

    @Getter
    @JsonValue
    final private String code;

    @Getter
    final private String tips;

    ErrorCode(String code, String tips) {
        this.code = code;
        this.tips = tips;
    }

    /**
     * ErrorCodeConverter
     *
     * @author mirakyux
     * @since 2023.03.29
     */
    public static class ErrorCodeConverter implements Converter<String, ErrorCode> {
        /**
         * Main conversion method.
         *
         * @param value value
         */
        @Override
        public ErrorCode convert(String value) {
            return OF_MAP.getOrDefault(value, NONE);
        }

        /**
         * Method that can be used to find out actual input (source) type; this
         * usually can be determined from type parameters, but may need
         * to be implemented differently from programmatically defined
         * converters (which cannot change static type parameter bindings).
         *
         * @param typeFactory typeFactory
         * @since 2.2
         */
        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(String.class);
        }

        /**
         * Method that can be used to find out actual output (target) type; this
         * usually can be determined from type parameters, but may need
         * to be implemented differently from programmatically defined
         * converters (which cannot change static type parameter bindings).
         *
         * @param typeFactory typeFactory
         * @since 2.2
         */
        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(ErrorCode.class);
        }
    }
}
