package cn.mirakyux.wx_cp_bot.core.trace;

import cn.hutool.core.lang.generator.Generator;
import cn.hutool.core.lang.generator.UUIDGenerator;

/**
 * TraceId工具类
 *
 * @author ningjianwen
 **/
public class TraceIdUtil {
    private static final Generator<String> generator = new UUIDGenerator();

    /**
     * 生成指定类型的traceId
     *
     * @return traceId
     */
    public static String generate() {
        return generator.next();
    }
}
