package com.harish.ApplicationSecurityRoleBasedAC;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="rolelogs")
public class Role
{
	@Id
	int roleid;
	String rolename;
	public int getRoleid()
	{
		return roleid;
	}
	public void setRoleid(int roleid)
	{
		this.roleid = roleid;
	}
	public String getRolename()
	{
		return rolename;
	}
	public void setRolename(String rolename)
	{
		this.rolename = rolename;
	} 
	

}
