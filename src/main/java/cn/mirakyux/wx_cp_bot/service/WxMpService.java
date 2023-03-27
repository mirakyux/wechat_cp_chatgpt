package cn.mirakyux.wx_cp_bot.service;

import cn.mirakyux.wx_cp_bot.core.cp.crypto.WXBizMsgCrypt;

/**
 * WxMpService
 *
 * @author mirakyux
 * @since 2023.03.21
 */
public interface WxMpService {
    /**
     * 获取加解密实例
     *
     * @return WXBizMsgCrypt
     */
    WXBizMsgCrypt getCrypt();

    /**
     * send msg
     *
     * @param msg msg
     * @param to to
     */
    void sendMsg(String msg, String to);
}
