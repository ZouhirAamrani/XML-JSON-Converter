package com.converter.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonValidator {
    
    private final ObjectMapper objectMapper;
    
    public JsonValidator() {
        this.objectMapper = new ObjectMapper();
    }
    
    public boolean validate(String jsonContent) throws Exception {
        if (jsonContent == null || jsonContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu JSON est vide");
        }
        
        try {
            // Parser le JSON
            JsonNode jsonNode = objectMapper.readTree(jsonContent);
            
            // Si on arrive ici, le JSON est valide
            return jsonNode != null;
            
        } catch (Exception e) {
            throw new Exception("JSON mal formé : " + e.getMessage());
        }
    }
}