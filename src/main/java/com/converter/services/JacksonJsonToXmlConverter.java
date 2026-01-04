package com.converter.services;

import com.converter.models.ConversionResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

public class JacksonJsonToXmlConverter implements IConverter {
    
    private final ObjectMapper jsonMapper;
    private final XmlMapper xmlMapper;
    
    public JacksonJsonToXmlConverter() {
        this.jsonMapper = new ObjectMapper();
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
    }
    
    @Override
    public ConversionResult convert(String jsonInput) throws Exception {
        try {
            // Lire le JSON et le convertir en JsonNode
            JsonNode jsonNode = jsonMapper.readTree(jsonInput);
            
            // Convertir JsonNode en XML String
            String xmlOutput = xmlMapper.writerWithDefaultPrettyPrinter()
                                        .writeValueAsString(jsonNode);
            
            return ConversionResult.success(xmlOutput);
            
        } catch (Exception e) {
            return ConversionResult.failure("Erreur Jackson lors de la conversion : " + e.getMessage());
        }
    }
}