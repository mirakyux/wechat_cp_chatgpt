package cn.mirakyux.wx_cp_bot.core.constant;

/**
 * UrlConstant
 *
 * @author mirakyux
 * @since 2023.03.31
 */
public class UrlConstant {
    public static final String WX_CP_ACCESS_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";
    
    public static final String WX_CP_REQUEST_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    public static final String OPEN_AI_CHAT_COMPLETIONS = "https://api.openai.com/v1/chat/completions";

    public static final String OPEN_AI_BILLING = "https://api.openai.com/dashboard/billing/credit_grants";
}
