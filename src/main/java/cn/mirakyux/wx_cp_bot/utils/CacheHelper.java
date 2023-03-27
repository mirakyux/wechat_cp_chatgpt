package cn.mirakyux.wx_cp_bot.utils;

import cn.mirakyux.wx_cp_bot.core.event.SendWxCpEvent;
import cn.mirakyux.wx_cp_bot.core.openai.enumerate.Message;
import com.google.common.cache.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * CacheUtil
 *
 * @author mirakyux
 * @since 2023.03.21
 */
@Slf4j
@Component
public class CacheHelper {
    private static final long CONTEXT_EXPIRE_MINUTES = 30L;

    private static final Cache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(120, TimeUnit.MINUTES)
                .build();

    private static final Cache<String, List<Message>> chatGptCache = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(CONTEXT_EXPIRE_MINUTES))
            .removalListener(new RemovalListener<String, List<Message>>() {
                /**
                 * Notifies the listener that a removal occurred at some point in the past.
                 *
                 * <p>This does not always signify that the key is now absent from the cache, as it may have
                 * already been re-added.
                 *
                 * @param notification notification
                 */
                @Override
                public void onRemoval(RemovalNotification<String, List<Message>> notification) {
                    RemovalCause cause = notification.getCause();
                    if (RemovalCause.REPLACED.equals(cause)) {
                        return;
                    }
                    String to = notification.getKey();
                    log.info("user[{}] context has been expired", to);
                    SpringContextUtils.getBean(ApplicationEventPublisher.class).publishEvent(new SendWxCpEvent(CONTEXT_EXPIRE_MINUTES + " 分钟未产生新的对话, 会话已过期", to));
                }
            })
            .build();

    public static void set(String key, String value) {
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.getIfPresent(key);
    }

    public static void setGptCache(String username, List<Message> gptMessageDtos) {
        chatGptCache.put(username, gptMessageDtos);
    }

    public static List<Message> getGptCache(String username) {
        List<Message> messages = chatGptCache.getIfPresent(username);
        if (CollectionUtils.isEmpty(messages)) {
            return Lists.newArrayList();
        }
        return messages;
    }
}
