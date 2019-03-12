//package com.futao.springbootdemo.foundation.shiro;
//
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author futao
// * Created on 2018-12-18.
// */
//@Configuration
//public class ShiroConfiguration {
//
//
//    /**
//     * Filter工厂，设置对应的过滤条件和跳转条件
//     * anon:所有url都都可以匿名访问
//     * authc: 需要认证才能进行访问
//     * user:配置记住我或认证通过可以访问
//     *
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        //拦截器
//        Map<String, String> map = new HashMap<>(2);
//        //不会被拦截的地址
//        map.put("/swagger-ui.html#", "anon");
//        //登出
//        map.put("/logout", "logout");
//        //对所有用户认证
////        map.put("/**", "authc");
//        //登录
//        shiroFilterFactoryBean.setLoginUrl("/user/mobileLogin");
//        //首页
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        //错误页面，认证不通过跳转
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
//        return shiroFilterFactoryBean;
//    }
//
//    /**
//     * 将自己的验证方式加入容器
//     *
//     * @return
//     */
//    @Bean
//    public ShiroRealmConfiguration myShiroRealm() {
//        return new ShiroRealmConfiguration();
//    }
//
//    /**
//     * 权限管理，配置主要是Realm的管理认证
//     *
//     * @return
//     */
//    @Bean
//    public org.apache.shiro.mgt.SecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(myShiroRealm());
//        return securityManager;
//    }
//
//
//    /**
//     * 加入注解的使用，不加入这个注解不生效
//     *
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//}
