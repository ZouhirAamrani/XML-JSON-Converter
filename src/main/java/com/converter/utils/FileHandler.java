package com.converter.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {
    
    public static File openXmlFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier XML");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers XML", "*.xml")
        );
        return fileChooser.showOpenDialog(stage);
    }
    
    public static File openJsonFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier JSON");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers JSON", "*.json")
        );
        return fileChooser.showOpenDialog(stage);
    }
    
    public static File saveXmlFile(Stage stage, String content) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder le fichier XML");
        fileChooser.setInitialFileName("output.xml");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers XML", "*.xml")
        );
        
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            Files.write(Paths.get(file.getAbsolutePath()), 
                       content.getBytes(StandardCharsets.UTF_8));
        }
        return file;
    }
    
    public static File saveJsonFile(Stage stage, String content) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder le fichier JSON");
        fileChooser.setInitialFileName("output.json");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers JSON", "*.json")
        );
        
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            Files.write(Paths.get(file.getAbsolutePath()), 
                       content.getBytes(StandardCharsets.UTF_8));
        }
        return file;
    }
    
    public static String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), 
                         StandardCharsets.UTF_8);
    }
}