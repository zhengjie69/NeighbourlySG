package com.nusiss.neighbourlysg.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile",
		uniqueConstraints = {
		@UniqueConstraint(columnNames = "email")
})
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

	@Column(name = "username", unique = true)
	private String username;

    @Column(name = "email", unique = true)
	@Email
    private String email;
    
    @Column(name = "password")
    private String password;

    @Column(name = "constituency")
    private String constituency;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "profile")
	private List<Event> events;

	@OneToMany(mappedBy = "profile")
	private List<EventParticipant> eventParticipants;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public @NotBlank @Size(max = 20) String getUsername() {
		return username;
	}

	public void setUsername(@NotBlank @Size(max = 20) String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getConstituency() {
		return constituency;
	}


	public void setConstituency(String constituency) {
		this.constituency = constituency;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}
