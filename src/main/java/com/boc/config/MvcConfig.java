package com.boc.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
@MapperScan("com.boc.mapper")
public class MvcConfig extends WebMvcConfigurerAdapter {
    
    /**
     * 访问对应的映射会自动跳转到对应视图
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/main").setViewName("dashboard");
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
        //拦截所有请求,springBoot做好了静态资源映射,如果访问的是静态资源,则不会通过拦截器,判断是否静态资源,就是看访问的是不是
        //静态资源文件夹下面的东西
        .addPathPatterns("/**")     
        .excludePathPatterns("/", "/user/login"); //除了登陆界面和登陆请求
    }
    
    /**
     * 根据参数返回不同的国际化对象,如果没有这个Bean,WebMvcAutoConfiguration会去创建一个LocaleResolver的Bean,
     * 它会根据请求头的信息来获取语言信息,然后返回不同的区域对象,而自己返回区域对象的话,目前是根据l参数,来返回比如是:zh_CN,那么就会
     * 用这个参数来创建一个Locale对象, 然后spring会根据这个对象去找不同的配置文件,比如返回的是zh CN,spring就会去找basename
     * 里面后缀为_zh_CN的配置文件,如果找不到这个文件,就会去找默认的,默认语言文件还是必须的,如果没有的话,会对所有的语言信息造成影响(失败)
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new LocaleResolver() {

            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String l = String.valueOf(request.getParameter("l"));
                if (!StringUtils.isEmpty(l) && !"null".equals(l)) {
                    String[] arr = l.toString().split("_");
                    Assert.isTrue(arr.length > 1, "国际化参数异常!");
                    return new Locale(arr[0], arr[1]);
                }
                return Locale.SIMPLIFIED_CHINESE;
            }

            @Override
            public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {}
            
        };
    }
    
    /**
     * @see ErrorMvcAutoConfiguration
     * 自定义错误数据,具体逻辑参考 {@link ErrorMvcAutoConfiguration} 和其中的两个bean {@link BasicErrorController} {@link DefaultErrorAttributes}
     * 其中DefaultErrorAttributes是定义错误数据,也就是发生错误后,spring mvc自动处理的话哪些参数放进请求域中,BasicErrorController决定了spring mvc自动处理错误的时候
     * 返回什么视图,然后在视图中使用DefaultErrorAttributes这些数据,比如里面可以看到server.error.path,就是设置ErrorController的访问路径,server.error.include-stacktrace
     * 设置是否加入跟踪信息,ErrorProperties里面的初始值是不加入,DefaultErrorAttributes会根据这个属性判断是否加入这个值
     * 页面的话会根据servlet.path + error.path(这个值默认是/error),这两个属性都在ErrorProperties中,可配置
     * @return
     */
    @Bean
    public ErrorAttributes myErrorAttributes() {
        Collection c = Stream.of(new RuntimeException().getStackTrace()).map(StackTraceElement::getMethodName).collect(Collectors.toList());
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
                    boolean includeStackTrace) {
                
                Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();
                
                errorAttributes.put("timestamp", "1999-09-09 24:00:00");
                errorAttributes.put("message", "fuck you");
                errorAttributes.put("error", "exception state");
                errorAttributes.put("status", "illegal");
                
                return errorAttributes;
            }
        };
    }
    
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource datasource() {
        return new DruidDataSource();
    }
    /**
     * 展示数据用的servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        bean.setInitParameters(new HashMap<String, String>() {{
            put("loginUsername", "admin");
            put("loginPassword", "123456");
            put("allow", ""); //默认就是允许所有访问
            put("deny", "192.168.1.103");
        }});
        return bean;
    }
    
    /**
     * 监视数据用的过滤器,只有这个才能获取到所有的请求信息,并采集他们的请求数据和行为,怎么知道哪次请求做了哪些行为,过滤之后的request的行为已经不受掌控了?
     * 或者只监控数据源的行为和访问的行为,不对每次访问使用了数据源的行为进行监控,两个监控是分开的
     * @return
     */
    @Bean
    public FilterRegistrationBean webStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        bean.setInitParameters(new HashMap<String, String>() {{
            put("exclusions", "*.js,*.css,/druid/*");   //不监控哪些请求
        }});
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }
    
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        //设置entite和数据库的字段格式,驼峰自动转下划线
        return c -> c.setMapUnderscoreToCamelCase(true);
    }
    

}
