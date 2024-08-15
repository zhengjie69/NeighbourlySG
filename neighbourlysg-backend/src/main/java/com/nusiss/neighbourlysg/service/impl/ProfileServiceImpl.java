package com.nusiss.neighbourlysg.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.exception.EmailInUseException;
import com.nusiss.neighbourlysg.exception.PasswordWrongException;
import com.nusiss.neighbourlysg.exception.UserNotExistedException;
import com.nusiss.neighbourlysg.mapper.ProfileMapper;
import com.nusiss.neighbourlysg.repository.ProfileRepository;
import com.nusiss.neighbourlysg.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    
    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper) {
    	this.profileRepository=profileRepository;
    	this.profileMapper=profileMapper;
    	
    }
    
    @Override
    public ProfileDto createProfile(ProfileDto profileDto) {
    	// Check if user with email already exists, since email is unique
        if (profileRepository.findByEmail(profileDto.getEmail()).isPresent()) {
            throw new EmailInUseException();
        }

        // Encrypt the password 
        //String encodedPassword = passwordEncoder.encode(password);
	
        Profile profile = profileMapper.toEntity(profileDto);
        Profile savedProfile = profileRepository.save(profile);

        return profileMapper.toDto(savedProfile);
    }
    
    @Override
    public ProfileDto login(LoginRequestDTO loginRequestDTO) {
    	// Check if user with email, check password
    	
    	Optional<Profile> profileOp = profileRepository.findByEmail(loginRequestDTO.getEmail());
    	
        if (profileOp.isPresent()) {
            //check password
        	if(!StringUtils.equals(profileOp.get().getPassword(), loginRequestDTO.getPassword())) {
        		 throw new PasswordWrongException();
        	}
        }else {
        	throw new UserNotExistedException();
        } 
        
        return profileMapper.toDto(profileOp.get());

        
    }

}