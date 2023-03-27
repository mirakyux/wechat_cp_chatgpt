package cn.mirakyux.wx_cp_bot.core.trace;


/**
 * TraceIdHolder
 * 简单traceId实现，不做服务层级的区别了
 * @author ningjianwen
 */
public class TraceIdHolder {

    private static final ThreadLocal<String> TRACE_ID_CONTEXT = new ThreadLocal<>();

    /**
     * 开关开启的情况下，将traceId放入容器中
     *
     * @param traceId traceId
     */
    public static void set(String traceId) {
        TRACE_ID_CONTEXT.set(traceId);
    }


    /**
     * 开关开启的情况下，尝试返回容器中的traceId
     * 开关关闭情况下返回null
     *
     * @return traceId
     */
    public static String get() {
        return TRACE_ID_CONTEXT.get();
    }

    /**
     * 删除保存的用户
     */
    public static void remove() {
        TRACE_ID_CONTEXT.remove();
    }
}
