package com.nusiss.neighbourlysg.dto;

import java.util.Map;

public class SurveyResponseDTO {
    private Long surveyId;
    private Map<Long, String> responses; // questionId -> response

    // Getters and Setters
    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Map<Long, String> getResponses() {
        return responses;
    }

    public void setResponses(Map<Long, String> responses) {
        this.responses = responses;
    }
}
