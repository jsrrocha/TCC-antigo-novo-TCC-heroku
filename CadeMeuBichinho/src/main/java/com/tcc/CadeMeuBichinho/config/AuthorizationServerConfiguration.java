package com.tcc.CadeMeuBichinho.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.tcc.CadeMeuBichinho.service.UserDetailsService;



@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends
        AuthorizationServerConfigurerAdapter {

    private static PasswordEncoder encoder;
    
    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    DataSource dataSource;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;


    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.authenticationManager(this.authenticationManager).tokenStore(tokenStore());
        endpoints.userDetailsService(this.userDetailsService);
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	/*  String pass = passwordEncoder.encode("123456");
    	//System.out.println(pass);
    	  clients.jdbc(dataSource)
          .withClient("global")
          .authorizedGrantTypes("password,refresh_token")
          .resourceIds("resources") 
          .scopes("read,write")
          .secret("$2a$10$p9Pk0fQNAQSesI4vuvKA0OZanDD2") 
          .autoApprove(true)
          .accessTokenValiditySeconds(20).and().build(); 
          */ 
    	
       String pass = passwordEncoder.encode("123456");
    	clients.inMemory()
                .withClient("global")   
                .resourceIds("resources")
                .scopes("read,write")
                .secret(pass)
                .authorizedGrantTypes("password")
                .authorizedGrantTypes("refresh_token")
                .accessTokenValiditySeconds(30) //mudar para 1800
                .refreshTokenValiditySeconds(30000)
                .autoApprove(true);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (encoder == null) {
            encoder = new BCryptPasswordEncoder();
        }
        return encoder;
    }
}
