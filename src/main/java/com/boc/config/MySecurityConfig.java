package com.boc.config;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdvice;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException { return bean; }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof MethodSecurityInterceptor && beanName.equals("methodSecurityInterceptor")) {
            MethodSecurityInterceptor msi = ((MethodSecurityInterceptor) bean);
            List<AccessDecisionVoter<?>> decisionVoters = ((AffirmativeBased) msi.getAccessDecisionManager()).getDecisionVoters();
            PreInvocationAuthorizationAdviceVoter v = (PreInvocationAuthorizationAdviceVoter) decisionVoters.get(0);

            try {
                Field field = v.getClass().getDeclaredField("preAdvice");
                field.setAccessible(true);
                PreInvocationAuthorizationAdvice pre = (PreInvocationAuthorizationAdvice) field.get(v);

                decisionVoters.set(0, new PreInvocationAuthorizationAdviceVoter(pre) {
                    @Override
                    public int vote(Authentication authentication, MethodInvocation method, Collection<ConfigAttribute> attributes) {
                        return super.vote(authentication, method, attributes);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return bean;
    }

}
