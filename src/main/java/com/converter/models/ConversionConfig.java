/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.converter.models;

/**
 *
 * @author pc
 */
public class ConversionConfig {
    
    private final boolean isXmlToJson;
    private final boolean isNativeMethod;
    
    public ConversionConfig(boolean isXmlToJson, boolean isNativeMethod) {
        this.isXmlToJson = isXmlToJson;
        this.isNativeMethod = isNativeMethod;
    }
    
    public boolean isXmlToJson() {
        return isXmlToJson;
    }
    
    public boolean isNativeMethod() {
        return isNativeMethod;
    }
    
    public String getConversionType() {
        return isXmlToJson ? "XML → JSON" : "JSON → XML";
    }
    
    public String getMethodType() {
        return isNativeMethod ? "Java Native" : "Jackson (API)";
    }
}
