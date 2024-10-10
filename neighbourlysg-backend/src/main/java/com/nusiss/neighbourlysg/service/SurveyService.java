package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;

import java.util.List;
import java.util.Optional;

public interface SurveyService {

    SurveyDTO createSurvey(SurveyDTO surveyDTO);

    List<SurveyDTO> getAllSurveys();

    Optional<SurveyDTO> getSurveyById(Long id);

    SurveyDTO updateSurvey(SurveyDTO surveyDTO);

    void deleteSurveyById(Long surveyId);

    void saveSurveyResponse(SurveyResponseDTO response);

    List<SurveyResponseDTO> getSurveyResponses(Long surveyId);
}
