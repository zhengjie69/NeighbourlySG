package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.entity.SurveyResponse;

import java.util.List;

public interface SurveyResponseService {
    SurveyResponseDTO saveSurveyResponse(SurveyResponseDTO surveyResponseDto);

    List<SurveyResponseDTO> getSurveyResponses(Long surveyId);

    SurveyResponseDTO getUserResponses(Long surveyId, Long userId);

    void deleteUserResponses(Long surveyId);
}
