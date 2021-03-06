package com.kite.playground.vkgallery.app;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.kite.playground.vkgallery.manager.VkUserManager;
import com.kite.playground.vkgallery.security.AuthoritiesExtractorImpl;
import com.kite.playground.vkgallery.security.VkAuthorizationCodeAccessTokenProvider;
import com.kite.playground.vkgallery.security.VkPrincipalExtractor;
import com.kite.playground.vkgallery.security.VkUserInfoTokenServices;

@Configuration
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;

    @Autowired
    private VkUserManager vkUserManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                    .antMatchers("/", "/*.js", "/*.css", "/login**", "/webjars/**", "/error**", "/swagger-ui.html",
                                 "/v2/api-docs", "/swagger-resources/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
        .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        .and().logout().logoutSuccessUrl("/").permitAll()
        .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Bean
    public Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter vkFilter = new OAuth2ClientAuthenticationProcessingFilter("/login");
        OAuth2RestTemplate vkTemplate = new OAuth2RestTemplate(vk(), oAuth2ClientContext);
        vkTemplate.setAccessTokenProvider(new AccessTokenProviderChain(Collections.singletonList(vkTokenProvider())));
        vkFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            private RedirectStrategy strategy = new DefaultRedirectStrategy();

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException {
                strategy.sendRedirect(request, response, "/");
            }
        });

        vkFilter.setRestTemplate(vkTemplate);

        VkUserInfoTokenServices tokenServices = new VkUserInfoTokenServices(vkResource().getUserInfoUri(), vk().getClientId());
        tokenServices.setRestTemplate(vkTemplate);
        tokenServices.setTokenType("code");
        tokenServices.setAuthoritiesExtractor(vkAuthoritiesExtractor());
        tokenServices.setPrincipalExtractor(vkPrincipleExtractor());
        vkFilter.setTokenServices(tokenServices);
        return vkFilter;
    }

    @Bean
    public PrincipalExtractor vkPrincipleExtractor() {
        return new VkPrincipalExtractor(vkUserManager);
    }

    @Bean
    public AuthoritiesExtractor vkAuthoritiesExtractor() {
        return new AuthoritiesExtractorImpl();
    }

    @Bean
    public AuthorizationCodeAccessTokenProvider vkTokenProvider() {
        return new VkAuthorizationCodeAccessTokenProvider();
    }

    @Bean
    @ConfigurationProperties("vk.resource")
    public ResourceServerProperties vkResource() {
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("vk.client")
    public AuthorizationCodeResourceDetails vk() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
}
