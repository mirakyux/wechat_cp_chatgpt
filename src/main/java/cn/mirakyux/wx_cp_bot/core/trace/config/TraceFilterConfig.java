package cn.mirakyux.wx_cp_bot.core.trace.config;

import cn.mirakyux.wx_cp_bot.core.trace.filter.TraceIdContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;



/**
 * TraceFilterConfig
 *
 * @author mirakyux
 * @since 2023.03.24
 */
@Configuration
public class TraceFilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> traceIdContextFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TraceIdContextFilter());
        List<String> urlList = new ArrayList<>();
        urlList.add("/*");
        registration.setUrlPatterns(urlList);
        registration.setName("traceIdContextFilter");
        registration.setOrder(-104);
        return registration;
    }
}
