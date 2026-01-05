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
            JsonNode jsonNode = jsonMapper.readTree(jsonInput);
            String xmlOutput;

            // Le JSON a-t-il une seule propriété racine ?
            if (jsonNode.isObject() && jsonNode.size() == 1) {
                
                // 1. Récupérer le nom de la clé 
                String rootName = jsonNode.fieldNames().next();
                
                // 2. Récupérer le contenu de l'enfant
                JsonNode childNode = jsonNode.get(rootName);
                
                
                xmlOutput = xmlMapper.writerWithDefaultPrettyPrinter()
                                     .withRootName(rootName) 
                                     .writeValueAsString(childNode);
            } else {
                xmlOutput = xmlMapper.writerWithDefaultPrettyPrinter()
                                     .withRootName("root")
                                     .writeValueAsString(jsonNode);
            }
            
            return ConversionResult.success(xmlOutput);
            
        } catch (Exception e) {
            return ConversionResult.failure("Erreur conversion : " + e.getMessage());
        }
    }
}