package cn.mirakyux.wx_cp_bot.listener;

import cn.mirakyux.wx_cp_bot.core.event.SendWxCpEvent;
import cn.mirakyux.wx_cp_bot.service.WxMpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * SendWxCpListener
 *
 * @author mirakyux
 * @since 2023.03.24
 */
@Slf4j
@Component
public class SendWxCpListener implements ApplicationListener<SendWxCpEvent> {
    @Resource
    private WxMpService wxMpService;

    @Async
    @Override
    public void onApplicationEvent(SendWxCpEvent event) {
        wxMpService.sendMsg(event.getMessage(), event.getTo());
    }
}
