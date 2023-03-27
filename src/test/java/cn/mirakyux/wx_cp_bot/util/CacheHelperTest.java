package cn.mirakyux.wx_cp_bot.util;

import cn.mirakyux.wx_cp_bot.utils.CacheHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CacheHelperTest
 *
 * @author mirakyux
 * @since 2023.03.27
 */
@SpringBootTest
public class CacheHelperTest {
    @Test
    public void test() {
        System.out.println(CacheHelper.getApplicationEventPublisher());
    }
}
