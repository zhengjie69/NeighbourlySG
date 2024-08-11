package com.nusiss.neighbourlysg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.swagger.v3.oas.models.OpenAPI;

@SpringBootTest
class SwaggerConfigTest {

	@Autowired
    private OpenAPI openAPI;

    @Test
    public void testOpenAPIConfiguration() {
        assertEquals("Swagger API Docs Howtofixthebugs", openAPI.getInfo().getTitle());
        assertEquals("Howtofixthebugs API Description", openAPI.getInfo().getDescription());
        assertEquals("1.0", openAPI.getInfo().getVersion());
    }

}
