package com.nusiss.neighbourlysg.util;

import com.nusiss.neighbourlysg.dto.QuestionDTO;
import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.entity.Profile;
import com.nusiss.neighbourlysg.entity.Question;
import com.nusiss.neighbourlysg.entity.Role;
import com.nusiss.neighbourlysg.entity.Survey;

import java.util.Arrays;

public final class MasterEntityTestUtil {

    public static Profile createProfileEntity(){
        Profile profile = new Profile();
        profile.setConstituency("con");
        profile.setEmail("email");
        profile.setId(1L);
        profile.setRoles(Arrays.asList(createRoleEntity()));
        profile.setName("name");
        profile.setPassword("password");
        return profile;
    }

    public static Role createRoleEntity(){
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        return role;
    }

    public static Survey createSurvey() {
        Survey survey = new Survey();
        survey.setDescription("desc");
        survey.setId(1L);
        survey.setTitle("title");
        survey.setQuestions(Arrays.asList(createQuestion()));
        return survey;
    }

    public static Question createQuestion() {
        Question question = new Question();
        question.setId(1L);
        question.setQuestionText("text");
        question.setQuestionType("type");
        question.setOptions(Arrays.asList("option"));
        return question;
    }
}
