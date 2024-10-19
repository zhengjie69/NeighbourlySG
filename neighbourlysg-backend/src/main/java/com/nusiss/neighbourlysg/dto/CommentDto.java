package com.nusiss.neighbourlysg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    private LocalDateTime creationDate;
    private Long postId;
    private Long profileId; // To link back to the Profile
    private String profileName;
}
