package cn.mirakyux.wx_cp_bot.controller;

import cn.mirakyux.wx_cp_bot.core.cp.crypto.WXBizMsgCrypt;
import cn.mirakyux.wx_cp_bot.core.cp.model.WxCpMessage;
import cn.mirakyux.wx_cp_bot.dto.WechatXmlDto;
import cn.mirakyux.wx_cp_bot.service.InvokeService;
import cn.mirakyux.wx_cp_bot.service.WxMpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * InvokeController
 *
 * @author mirakyux
 * @since 2023.03.20
 */
@Slf4j
@RestController
public class InvokeController {
    @Resource
    private InvokeService invokeService;

    @Resource
    private WxMpService wxMpService;

    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/invoke")
    public String echo(@RequestParam("msg_signature") String msg_signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("echostr") String echoStr) {
        log.info("echo, msg_signature:{}, timestamp:{}, nonce:{}, echoStr:{}",
                msg_signature, timestamp, nonce, echoStr);
        WXBizMsgCrypt crypt = wxMpService.getCrypt();

        try {
            String sEchoStr = crypt.VerifyURL(msg_signature, timestamp,
                    nonce, echoStr);
            log.info("verifyUrl echoStr: {}", sEchoStr);
            return sEchoStr;
        } catch (Exception e) {
            log.error("verifyUrl error", e);
            return "";
        }
    }


    @PostMapping(value = "/invoke",
            consumes = {"application/xml", "text/xml"},
            produces = "application/xml;charset=utf-8")
    public String invoke(@RequestParam("msg_signature") String msg_signature,
                         @RequestParam("timestamp") String timestamp,
                         @RequestParam("nonce") String nonce,
                         @RequestBody WechatXmlDto body) {
        WXBizMsgCrypt crypt = wxMpService.getCrypt();

        try {
            String msg = body.getEncrypt();
            String xmlContent = crypt.decrypt(msg);
            log.info("xml content msg: " + xmlContent);
            WxCpMessage message = WxCpMessage.fromXml(xmlContent);
            invokeService.handleMessage(message);
            String data = message.getContent();
            return data;
        } catch (Exception e) {
            log.error("DecryptMsg msg error", e);
            return "";
        }
    }
}
