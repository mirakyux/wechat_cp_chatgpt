package cn.mirakyux.wx_cp_bot.core.trace.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.mirakyux.wx_cp_bot.core.trace.TraceIdHolder;

/**
 * logback获取TraceId
 *
 * @author ningjianwen
 */
public class TraceIdConverter extends ClassicConverter {
    private static final String DEFAULT = "empty#0";

    @Override
    public String convert(ILoggingEvent event) {
        String traceId = TraceIdHolder.get();
        if (null == traceId) {
            return DEFAULT;
        }
        return traceId;
    }
}
