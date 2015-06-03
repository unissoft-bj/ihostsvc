/*
 * Copyright 2014 unisoft
 */

package net.wyun.wm.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/**
 * https://crazygui.wordpress.com/2014/08/29/secure-rest-services-using-spring-security/
 * @author Xuecheng
 *
 */

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) 
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		
	@Autowired
	private RESTAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private RESTAuthenticationFailureHandler authenticationFailureHandler;
	@Autowired
	private RESTAuthenticationSuccessHandler authenticationSuccessHandler;
	@Autowired
	private WmsLogoutSuccessHandler wmsLogoutSuccessHandler;
	
	
	@Autowired
	@Qualifier("authenticationProvider")
	AuthenticationProvider authenticationProvider;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().and().logout();
		http.authorizeRequests()
				.antMatchers("/index.html", "/home.html", "/login", "/login.html", 
						     "/index-wms.html", "/ihost", "/mymac", "/wms", "/tpls/**", "/survey",
						     "/", "/reception", "/register")
						     .permitAll()
						     .antMatchers("/secure/lottery/**").hasAuthority("PERM_CREATE_LOTTERY")
						     .antMatchers("/secure//shangwangma").hasAuthority("PERM_CREATE_TOKEN")
						     .anyRequest().authenticated();
		
		http.csrf().disable();
		//http.csrf().csrfTokenRepository(csrfTokenRepository()).and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		
		//http.authorizeRequests().antMatchers("/secure/lottery/**").hasAuthority("PERM_CREATE_LOTTERY");
		
		http.formLogin().successHandler(authenticationSuccessHandler);
		http.formLogin().failureHandler(authenticationFailureHandler);
		http.logout().logoutSuccessHandler(wmsLogoutSuccessHandler);
		
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		
	}

	private Filter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request,
					HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
						.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie == null || token != null
							&& !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}
	
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response
				.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
