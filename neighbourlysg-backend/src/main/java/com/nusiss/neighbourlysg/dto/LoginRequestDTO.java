package com.nusiss.neighbourlysg.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
	@NotBlank
	private String email;

	@NotBlank
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String username) {
		this.email = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
