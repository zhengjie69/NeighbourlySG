package com.nusiss.neighbourlysg.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "password")
    private String password;

    @Column(name = "constituency")
    private String constituency;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles = new ArrayList<>();

	@OneToMany(mappedBy = "profile")
	private List<Event> events;

	@OneToMany(mappedBy = "profile")
	private List<EventParticipant> eventParticipants;

	// A profile can have many posts
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts = new ArrayList<>();

	// A profile can have many likes (posts the profile liked)
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Like> likes = new ArrayList<>();

	// A profile can make many comments on posts
	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	// Add helper methods to manage posts, likes, and comments

	public void addPost(Post post) {
		posts.add(post);
		post.setProfile(this);
	}

	public void removePost(Post post) {
		posts.remove(post);
		post.setProfile(null);
	}

	public void addLike(Like like) {
		likes.add(like);
		like.setProfile(this);
	}

	public void removeLike(Like like) {
		likes.remove(like);
		like.setProfile(null);
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.setProfile(this);
	}

	public void removeComment(Comment comment) {
		comments.remove(comment);
		comment.setProfile(null);
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
