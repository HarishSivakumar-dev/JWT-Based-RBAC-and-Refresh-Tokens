package com.harish.ApplicationSecurityRoleBasedAC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFilter implements AuthenticationProvider
{
	@Autowired 
	private UserDetailsRepo udr;
	
	BCryptPasswordEncoder bpe=new BCryptPasswordEncoder(15);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		String name=authentication.getName();
		String pass=authentication.getCredentials().toString();
		
		UserDetails rd=udr.findByName(name).orElseThrow(()-> new BadCredentialsException("NO NAME FOUND !"));
		String encoded=rd.getPassword();
		if(bpe.matches(pass, encoded))
		{
			return new UsernamePasswordAuthenticationToken(name,pass);
			
		}
		else
		{
			throw new BadCredentialsException("Password Mismatch !");
		}
		
	}

	@Override
	public boolean supports(Class<?> authentication)
	{
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
