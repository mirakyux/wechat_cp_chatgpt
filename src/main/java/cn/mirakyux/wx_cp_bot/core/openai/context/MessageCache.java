package cn.mirakyux.wx_cp_bot.core.openai.context;

import cn.mirakyux.wx_cp_bot.core.event.SendWxCpEvent;
import cn.mirakyux.wx_cp_bot.core.openai.model.Message;
import com.google.common.cache.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * MessageCache
 *
 * @author mirakyux
 * @since 2023.03.27
 */
@Slf4j
@Component
public class MessageCache {
    private static final long CONTEXT_EXPIRE_MINUTES = 30L;

    private static ApplicationEventPublisher applicationEventPublisher;

    private static final Map<String, Object> lockMap = new HashMap<>();

    private static final Object mapLock = new Object();

    @Resource
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        MessageCache.applicationEventPublisher = applicationEventPublisher;
    }

    private static final Cache<String, List<Message>> chatGptCache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(CONTEXT_EXPIRE_MINUTES))
            .removalListener(RemovalListeners.asynchronous(new RemovalListener<String, List<Message>>() {
                /**
                 * Notifies the listener that a removal occurred at some point in the past.
                 *
                 * <p>This does not always signify that the key is now absent from the cache, as it may have
                 * already been re-added.
                 *
                 * @param notification notification
                 */
                @Override
                public void onRemoval(@Nonnull RemovalNotification<String, List<Message>> notification) {
                    RemovalCause cause = notification.getCause();
                    if (RemovalCause.REPLACED.equals(cause)) {
                        return;
                    }
                    String to = notification.getKey();
                    log.info("user[{}] context has been expired", to);
                    applicationEventPublisher.publishEvent(new SendWxCpEvent("ᓚᘏᗢ 虽然不一定准时, 但是一定有个会话已经过期了", to));
                }
            }, Executors.newSingleThreadExecutor()))
            .build();

    public static void put(String username, List<Message> gptMessageDtos) {
        chatGptCache.put(username, gptMessageDtos);
    }

    public static List<Message> addAndGet(String username, Message message) {
        List<Message> messages = chatGptCache.getIfPresent(username);
        if (messages == null) {
            synchronized (getLock(username)) {
                messages = chatGptCache.getIfPresent(username);
                if (messages == null) {
                    messages = Lists.newArrayList();
                    messages.add(message);
                    chatGptCache.put(username, messages);
                    applicationEventPublisher.publishEvent(new SendWxCpEvent("ᓚᘏᗢ 开启新的会话, 本会话将会在闲置 " + CONTEXT_EXPIRE_MINUTES + " 分钟后过期", username));
                }
            }
        }
        return messages;
    }

    public static List<Message> get(String username) {
        List<Message> messages = chatGptCache.getIfPresent(username);
        if (messages == null) {
            synchronized (getLock(username)) {
                messages = chatGptCache.getIfPresent(username);
                if (messages == null) {
                    messages = Lists.newArrayList();
                    chatGptCache.put(username, messages);
                    applicationEventPublisher.publishEvent(new SendWxCpEvent("ᓚᘏᗢ 开启新的会话, 本会话将会在闲置 " + CONTEXT_EXPIRE_MINUTES + " 分钟后过期", username));
                }
            }
        }
        return messages;
    }

    public static void clear(String username) {
        chatGptCache.invalidate(username);
    }

    private static Object getLock(String param) {
        if (!lockMap.containsKey(param)) {
            synchronized (mapLock) {
                if (!lockMap.containsKey(param)) {
                    lockMap.put(param, new Object());
                }
                return lockMap.get(param);
            }
        }
        return lockMap.get(param);
    }
}
