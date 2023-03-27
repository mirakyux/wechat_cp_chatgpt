package cn.mirakyux.wx_cp_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class WxMpBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxMpBotApplication.class, args);
    }

}
