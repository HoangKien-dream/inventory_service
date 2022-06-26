package com.example.productservice.configAuth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

//    private final UserDetailsService userDetailsService;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //override default login path
        com.example.productservice.configAuth.ApiAuthenticationFilter apiAuthenticationFilter =
                new com.example.productservice.configAuth.ApiAuthenticationFilter(authenticationManagerBean());
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.authorizeRequests().antMatchers("/api/v1/register**", "/api/v1/login**", "/api/v1/token/refresh**").permitAll();
//        http.authorizeRequests().antMatchers("/api/v1/product/**").hasAnyAuthority("User");
        //add requests path for more role here
//        http.authorizeRequests().antMatchers("/api/v1/order/**").hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers("/api/add-role/**").hasAnyAuthority("admin");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(apiAuthenticationFilter);
        http.addFilterBefore(new com.example.productservice.configAuth.ApiAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH",
                "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type",
                "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
