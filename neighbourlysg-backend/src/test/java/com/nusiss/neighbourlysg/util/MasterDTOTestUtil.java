package com.nusiss.neighbourlysg.util;

import com.nusiss.neighbourlysg.common.RoleConstants;
import com.nusiss.neighbourlysg.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MasterDTOTestUtil {

    public static LoginRequestDTO createLoginRequestDTO(){
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("email");
        loginRequestDTO.setPassword("password");
        return loginRequestDTO;
    }

    public static ProfileDto createProfileDTO() {
        ProfileDto profile = new ProfileDto();
        profile.setConstituency("con");
        profile.setEmail("email");
        profile.setId(1L);
        profile.setName("name");
        profile.setPassword("password");
        return profile;
    }

    public static ProfileDto createProfileDTOWithRoles() {
        ProfileDto profile = new ProfileDto();
        profile.setConstituency("con");
        profile.setEmail("email");
        profile.setId(1L);
        profile.setName("name");
        profile.setPassword("password");
        Set<String> roles = new HashSet<>();
        roles.add(RoleConstants.ROLE_USER);
        roles.add(RoleConstants.ROLE_ORGANISER);
        roles.add(RoleConstants.ROLE_ADMIN);
        profile.setRoles(roles);
        return profile;
    }

    public static RoleDto createRoleDTO() {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(1);
        roleDto.setName("USER");
        return roleDto;
    }

    public static EventDto createEventDTO(){

        EventDto eventDto = new EventDto();
        eventDto.setRsvpCount(1L);
        eventDto.setLocation("testLocation");
        eventDto.setDescription("testDescription");
        eventDto.setTitle("testTitle");
        eventDto.setStartTime("testStartTime");
        eventDto.setEndTime("testEndTime");
        eventDto.setDate(LocalDate.now());

        return eventDto;
    }
    public static SurveyDTO createSurveyDTO() {
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setDescription("desc");
        surveyDTO.setId(1L);
        surveyDTO.setTitle("title");
        surveyDTO.setQuestions(Arrays.asList(createQuestionDTO()));
        return surveyDTO;
    }

    public static QuestionDTO createQuestionDTO() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setQuestionText("text");
        questionDTO.setQuestionType("type");
        questionDTO.setOptions(Arrays.asList("option"));
        return questionDTO;
    }

    public static PostDto createPostDTO() {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setContent("Content");
        postDto.setProfileId(1L);
        postDto.setLikeCount(0);
        postDto.setCreationDate(LocalDateTime.now()); // Set a default creation date

        // Create and set comments
        List<CommentDto> comments = new ArrayList<>();
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setContent("This is a comment");
        commentDto.setProfileId(1L);
        commentDto.setCreationDate(LocalDateTime.now());
        comments.add(commentDto);
        postDto.setComments(comments);

        // Create and set tags
        List<String> tags = new ArrayList<>();
        tags.add("Tag1");
        tags.add("Tag2");
        postDto.setTags(tags);

        return postDto;
    }

}
