package cn.mirakyux.wx_cp_bot.core.openai.context;

import cn.mirakyux.wx_cp_bot.core.event.SendWxCpEvent;
import cn.mirakyux.wx_cp_bot.core.openai.enumerate.Message;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

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

    @Resource
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        MessageCache.applicationEventPublisher = applicationEventPublisher;
    }

    private static final Cache<String, List<Message>> chatGptCache = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(CONTEXT_EXPIRE_MINUTES))
            .build();

    public static void put(String username, List<Message> gptMessageDtos) {
        chatGptCache.put(username, gptMessageDtos);
    }

    public static List<Message> get(String username) {
        List<Message> messages = chatGptCache.getIfPresent(username);
        if (CollectionUtils.isEmpty(messages)) {
            applicationEventPublisher.publishEvent(new SendWxCpEvent("开启新的会话, 本会话将会在闲置 " + CONTEXT_EXPIRE_MINUTES + " 分钟后过期", username));
            return Lists.newArrayList();
        }
        return messages;
    }
}
