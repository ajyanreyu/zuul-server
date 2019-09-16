package com.shiro.zull.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private Environment env;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
        resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("api/security/oauth/token/**").permitAll() // public route
                .antMatchers(HttpMethod.GET, "/api/product/list", "api/product/item").permitAll()
                .antMatchers("/api/items/details", "/api/user/user").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/product", "/api/item").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/product", "/api/item").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/product", "/api/item").hasRole("ADMIN")
                .anyRequest().authenticated();
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());

    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(env.getProperty("config.security.oauth.client.client.jwt.key")); // sign the token
        return tokenConverter;
    }
}
