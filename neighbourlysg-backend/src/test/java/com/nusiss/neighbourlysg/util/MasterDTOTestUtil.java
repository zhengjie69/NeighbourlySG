package com.nusiss.neighbourlysg.util;

import com.nusiss.neighbourlysg.dto.EventDto;
import com.nusiss.neighbourlysg.dto.LoginRequestDTO;
import com.nusiss.neighbourlysg.dto.ProfileDto;
import com.nusiss.neighbourlysg.dto.QuestionDTO;
import com.nusiss.neighbourlysg.dto.RoleDto;
import com.nusiss.neighbourlysg.dto.SurveyDTO;

import java.time.LocalDate;
import java.util.Arrays;

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
        profile.setRoles(Arrays.asList(1));
        profile.setName("name");
        profile.setPassword("password");
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

}
