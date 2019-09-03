package com.shiro.zull.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
        resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/api/security/oauth/**")
                .permitAll() // public route
                .antMatchers(HttpMethod.GET, "/api/product/list", "api/product/item")
                .permitAll();
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());

    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey("shiro_secret_code"); // sign the token
        return tokenConverter;
    }
}
