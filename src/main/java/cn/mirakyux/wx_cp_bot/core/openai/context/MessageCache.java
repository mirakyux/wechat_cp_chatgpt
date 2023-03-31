package cn.mirakyux.wx_cp_bot.core.openai.context;

import cn.mirakyux.wx_cp_bot.core.configuration.OpenAiConfig;
import cn.mirakyux.wx_cp_bot.core.constant.BaseConstant;
import cn.mirakyux.wx_cp_bot.core.event.SendWxCpEvent;
import cn.mirakyux.wx_cp_bot.core.openai.model.Message;
import com.github.benmanes.caffeine.cache.*;
import com.google.common.collect.EvictingQueue;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

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

    private static OpenAiConfig openAiConfig;

    private static Cache<String, Queue<Message>> chatGptCache;

    private static final Map<String, Object> lockMap = new HashMap<>();

    private static final Object mapLock = new Object();

    @Resource
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        MessageCache.applicationEventPublisher = applicationEventPublisher;
    }

    @Resource
    private void setOpenAiConfig(OpenAiConfig openAiConfig) {
        MessageCache.openAiConfig = openAiConfig;
        MessageCache.chatGptCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(openAiConfig.getHoldingTime()))
                .scheduler(Scheduler.systemScheduler())
                .ticker(Ticker.systemTicker())
                .removalListener((RemovalListener<String, Queue<Message>>) (key, messages, removalCause) -> {
                    if (RemovalCause.REPLACED.equals(removalCause)) {
                        return;
                    }
                    log.info("user[{}] context has been expired", key);
                    applicationEventPublisher.publishEvent(new SendWxCpEvent(BaseConstant.CONTEXT_EXPIRE, key));
                })
                .build();
    }

    public static void put(String username, Queue<Message> messages) {
        chatGptCache.put(username, messages);
    }

    public static Queue<Message> addAndGet(String username, Message message) {
        synchronized (getLock(username)) {
            Queue<Message> messages = chatGptCache.getIfPresent(username);
            if (messages == null) {
                messages = EvictingQueue.create(openAiConfig.getMaxContext());
                applicationEventPublisher.publishEvent(new SendWxCpEvent(String.format(BaseConstant.OPEN_CONTEXT, CONTEXT_EXPIRE_MINUTES), username));
            }
            messages.add(message);
            chatGptCache.put(username, messages);
            return messages;
        }
    }

    public static Queue<Message> get(String username) {
        Queue<Message> messages = chatGptCache.getIfPresent(username);
        if (messages == null) {
            synchronized (getLock(username)) {
                messages = chatGptCache.getIfPresent(username);
                if (messages == null) {
                    messages = EvictingQueue.create(openAiConfig.getMaxContext());
                    chatGptCache.put(username, messages);
                    applicationEventPublisher.publishEvent(new SendWxCpEvent(String.format(BaseConstant.OPEN_CONTEXT, CONTEXT_EXPIRE_MINUTES), username));
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
