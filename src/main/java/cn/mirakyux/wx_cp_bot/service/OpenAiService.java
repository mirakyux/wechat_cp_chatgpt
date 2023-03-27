package cn.mirakyux.wx_cp_bot.service;

/**
 * OpenAiService
 *
 * @author mirakyux
 * @since 2023.03.21
 */
public interface OpenAiService {
    /**
     * openai GPT 3.5 complete功能
     * 接口文档：<a href="https://platform.openai.com/docs/api-reference/completions/create">...</a>
     *
     * @param text text
     * @param fromUser fromUser
     * @return result
     */
    String gptNewComplete(String text, String fromUser);

    /**
     * 余额
     *
     * @return 余额
     */
    String getBalance();
}
