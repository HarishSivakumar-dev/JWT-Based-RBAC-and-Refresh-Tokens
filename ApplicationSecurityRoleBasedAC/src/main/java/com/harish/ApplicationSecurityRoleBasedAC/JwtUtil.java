package com.harish.ApplicationSecurityRoleBasedAC;

import java.awt.event.KeyAdapter;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil
{
	@Autowired 
	private UserDetailsRepo udrp;
	
	String normalkey="HYFYIuiu*9*(*(66(**(*%^%$#%ihiniji&(((&5^^#%#";
	String refreshkey="yigifIFUTFYTYDydtdtretetf&^%$^464665*(&*(&(()&68%#$@$2";
	SecretKey keyforgeneral=Keys.hmacShaKeyFor(normalkey.getBytes());
	SecretKey keyforrefresh=Keys.hmacShaKeyFor(refreshkey.getBytes());
	
	
	@SuppressWarnings("deprecation")
	public String generateJWT(String name)
	{
		Instant it=Instant.now();
		UserDetails urd= udrp.findByName(name).orElseThrow(()->new BadCredentialsException("No User Found !"));
		List<String> roles=urd.getRoles()
							  .stream()
							  .map(role->role.getRolename())
							  .collect(Collectors.toList());
		
		return Jwts.builder()
				   .claim("roles", roles)
				   .setSubject(name)
				   .setIssuedAt(Date.from(it))
				   .setExpiration(Date.from(it.plusSeconds(120)))
				   .signWith(keyforgeneral,SignatureAlgorithm.HS256)
				   .compact();
	}
	@SuppressWarnings("deprecation")
	public String generateRefresh(String name)
	{
		Instant it=Instant.now();
		UserDetails ud=udrp.findByName(name).orElseThrow(()->new BadCredentialsException("No User Name Found !"));
		List<String> roles=ud.getRoles()
							 .stream()
							 .map(role->role.getRolename())
							 .collect(Collectors.toList());
		return Jwts.builder()
				   .setSubject(name)
				   .signWith(keyforrefresh,SignatureAlgorithm.HS256)
				   .setIssuedAt(Date.from(it))
				   .setExpiration(Date.from(it.plusSeconds(604800)))
				   .claim("roles",roles)
				   .compact();

	}
	@SuppressWarnings("deprecation")
	public String extractUser(String token)
	{
		return Jwts.parser().setSigningKey(keyforgeneral).build().parseClaimsJws(token).getBody().getSubject();
	}
	@SuppressWarnings("deprecation")
	public String extractUserRefresh(String token)
	{
		return Jwts.parser().setSigningKey(keyforrefresh).build().parseClaimsJws(token).getBody().getSubject();
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<String> extractRoles(String token)
	{
		return (List<String>) Jwts.parser().setSigningKey(keyforgeneral).build().parseClaimsJws(token).getBody().get("roles");
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<String> extractRolesRefresh(String token)
	{
		return (List<String>) Jwts.parser().setSigningKey(keyforrefresh).build().parseClaimsJws(token).getBody().get("roles");
	}
	@SuppressWarnings("deprecation")
	public boolean validateUser(String token)
	{
		try
		{
			Jwts.parser().setSigningKey(keyforgeneral).build().parseClaimsJws(token);
			return true;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	@SuppressWarnings("deprecation")
	public boolean validateUserRefresh(String token)
	{
		try
		{
			Jwts.parser().setSigningKey(keyforrefresh).build().parseClaimsJws(token);
			System.out.println("inside validate fn !");
			return true;
		}
		catch(Exception e)
		{
			System.out.println("exception");
			throw e;
		}
	}
}
