package com.harish.ApplicationSecurityRoleBasedAC;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
public class SecurityConfig
{
	
	@Autowired
	private CustomAuthenticationFilter custom;
	@Autowired
	private JwtFilter jwt;
	@Autowired
	private CustomAuthEntryPoint caep;
	@Autowired
	private CustomAccessDeniedHandler cadh;
	
	@Bean
	public SecurityFilterChain secfilter(HttpSecurity http) throws Exception
	{
		return http.csrf(customizer->customizer.disable())
				   .formLogin(form->form.disable())
				   .authorizeHttpRequests(requests->requests.requestMatchers("/app/register","/app/login","/app/refresh").permitAll().anyRequest().authenticated())
				   .authenticationProvider(custom)
				   .exceptionHandling(ex->{
					   			ex.accessDeniedHandler(cadh);
					   			ex.authenticationEntryPoint(caep);
				   })
				   .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				   .addFilterBefore(jwt,UsernamePasswordAuthenticationFilter.class)
				   .build();
				 
	}
	@Bean
	public AuthenticationManager authmanager(HttpSecurity sec) throws Exception
	{
		return sec.getSharedObject(AuthenticationManagerBuilder.class)
				  .authenticationProvider(custom)
				  .build();
	}

}
