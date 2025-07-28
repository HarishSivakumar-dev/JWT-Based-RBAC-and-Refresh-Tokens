package com.harish.ApplicationSecurityRoleBasedAC;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@Component
public class EndpointControllers
{
	@Autowired
	private RoleRepo rr;
	
	@Autowired 
	private UserDetailsRepo ur;
	
	@Autowired 
	private AuthenticationManager am;
	
	@Autowired
	private JwtUtil jwt;
	
	BCryptPasswordEncoder bpe=new BCryptPasswordEncoder(15);
	
	@PostMapping("/register")
	public String registerEndpoint(@RequestBody UserDetails ud)
	{
		String role="ROLE_USER";
		String pass=ud.getPassword();
		ud.setPassword(bpe.encode(pass));
		Role roles=rr.findByRolename(role).orElseThrow(()->new BadCredentialsException("NO SUCH USER FOUND !"));
		ud.getRoles().add(roles);
		System.out.println(ud.getRoles());
		
		ur.save(ud);
		
		return "User Registered successfully !";
	}
	@PostMapping("/login")
	public String loginEndpoint(@RequestBody UserDetails ud)
	{
		String name=ud.getName();
		String pass=ud.getPassword();
		
		am.authenticate(new UsernamePasswordAuthenticationToken(name,pass));
		
		String access=jwt.generateJWT(name);
		String refresh=jwt.generateRefresh(name);
		
		return "Access Token\n"+access+"Refresh Token\n"+refresh;	
	}
	@PostMapping("/refresh")
	public String refreshEndpoint(@RequestBody RefreshEndpointDTO dto)
	{
		String token=dto.getToken();
		String name=jwt.extractUserRefresh(token);
		System.out.println(name);
		
		if(jwt.validateUserRefresh(token))
		{
			String access=jwt.generateJWT(name);
			return access;
		}
		else
		{
			throw new BadCredentialsException("Token Expired !");
		}
	}
	@GetMapping("/contactus")
	public String contactusEndpoint()
	{
		return "To contact us ! please email us to harishss.2k07@gmail.com or call 9092418002";
		
	}
	@GetMapping("/getusers")
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserDetails> getUsers()
	{
		return ur.findAll();
	}
	@PostMapping("/updaterole")
	@PreAuthorize("hasRole('ADMIN')")
	public String updateuserroleEndpoint(@RequestBody DTOForPromotion dpo)
	{
		int id=dpo.getId();
		UserDetails user=ur.findById(id).orElseThrow(()->new BadCredentialsException("NO USER FOUND !"));
		String role="ROLE_ADMIN";
		Role r=rr.findByRolename(role).orElseThrow(()->new BadCredentialsException("NO ROLE FOUND !"));
		user.getRoles().add(r);
		ur.save(user);
		return "USER UPDATED SUCCESSFULLY !";
	}
}
