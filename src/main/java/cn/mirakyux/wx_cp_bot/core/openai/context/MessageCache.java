package cn.mirakyux.wx_cp_bot.core.openai.context;

import cn.mirakyux.wx_cp_bot.core.event.SendWxCpEvent;
import cn.mirakyux.wx_cp_bot.core.openai.model.Message;
import com.github.benmanes.caffeine.cache.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final  Cache<String, List<Message>> chatGptCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(CONTEXT_EXPIRE_MINUTES))
            .scheduler(Scheduler.systemScheduler())
            .ticker(Ticker.systemTicker())
            .removalListener(new RemovalListener<String, List<Message>>() {
                @Override
                public void onRemoval(@Nullable String key, @Nullable List<Message> messages, @NonNull RemovalCause removalCause) {
                    if (RemovalCause.REPLACED.equals(removalCause)) {
                        return;
                    }
                    log.info("user[{}] context has been expired", key);
                    applicationEventPublisher.publishEvent(new SendWxCpEvent("ᓚᘏᗢ 虽然不一定准时, 但是一定有个会话已经过期了", key));
                }
            })
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
                    applicationEventPublisher.publishEvent(new SendWxCpEvent("ᓚᘏᗢ 开启新的会话, 本会话将会在闲置 " + CONTEXT_EXPIRE_MINUTES + " 分钟后过期\n你可以说 \"结束会话\" 来终止本次会话", username));
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
                    applicationEventPublisher.publishEvent(new SendWxCpEvent("ᓚᘏᗢ 开启新的会话, 本会话将会在闲置 " + CONTEXT_EXPIRE_MINUTES + " 分钟后过期\n你可以说 \"结束会话\" 来终止本次会话", username));
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
