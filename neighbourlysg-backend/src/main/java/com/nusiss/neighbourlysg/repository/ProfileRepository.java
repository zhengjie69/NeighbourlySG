package com.nusiss.neighbourlysg.repository;

import com.nusiss.neighbourlysg.entity.Profile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
	
	Optional<Profile> findByEmail(String email);

}
