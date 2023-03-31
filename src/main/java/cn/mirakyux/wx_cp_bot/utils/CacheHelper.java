package cn.mirakyux.wx_cp_bot.utils;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

/**
 * CacheUtil
 *
 * @author mirakyux
 * @since 2023.03.21
 */
public class CacheHelper {
    private static final Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(120L))
                .build();

    public static void set(String key, String value) {
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.getIfPresent(key);
    }
}
