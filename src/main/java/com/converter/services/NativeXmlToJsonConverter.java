package com.converter.services;

import com.converter.models.ConversionResult;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class NativeXmlToJsonConverter implements IConverter {
    
    @Override
    public ConversionResult convert(String xmlInput) throws Exception {
        try {
            // Parser le XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlInput)));
            
            // Normaliser le document
            document.getDocumentElement().normalize();
            
            // Convertir en JSON
            StringBuilder json = new StringBuilder();
            Element root = document.getDocumentElement();
            
            json.append("{\n");
            elementToJson(root, json, 1);
            json.append("\n}");
            
            return ConversionResult.success(json.toString());
            
        } catch (Exception e) {
            return ConversionResult.failure("Erreur lors de la conversion : " + e.getMessage());
        }
    }
    
    private void elementToJson(Element element, StringBuilder json, int indent) {
        String elementName = element.getNodeName();
        String indentation = getIndentation(indent);
        
        json.append(indentation).append("\"").append(elementName).append("\": ");
        
        // Vérifier si l'élément a des attributs
        NamedNodeMap attributes = element.getAttributes();
        boolean hasAttributes = attributes.getLength() > 0;
        
        // Vérifier si l'élément a des enfants
        NodeList children = element.getChildNodes();
        boolean hasElementChildren = false;
        boolean hasTextContent = false;
        String textContent = "";
        
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                hasElementChildren = true;
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getNodeValue().trim();
                if (!text.isEmpty()) {
                    hasTextContent = true;
                    textContent = text;
                }
            }
        }
        
        // Cas 1: Élément avec enfants ou attributs
        if (hasElementChildren || hasAttributes) {
            json.append("{\n");
            
            // Ajouter les attributs
            if (hasAttributes) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    Node attr = attributes.item(i);
                    json.append(getIndentation(indent + 1))
                        .append("\"@").append(attr.getNodeName()).append("\": ")
                        .append("\"").append(escapeJson(attr.getNodeValue())).append("\"");
                    
                    if (i < attributes.getLength() - 1 || hasTextContent || hasElementChildren) {
                        json.append(",");
                    }
                    json.append("\n");
                }
            }
            
            // Ajouter le contenu texte si présent
            if (hasTextContent && !hasElementChildren) {
                json.append(getIndentation(indent + 1))
                    .append("\"#text\": \"").append(escapeJson(textContent)).append("\"");
                json.append("\n");
            }
            
            // Ajouter les éléments enfants
            if (hasElementChildren) {
                // Grouper les enfants par nom
                java.util.Map<String, java.util.List<Element>> childrenByName = new java.util.LinkedHashMap<>();
                
                for (int i = 0; i < children.getLength(); i++) {
                    Node child = children.item(i);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        Element childElement = (Element) child;
                        String childName = childElement.getNodeName();
                        
                        if (!childrenByName.containsKey(childName)) {
                            childrenByName.put(childName, new java.util.ArrayList<>());
                        }
                        childrenByName.get(childName).add(childElement);
                    }
                }
                
                int childGroupIndex = 0;
                for (java.util.Map.Entry<String, java.util.List<Element>> entry : childrenByName.entrySet()) {
                    java.util.List<Element> elements = entry.getValue();
                    
                    if (elements.size() == 1) {
                        // Un seul élément avec ce nom
                        elementToJson(elements.get(0), json, indent + 1);
                    } else {
                        // Plusieurs éléments avec le même nom -> tableau
                        json.append(getIndentation(indent + 1))
                            .append("\"").append(entry.getKey()).append("\": [\n");
                        
                        for (int i = 0; i < elements.size(); i++) {
                            Element child = elements.get(i);
                            json.append(getIndentation(indent + 2)).append("{\n");
                            
                            // Traiter le contenu de l'élément
                            NodeList grandChildren = child.getChildNodes();
                            boolean hasGrandChildren = false;
                            String childText = "";
                            
                            for (int j = 0; j < grandChildren.getLength(); j++) {
                                Node grandChild = grandChildren.item(j);
                                if (grandChild.getNodeType() == Node.ELEMENT_NODE) {
                                    hasGrandChildren = true;
                                } else if (grandChild.getNodeType() == Node.TEXT_NODE) {
                                    String text = grandChild.getNodeValue().trim();
                                    if (!text.isEmpty()) {
                                        childText = text;
                                    }
                                }
                            }
                            
                            if (hasGrandChildren) {
                                // Traiter les petits-enfants
                                processChildElements(child, json, indent + 3);
                            } else if (!childText.isEmpty()) {
                                json.append(getIndentation(indent + 3))
                                    .append("\"#text\": \"").append(escapeJson(childText)).append("\"\n");
                            }
                            
                            json.append(getIndentation(indent + 2)).append("}");
                            
                            if (i < elements.size() - 1) {
                                json.append(",");
                            }
                            json.append("\n");
                        }
                        
                        json.append(getIndentation(indent + 1)).append("]");
                    }
                    
                    if (childGroupIndex < childrenByName.size() - 1) {
                        json.append(",");
                    }
                    json.append("\n");
                    childGroupIndex++;
                }
            }
            
            json.append(getIndentation(indent)).append("}");
            
        } else if (hasTextContent) {
            // Cas 2: Élément avec seulement du texte
            json.append("\"").append(escapeJson(textContent)).append("\"");
        } else {
            // Cas 3: Élément vide
            json.append("null");
        }
    }
    
    private void processChildElements(Element parent, StringBuilder json, int indent) {
        NodeList children = parent.getChildNodes();
        java.util.Map<String, java.util.List<Element>> childrenByName = new java.util.LinkedHashMap<>();
        
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) child;
                String childName = childElement.getNodeName();
                
                if (!childrenByName.containsKey(childName)) {
                    childrenByName.put(childName, new java.util.ArrayList<>());
                }
                childrenByName.get(childName).add(childElement);
            }
        }
        
        int index = 0;
        for (java.util.Map.Entry<String, java.util.List<Element>> entry : childrenByName.entrySet()) {
            java.util.List<Element> elements = entry.getValue();
            
            if (elements.size() == 1) {
                elementToJson(elements.get(0), json, indent);
            } else {
                json.append(getIndentation(indent))
                    .append("\"").append(entry.getKey()).append("\": [\n");
                
                for (int i = 0; i < elements.size(); i++) {
                    Element child = elements.get(i);
                    String childText = child.getTextContent().trim();
                    
                    json.append(getIndentation(indent + 1))
                        .append("\"").append(escapeJson(childText)).append("\"");
                    
                    if (i < elements.size() - 1) {
                        json.append(",");
                    }
                    json.append("\n");
                }
                
                json.append(getIndentation(indent)).append("]");
            }
            
            if (index < childrenByName.size() - 1) {
                json.append(",");
            }
            json.append("\n");
            index++;
        }
    }
    
    private String getIndentation(int level) {
        return "  ".repeat(level);
    }
    
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}