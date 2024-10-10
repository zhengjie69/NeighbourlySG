package com.nusiss.neighbourlysg.dto;

import java.util.List;
import java.util.Map;

public class SurveyResponseDTO {
    private Long id;
    private Long surveyId;
    private List<QuestionResponseDTO> responses;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public List<QuestionResponseDTO> getResponses() {
        return responses;
    }

    public void setResponses(List<QuestionResponseDTO> responses) {
        this.responses = responses;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
