package com.converter.services;

import com.converter.models.ConversionResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JacksonXmlToJsonConverter implements IConverter {
    
    private final XmlMapper xmlMapper;
    private final ObjectMapper jsonMapper;
    
    public JacksonXmlToJsonConverter() {
        this.xmlMapper = new XmlMapper();
        this.jsonMapper = new ObjectMapper();
        this.jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    @Override
    public ConversionResult convert(String xmlInput) throws Exception {
        try {
            // Lire le XML et le convertir en JsonNode
            JsonNode jsonNode = xmlMapper.readTree(xmlInput.getBytes());
            
            // Convertir JsonNode en String JSON formaté
            String jsonOutput = jsonMapper.writeValueAsString(jsonNode);
            
            return ConversionResult.success(jsonOutput);
            
        } catch (Exception e) {
            return ConversionResult.failure("Erreur Jackson lors de la conversion : " + e.getMessage());
        }
    }
}