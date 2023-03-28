package cn.mirakyux.wx_cp_bot.service.impl;

import cn.mirakyux.wx_cp_bot.core.cp.enumerate.MsgTypeEnum;
import cn.mirakyux.wx_cp_bot.core.cp.model.WxCpInXmlMessage;
import cn.mirakyux.wx_cp_bot.core.event.CallOpenAiEvent;
import cn.mirakyux.wx_cp_bot.core.event.SendWxCpEvent;
import cn.mirakyux.wx_cp_bot.service.InvokeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * InvokeServiceImpl
 *
 * @author mirakyux
 * @since 2023.03.24
 */
@Slf4j
@Service
public class InvokeServiceImpl implements InvokeService {
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void handleMessage(WxCpInXmlMessage message) {
        MsgTypeEnum msgType = message.getMsgType();

        switch (msgType) {
            case TEXT:
                applicationEventPublisher.publishEvent(new CallOpenAiEvent(message.getContent(), message.getFromUserName()));
                break;
            case IMAGE:
                applicationEventPublisher.publishEvent(new SendWxCpEvent("图片下载链接: " + message.getPicUrl(), message.getFromUserName()));
                break;
            default: break;
        }
    }
}
