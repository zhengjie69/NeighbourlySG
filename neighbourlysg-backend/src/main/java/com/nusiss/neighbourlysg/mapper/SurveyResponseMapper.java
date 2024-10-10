package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.dto.SurveyResponseDTO;
import com.nusiss.neighbourlysg.entity.Survey;
import com.nusiss.neighbourlysg.entity.SurveyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SurveyResponseMapper {
    SurveyResponseMapper INSTANCE = Mappers.getMapper(SurveyResponseMapper.class);

    @Mapping(source = "survey.id", target = "surveyId")
    SurveyResponseDTO toDto(SurveyResponse surveyResponse);

    SurveyResponse toEntity(SurveyResponseDTO surveyResponseDTO);
}
