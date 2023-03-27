package cn.mirakyux.wx_cp_bot.core.exception;

public class BusinessException extends RuntimeException {
    BusinessException(String msg) {
        super(msg);
    }
}
