package com.converter.services;

import com.converter.models.ConversionResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.nio.charset.StandardCharsets;

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
        // Validation de l'entrée
        if (xmlInput == null || xmlInput.trim().isEmpty()) {
            return ConversionResult.failure("Le XML d'entrée est vide ou null");
        }
        
        try {
            // Extraire le nom de l'élément racine du XML
            String rootName = extractRootElementName(xmlInput);
            
            // Lire le XML et le convertir en JsonNode
            JsonNode jsonNode = xmlMapper.readTree(xmlInput.getBytes(StandardCharsets.UTF_8));
            
            // Créer un nouvel objet JSON avec le nom de la racine
            ObjectNode rootObject = jsonMapper.createObjectNode();
            rootObject.set(rootName, jsonNode);
            
            // Convertir en String JSON formaté
            String jsonOutput = jsonMapper.writeValueAsString(rootObject);
            
            return ConversionResult.success(jsonOutput);
            
        } catch (Exception e) {
            return ConversionResult.failure("Erreur de conversion XML vers JSON : " + e.getMessage());
        }
    }
    
    /**
     * Extrait le nom de l'élément racine du XML
     */
    private String extractRootElementName(String xml) {
        String trimmed = xml.trim();
        
        // Ignorer la déclaration XML si elle existe
        if (trimmed.startsWith("<?xml")) {
            int endDeclaration = trimmed.indexOf("?>");
            if (endDeclaration != -1) {
                trimmed = trimmed.substring(endDeclaration + 2).trim();
            }
        }
        
        // Trouver le premier '<' suivi du nom de l'élément
        int start = trimmed.indexOf('<');
        if (start == -1) {
            return "root"; // Valeur par défaut
        }
        
        start++; // Passer le '<'
        int end = start;
        
        // Lire le nom jusqu'à un espace, '>' ou '/'
        while (end < trimmed.length()) {
            char c = trimmed.charAt(end);
            if (c == ' ' || c == '>' || c == '/') {
                break;
            }
            end++;
        }
        
        String rootName = trimmed.substring(start, end);
        return rootName.isEmpty() ? "root" : rootName;
    }
}