package com.nusiss.neighbourlysg.service;

import com.nusiss.neighbourlysg.dto.SurveyDTO;
import java.util.List;
import java.util.Optional;

public interface SurveyService {

    SurveyDTO createSurvey(SurveyDTO surveyDTO);

    List<SurveyDTO> getAllSurveys();

    Optional<SurveyDTO> getSurveyById(Long id);
}
