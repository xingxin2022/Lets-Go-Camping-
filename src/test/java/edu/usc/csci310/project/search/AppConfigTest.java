package edu.usc.csci310.project.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AppConfigTest {

    @Test
    public void restTemplateBeanTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        assertNotNull(restTemplate, "RestTemplate bean should not be null");
        context.close();
    }

    @Test
    public void objectMapperBeanTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        assertNotNull(objectMapper, "ObjectMapper bean should not be null");
        context.close();
    }

    @Test
    public void stateCodeMapBeanTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Map<String, String> stateCodeMap = context.getBean("stateCodeMap", Map.class);
        assertNotNull(stateCodeMap, "State code map bean should not be null");
        assertEquals("AL", stateCodeMap.get("alabama"), "Alabama should map to 'AL'");
        assertEquals("CA", stateCodeMap.get("california"), "California should map to 'CA'");
        // You can add more checks here to ensure all states are correctly mapped
        context.close();
    }
}
