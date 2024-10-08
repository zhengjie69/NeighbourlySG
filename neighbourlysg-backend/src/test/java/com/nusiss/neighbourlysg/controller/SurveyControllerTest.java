package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;

import com.nusiss.neighbourlysg.dto.SurveyDTO;
import com.nusiss.neighbourlysg.service.SurveyService;
import com.nusiss.neighbourlysg.util.MasterDTOTestUtil;
import com.nusiss.neighbourlysg.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class SurveyControllerTest {
    @Mock
    SurveyService surveyService;

    private MockMvc mockMvc;

    private SurveyController surveyController;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        surveyController = new SurveyController(surveyService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(surveyController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void createSurveyTest() throws Exception {
        SurveyDTO surveyDTO=MasterDTOTestUtil.createSurveyDTO();

        when(surveyService.createSurvey(any())).thenReturn(surveyDTO);

        byte[] data = TestUtil.convertObjectToJsonBytes(surveyDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + "/SurveyService/createSurvey")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8).content(data)).andExpect(status().isOk());
    }

    @Test
    void getAllSurveys_shouldReturnListOfSurveyDTOs() throws Exception {
        // Arrange
        SurveyDTO surveyDTO=MasterDTOTestUtil.createSurveyDTO();

        List<SurveyDTO> surveyDTOs = new ArrayList<>();
        surveyDTOs.add(surveyDTO);

        when(surveyService.getAllSurveys()).thenReturn(surveyDTOs);

        // Act and Assert
        mockMvc.perform(get("/api/SurveyService/getAllSurveys")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists()) // Customize this based on the properties of SurveyDTO
                .andExpect(jsonPath("$[0].id").value(surveyDTO.getId())); // Example property check
    }


    @Test
    void getSurveyTest() throws Exception {
        SurveyDTO surveyDTO=MasterDTOTestUtil.createSurveyDTO();

        when(surveyService.getSurveyById(any())).thenReturn(Optional.of(surveyDTO));

        byte[] objectToJson = TestUtil.convertObjectToJsonBytes(surveyDTO);

        mockMvc .perform(get("/api/" + "/SurveyService/survey/{id}", surveyDTO.getId()).contentType(TestUtil.APPLICATION_JSON_UTF8).content(objectToJson))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(surveyDTO.getId()))
                .andExpect(jsonPath("$.description").value(surveyDTO.getDescription()))
                .andExpect(jsonPath("$.title").value(surveyDTO.getTitle()));
    }
}
