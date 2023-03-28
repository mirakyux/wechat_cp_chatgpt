package cn.mirakyux.wx_cp_bot.service;

import cn.mirakyux.wx_cp_bot.core.cp.model.WxCpInXmlMessage;

/**
 * InvokeService
 *
 * @author mirakyux
 * @since 2023.03.24
 */
public interface InvokeService {
    void handleMessage(WxCpInXmlMessage message);
}
