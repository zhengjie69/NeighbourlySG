package com.nusiss.neighbourlysg.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    // Many posts can be created by one profile
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    // Tracks how many likes the post has
    @Column(name = "likes_count", nullable = false)
    private int likesCount = 0; // Initialize to zero

    // One post can have many likes (mapped by the 'post' field in the Like entity)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    // One post can have many comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "tags")
    private List<String> tags = new ArrayList<>();


    // Add helper methods to manage the list of likes and comments

    public void addLike(Like like) {
        likes.add(like);
        like.setPost(this);
        likesCount++; // Increment the like count
    }

    public void removeLike(Like like) {
        likes.remove(like);
        like.setPost(null);
        likesCount--; // Decrement the like count
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

}