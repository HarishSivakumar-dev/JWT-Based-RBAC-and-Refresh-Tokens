package com.harish.ApplicationSecurityRoleBasedAC;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter
{
	@Autowired
	private JwtUtil jwt;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		String uri=request.getRequestURI();
		if(uri.equals("/app/register") || uri.equals("/app/login") || uri.equals("/app/refresh"))
		{
			System.out.println("skipped filter");
			System.out.println(uri);
			filterChain.doFilter(request, response);
			return;
		}
		else
		{
			String header=request.getHeader("Authorization");
			if(header!=null && header.startsWith("Bearer"))
			{
				String token=header.substring(7);
				if(jwt.validateUser(token))
				{
					List<String> roles=jwt.extractRoles(token);
					List<SimpleGrantedAuthority> authroles=roles.stream()
																.map(role->new SimpleGrantedAuthority(role))
																.collect(Collectors.toList());
					String name=jwt.extractUser(token);
					
					Authentication op=new UsernamePasswordAuthenticationToken(name,null,authroles);
					SecurityContextHolder.getContext().setAuthentication(op);
					
					filterChain.doFilter(request, response);
					return;
					
				}
				else
				{
					throw new BadCredentialsException("Invalid JWT !");
				}
				
			}
			else
			{
				throw new BadCredentialsException("INVALID HEADER !");
			}
		}
			
	}
}
