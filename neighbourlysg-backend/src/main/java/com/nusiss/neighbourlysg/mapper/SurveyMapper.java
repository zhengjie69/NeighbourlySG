package com.nusiss.neighbourlysg.mapper;

import com.nusiss.neighbourlysg.dto.QuestionDTO;
import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.entity.Question;
import com.nusiss.neighbourlysg.entity.Survey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SurveyMapper {
    SurveyMapper INSTANCE = Mappers.getMapper(SurveyMapper.class);

    SurveyDTO toDto(Survey survey);

    Survey toEntity(SurveyDTO surveyDTO);
}
