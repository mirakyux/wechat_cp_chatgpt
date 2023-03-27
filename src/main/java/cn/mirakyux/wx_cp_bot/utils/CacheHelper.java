package cn.mirakyux.wx_cp_bot.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * CacheUtil
 *
 * @author mirakyux
 * @since 2023.03.21
 */
public class CacheHelper {
    private static final Cache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(120, TimeUnit.MINUTES)
                .build();

    public static void set(String key, String value) {
        cache.put(key, value);
    }

    public static String get(String key) {
        return cache.getIfPresent(key);
    }
}
