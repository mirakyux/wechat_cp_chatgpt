package cn.mirakyux.wx_cp_bot.listener;

import cn.mirakyux.wx_cp_bot.core.constant.BaseConstant;
import cn.mirakyux.wx_cp_bot.core.event.CallOpenAiEvent;
import cn.mirakyux.wx_cp_bot.core.event.SendWxCpEvent;
import cn.mirakyux.wx_cp_bot.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * CallOpenAiListener
 *
 * @author mirakyux
 * @since 2023.03.24
 */
@Slf4j
@Component
public class CallOpenAiListener implements ApplicationListener<CallOpenAiEvent> {
    @Resource
    OpenAiService openAiService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Async
    @Override
    public void onApplicationEvent(CallOpenAiEvent event) {
        String data = event.getMessage();
        String fromUser = event.getTo();
        String result;
        if (BaseConstant.BALANCE.equals(data)) {
            result = openAiService.getBalance();
        } else if (BaseConstant.CLEAR_CONTEXT.equals(data)) {
            result = openAiService.clear(fromUser);
        } else {
            result = openAiService.gptNewComplete(data, fromUser);
        }

        applicationEventPublisher.publishEvent(new SendWxCpEvent(result, fromUser));
    }
}
