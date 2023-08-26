package com.ebac.modulo45.configuration;

import com.ebac.modulo45.component.AuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/pagina-login").setViewName("pagina-login");
	}

	@Bean
	public FilterRegistrationBean<AuthorizationFilter> authorizationFilterRegistrationBean() {
		FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
		AuthorizationFilter authorizationFilter = new AuthorizationFilter();

		registrationBean.setName("authorization");
		registrationBean.setFilter(authorizationFilter);
		registrationBean.setOrder(1);

		return registrationBean;
	}
}
