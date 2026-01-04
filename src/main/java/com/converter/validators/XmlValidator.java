package com.converter.validators;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class XmlValidator {
    
    public boolean validate(String xmlContent) throws Exception {
        if (xmlContent == null || xmlContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu XML est vide");
        }
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // Parser le XML
            Document document = builder.parse(
                new InputSource(new StringReader(xmlContent))
            );
            
            // Si on arrive ici, le XML est bien formé
            return document != null;
            
        } catch (SAXException e) {
            throw new Exception("XML mal formé : " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de validation XML : " + e.getMessage());
        }
    }
}