package com.nusiss.neighbourlysg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private Long profileId; // To link back to the Profile
    private String profileName; // New field to store the user's name
    private int likeCount;   // To store the number of likes
    private List<CommentDto> comments; // List of comments associated with the post
    private List<String> tags;  // To store the tags for the post

}