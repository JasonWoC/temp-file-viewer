package cn.mrdear.conf;

import net.sf.ehcache.constructs.web.filter.SimplePageCachingFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.mrdear.filter.GzipFilter;

/**
 * 定义一些Filter使用
 * @author Niu Li
 * @date 2016/8/13
 */
@Configuration
@EnableCaching
public class CacheConfig {
    /**
     * 配置ehcache的Gzip压缩
     * @return
     */
    @Bean
    public FilterRegistrationBean gzipFilter(){
        FilterRegistrationBean gzipFilter = new FilterRegistrationBean(new GzipFilter());
        String[] arrs = {"*.css","*.js","*.json"};
        gzipFilter.setUrlPatterns(Arrays.asList(arrs));
        return gzipFilter;
    }
    /**
     * 配置页面缓存,页面缓存会自动开启GZIP压缩
     */
    @Bean
    public FilterRegistrationBean helloFilter(){
        FilterRegistrationBean helloFilter = new FilterRegistrationBean(new SimplePageCachingFilter());
        Map<String,String> maps = new HashMap<>();
        //设置参数
        maps.put("cacheName","hello");
        helloFilter.setInitParameters(maps);
        //设置路径
        String[] arrs = {"/12321"};
        helloFilter.setUrlPatterns(Arrays.asList(arrs));
        return helloFilter;
    }
    /*
    * 据shared与否的设置,
    * Spring分别通过CacheManager.create()
    * 或new CacheManager()方式来创建一个ehcache基地.
    *
    * 也说是说通过这个来设置cache的基地是这里的Spring独用,还是跟别的(如hibernate的Ehcache共享)
    *
    */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean ();
        cacheManagerFactoryBean.setConfigLocation (new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }
    /**
     *  ehcache 主要的管理器
     * @param bean
     * @return
     */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean){
        return new EhCacheCacheManager(bean.getObject());
    }

}
