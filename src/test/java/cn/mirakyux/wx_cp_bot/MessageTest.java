package cn.mirakyux.wx_cp_bot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.Resource;

/**
 * MessageTest
 *
 * @author mirakyux
 * @since 2023.03.27
 */
@SpringBootTest
public class MessageTest {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;
    @Test
    public void test() {
        // applicationEventPublisher.publishEvent(new CallOpenAiEvent("message", "to"));
    }
}
