package com.harish.ApplicationSecurityRoleBasedAC;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails,Integer>
{
	Optional<UserDetails> findByName(String name);

}
