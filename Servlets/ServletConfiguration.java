package com.company.base.accenture.movies.Servlets;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfiguration {
//    @Bean
//    public ServletRegistrationBean registrationServlet() {
//        ServletRegistrationBean bean = new ServletRegistrationBean(new RegistationServlet(), "/user/register");
//        bean.setLoadOnStartup(1);
//        return bean;
//    }
//
//    @Bean
//    public ServletRegistrationBean loginServlet() {
//        ServletRegistrationBean bean = new ServletRegistrationBean(new LoginServlet(), "/user/login");
//        bean.setLoadOnStartup(1);
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean securityServletFilter() {
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new SecurityServletFilter());
//        bean.addUrlPatterns("/secured/*");
//        return bean;
//    }
}
