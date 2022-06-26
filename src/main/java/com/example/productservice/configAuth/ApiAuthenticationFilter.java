
package com.example.productservice.configAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * this filter is call when user want to login to the system
 * the default login path is "/login" but it can be override, see override login path in ApiSecurityConfig
 */
public class ApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //we need this class to authenticate user when they login successfully
    private final AuthenticationManager authenticationManager;


    public ApiAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    //this function is call first when user try to login with their user name and password
    //so here we get username and password from request body then let spring do the magic


    //when username and password is correct this function will be call and pass in current login success information
    //so here we will return token for user
}
