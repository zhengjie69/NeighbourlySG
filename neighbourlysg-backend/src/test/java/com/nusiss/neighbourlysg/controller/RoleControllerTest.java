package com.nusiss.neighbourlysg.controller;

import com.nusiss.neighbourlysg.NeighbourlysgBackendApplication;
import com.nusiss.neighbourlysg.dto.RoleDto;
import com.nusiss.neighbourlysg.mapper.RoleMapper;
import com.nusiss.neighbourlysg.service.RoleService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = NeighbourlysgBackendApplication.class)
class RoleControllerTest {
    @Mock
    RoleService roleService;

    @Autowired
    RoleMapper roleMapper;

    private MockMvc mockMvc;

    private RoleController roleController;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        roleController = new RoleController(roleService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(roleController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(TestUtil.createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Test
    void getAllRoles_ShouldReturnOkWhenRolesAreRetrieved() throws Exception {
        // Given
        RoleDto role1 = MasterDTOTestUtil.createRoleDTO(); // Create and populate with test data
        List<RoleDto> roles = List.of(role1);

        when(roleService.getAllRoles()).thenReturn(roles);

        // When & Then
        mockMvc.perform(get("/api/RoleService/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}

