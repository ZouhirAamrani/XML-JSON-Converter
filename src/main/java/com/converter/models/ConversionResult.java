package com.converter.models;

public class ConversionResult {
    private final boolean success;
    private final String output;
    private final String errorMessage;
    
    private ConversionResult(boolean success, String output, String errorMessage) {
        this.success = success;
        this.output = output;
        this.errorMessage = errorMessage;
    }
    
    public static ConversionResult success(String output) {
        return new ConversionResult(true, output, null);
    }
    
    public static ConversionResult failure(String errorMessage) {
        return new ConversionResult(false, null, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getOutput() {
        return output;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}