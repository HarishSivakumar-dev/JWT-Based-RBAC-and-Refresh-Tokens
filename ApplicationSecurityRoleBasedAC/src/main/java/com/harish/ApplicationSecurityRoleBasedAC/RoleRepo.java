package com.harish.ApplicationSecurityRoleBasedAC;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer>
{
	Optional<Role>findByRolename(String name);
}
