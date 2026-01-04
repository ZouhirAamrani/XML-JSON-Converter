package com.converter.services;

import com.converter.models.ConversionResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NativeJsonToXmlConverter implements IConverter {
    
    @Override
    public ConversionResult convert(String jsonInput) throws Exception {
        try {
            // Créer un document XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            
            // Parser le JSON manuellement
            String trimmedJson = jsonInput.trim();
            
            // Vérifier que le JSON commence par {
            if (!trimmedJson.startsWith("{")) {
                throw new IllegalArgumentException("Le JSON doit commencer par '{'");
            }
            
            // Créer l'élément racine
            Element root = document.createElement("root");
            document.appendChild(root);
            
            // Parser et convertir
            parseJsonObject(trimmedJson, root, document);
            
            // Convertir le document en String XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            
            return ConversionResult.success(writer.toString());
            
        } catch (Exception e) {
            return ConversionResult.failure("Erreur lors de la conversion : " + e.getMessage());
        }
    }
    
    private void parseJsonObject(String json, Element parent, Document document) {
        // Retirer les accolades extérieures
        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }
        json = json.trim();
        
        if (json.isEmpty()) {
            return;
        }
        
        // Parser les paires clé-valeur
        int braceCount = 0;
        int bracketCount = 0;
        boolean inString = false;
        boolean escape = false;
        StringBuilder currentPair = new StringBuilder();
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (escape) {
                currentPair.append(c);
                escape = false;
                continue;
            }
            
            if (c == '\\') {
                escape = true;
                currentPair.append(c);
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                currentPair.append(c);
                continue;
            }
            
            if (!inString) {
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                else if (c == '[') bracketCount++;
                else if (c == ']') bracketCount--;
                
                if (c == ',' && braceCount == 0 && bracketCount == 0) {
                    // Fin d'une paire clé-valeur
                    processKeyValuePair(currentPair.toString().trim(), parent, document);
                    currentPair = new StringBuilder();
                    continue;
                }
            }
            
            currentPair.append(c);
        }
        
        // Traiter la dernière paire
        if (currentPair.length() > 0) {
            processKeyValuePair(currentPair.toString().trim(), parent, document);
        }
    }
    
    private void processKeyValuePair(String pair, Element parent, Document document) {
        // Trouver le séparateur ':'
        int colonIndex = findColonSeparator(pair);
        
        if (colonIndex == -1) {
            return;
        }
        
        String key = pair.substring(0, colonIndex).trim();
        String value = pair.substring(colonIndex + 1).trim();
        
        // Retirer les guillemets de la clé
        key = key.replaceAll("^\"|\"$", "");
        
        // Ignorer les attributs (commençant par @)
        if (key.startsWith("@")) {
            String attrName = key.substring(1);
            String attrValue = value.replaceAll("^\"|\"$", "");
            parent.setAttribute(attrName, unescapeJson(attrValue));
            return;
        }
        
        // Ignorer #text (contenu texte direct)
        if (key.equals("#text")) {
            String textValue = value.replaceAll("^\"|\"$", "");
            parent.setTextContent(unescapeJson(textValue));
            return;
        }
        
        // Créer l'élément
        Element element = document.createElement(key);
        
        // Déterminer le type de valeur
        if (value.startsWith("{")) {
            // Objet JSON
            parseJsonObject(value, element, document);
        } else if (value.startsWith("[")) {
            // Tableau JSON
            parseJsonArray(value, key, parent, document);
            return; // Ne pas ajouter l'élément car on a déjà ajouté les éléments du tableau
        } else if (value.equals("null")) {
            // Valeur nulle
            element.setTextContent("");
        } else {
            // Valeur simple (string, number, boolean)
            String textValue = value.replaceAll("^\"|\"$", "");
            element.setTextContent(unescapeJson(textValue));
        }
        
        parent.appendChild(element);
    }
    
    private void parseJsonArray(String array, String elementName, Element parent, Document document) {
        // Retirer les crochets
        array = array.trim();
        if (array.startsWith("[")) {
            array = array.substring(1);
        }
        if (array.endsWith("]")) {
            array = array.substring(0, array.length() - 1);
        }
        array = array.trim();
        
        if (array.isEmpty()) {
            return;
        }
        
        // Parser les éléments du tableau
        int braceCount = 0;
        int bracketCount = 0;
        boolean inString = false;
        boolean escape = false;
        StringBuilder currentElement = new StringBuilder();
        
        for (int i = 0; i < array.length(); i++) {
            char c = array.charAt(i);
            
            if (escape) {
                currentElement.append(c);
                escape = false;
                continue;
            }
            
            if (c == '\\') {
                escape = true;
                currentElement.append(c);
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                currentElement.append(c);
                continue;
            }
            
            if (!inString) {
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                else if (c == '[') bracketCount++;
                else if (c == ']') bracketCount--;
                
                if (c == ',' && braceCount == 0 && bracketCount == 0) {
                    // Fin d'un élément du tableau
                    processArrayElement(currentElement.toString().trim(), elementName, parent, document);
                    currentElement = new StringBuilder();
                    continue;
                }
            }
            
            currentElement.append(c);
        }
        
        // Traiter le dernier élément
        if (currentElement.length() > 0) {
            processArrayElement(currentElement.toString().trim(), elementName, parent, document);
        }
    }
    
    private void processArrayElement(String elementValue, String elementName, Element parent, Document document) {
        Element element = document.createElement(elementName);
        
        if (elementValue.startsWith("{")) {
            // Objet JSON
            parseJsonObject(elementValue, element, document);
        } else {
            // Valeur simple
            String textValue = elementValue.replaceAll("^\"|\"$", "");
            element.setTextContent(unescapeJson(textValue));
        }
        
        parent.appendChild(element);
    }
    
    private int findColonSeparator(String pair) {
        boolean inString = false;
        boolean escape = false;
        
        for (int i = 0; i < pair.length(); i++) {
            char c = pair.charAt(i);
            
            if (escape) {
                escape = false;
                continue;
            }
            
            if (c == '\\') {
                escape = true;
                continue;
            }
            
            if (c == '"') {
                inString = !inString;
                continue;
            }
            
            if (!inString && c == ':') {
                return i;
            }
        }
        
        return -1;
    }
    
    private String unescapeJson(String text) {
        return text.replace("\\\"", "\"")
                   .replace("\\\\", "\\")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }
}