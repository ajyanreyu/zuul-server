package com.shiro.zull.oauth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private Logger log = LoggerFactory.getLogger(ResourceServerConfig.class);


    @Value("${config.security.oauth.client.jwt.key}")
    private String jwtKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
        resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("Configure: " + http);
        http.authorizeRequests()
                .antMatchers("/api/security/oauth/token/**").permitAll() // public route
                .antMatchers(HttpMethod.GET, "/api/product/list", "/api/product/item").permitAll()
                .antMatchers("/api/items/details", "/api/user/user").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/api/product", "/api/item").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/product", "/api/item").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/product", "/api/item").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().cors().configurationSource(corsConfigurationSource());
    }

    /**
     * Defines the configuration of the cors
     *
     * @return CorsConfigurationSource
     * @author Albano Yanes <ajyanreyu@gmail.com>
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    /**
     * Defines a filter for globally defined cors to be applied
     *
     * @return FilterRegistrationBean<CorsFilter>
     * @author Albano Yanes <ajyanreyu@gmail.com>
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilter() {
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;

    }


    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());

    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        log.info(jwtKey);
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        tokenConverter.setSigningKey(jwtKey); // sign the token
        return tokenConverter;
    }
}
