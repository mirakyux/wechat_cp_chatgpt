package cn.mirakyux.wx_cp_bot.core.trace.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.mirakyux.wx_cp_bot.core.trace.TraceIdHolder;
import cn.mirakyux.wx_cp_bot.core.trace.TraceIdUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 封装traceId拦截器
 *
 * @author ningjianwen
 */
@Slf4j
//@Order(-104)
//@WebFilter(urlPatterns = {"/*"}, filterName = "traceIdContextFilter")
public class TraceIdContextFilter implements Filter {

    private static final String TRACE_ID_KEY = "X-Request-Id";

    private static Set<String> EXCLUDE_URI_SET;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 访问开始时间
            long startTime = System.currentTimeMillis();
            // 获取traceId
            String traceId = ((HttpServletRequest) request).getHeader(TRACE_ID_KEY);
            boolean need2GenerateTraceId = CharSequenceUtil.isEmpty(traceId);
            String source = request.getRemoteAddr();
            String target = ((HttpServletRequest) request).getRequestURI();
            if (need2GenerateTraceId) {
                traceId = TraceIdUtil.generate();
                TraceIdHolder.set(traceId);
                log.info(":::接口请求开始::: 来源地址: {}, 目标地址: {}, 生成traceId: {}",
                        source,
                        target,
                        traceId
                );
            } else {
                TraceIdHolder.set(traceId);
                log.info(":::接口请求开始::: 来源地址: {}, 目标地址: {}", source, target
                );
            }
            HttpServletResponse res = (HttpServletResponse) response;
            res.setHeader(TRACE_ID_KEY, traceId);
            chain.doFilter(request, res);
            log.info(":::接口请求结束::: 目标地址: {}, 耗时: {} 毫秒", target, System.currentTimeMillis() - startTime);
        } finally {
            TraceIdHolder.remove();
        }
    }

    @Override
    public void destroy() {
        log.debug("[traceIdContextFilter]销毁");
    }
}
