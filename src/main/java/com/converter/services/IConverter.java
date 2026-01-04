package com.converter.services;

import com.converter.models.ConversionResult;

public interface IConverter {
    ConversionResult convert(String input) throws Exception;
}