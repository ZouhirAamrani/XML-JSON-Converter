package com.converter.controllers;

import com.converter.models.ConversionConfig;
import com.converter.models.ConversionResult;
import com.converter.services.*;
import com.converter.validators.XmlValidator;
import com.converter.utils.FileHandler;
import com.converter.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class XmlToJsonController {
    
    @FXML
    private Label methodLabel;
    
    @FXML
    private TextArea xmlInputArea;
    
    @FXML
    private TextArea jsonOutputArea;
    
    @FXML
    private Label validationStatusLabel;
    
    @FXML
    private Button validateButton;
    
    @FXML
    private Button convertButton;
    
    @FXML
    private Button downloadButton;
    
    @FXML
    private Button clearInputButton;
    
    @FXML
    private Button copyOutputButton;
    
    private ConversionConfig config;
    private XmlValidator xmlValidator;
    private IConverter converter;
    private String currentJsonOutput;
    
    @FXML
    private void initialize() {
        xmlValidator = new XmlValidator();
        
        // Listener pour activer le bouton de validation quand du texte est entré
        xmlInputArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                validateButton.setDisable(false);
                clearInputButton.setDisable(false);
            } else {
                validateButton.setDisable(true);
                convertButton.setDisable(true);
                clearInputButton.setDisable(true);
                validationStatusLabel.setText("Statut : En attente de validation");
                validationStatusLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
            }
        });
        
        // Listener pour activer le bouton copier quand il y a du résultat 
        jsonOutputArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                copyOutputButton.setDisable(false);
            } else {
                copyOutputButton.setDisable(true);
            }
        });
    }
    
    public void setConfig(ConversionConfig config) {
        this.config = config;
        methodLabel.setText(config.getMethodType());
        
        // Initialiser le bon convertisseur
        if (config.isNativeMethod()) {
            converter = new NativeXmlToJsonConverter();
        } else {
            converter = new JacksonXmlToJsonConverter();
        }
    }
    
    @FXML
    private void handleLoadFile() {
        try {
            File file = FileHandler.openXmlFile((Stage) xmlInputArea.getScene().getWindow());
            
            if (file != null) {
                String content = FileHandler.readFile(file);
                xmlInputArea.setText(content);
                
                // Réinitialiser l'état
                jsonOutputArea.clear();
                convertButton.setDisable(true);
                downloadButton.setDisable(true);
                validationStatusLabel.setText("Statut : Fichier chargé - En attente de validation");
                validationStatusLabel.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;");
                
                AlertHelper.showInfo("Fichier chargé", 
                    "Le fichier " + file.getName() + " a été chargé avec succès.");
            }
        } catch (Exception e) {
            AlertHelper.showError("Erreur de chargement", 
                "Impossible de charger le fichier : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleClearInput() {  
        xmlInputArea.clear();
        jsonOutputArea.clear();
        currentJsonOutput = null;
        
        // Réinitialiser l'état
        validateButton.setDisable(true);
        convertButton.setDisable(true);
        downloadButton.setDisable(true);
        copyOutputButton.setDisable(true);
        
        validationStatusLabel.setText("Statut : En attente de validation");
        validationStatusLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
    }
    
    @FXML
    private void handleValidate() {
        String xmlContent = xmlInputArea.getText().trim();
        
        if (xmlContent.isEmpty()) {
            AlertHelper.showWarning("Champ vide", "Veuillez entrer du code XML.");
            return;
        }
        
        try {
            boolean isValid = xmlValidator.validate(xmlContent);
            
            if (isValid) {
                validationStatusLabel.setText("✓ Statut : XML valide - Prêt pour la conversion");
                validationStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                convertButton.setDisable(false);
                AlertHelper.showSuccess("Validation réussie", "Le code XML est bien formé et valide.");
            }
            
        } catch (Exception e) {
            validationStatusLabel.setText("✗ Statut : XML invalide");
            validationStatusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            convertButton.setDisable(true);
            AlertHelper.showError("XML Invalide", 
                "Le code XML contient des erreurs :\n\n" + e.getMessage());
        }
    }
    
    @FXML
    private void handleConvert() {
        String xmlContent = xmlInputArea.getText().trim();
        
        try {
            // Afficher un indicateur de progression (optionnel)
            validationStatusLabel.setText("⏳ Conversion en cours...");
            validationStatusLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
            
            // Effectuer la conversion
            ConversionResult result = converter.convert(xmlContent);
            
            if (result.isSuccess()) {
                currentJsonOutput = result.getOutput();
                jsonOutputArea.setText(currentJsonOutput);
                downloadButton.setDisable(false);
                
                validationStatusLabel.setText("✓ Conversion réussie !");
                validationStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                
                AlertHelper.showSuccess("Conversion réussie", 
                    "Le XML a été converti en JSON avec succès.");
            } else {
                throw new Exception(result.getErrorMessage());
            }
            
        } catch (Exception e) {
            validationStatusLabel.setText("✗ Erreur de conversion");
            validationStatusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            
            AlertHelper.showError("Erreur de conversion", 
                "Impossible de convertir le XML :\n\n" + e.getMessage());
        }
    }
    
    @FXML
    private void handleCopyOutput() {  
        String jsonContent = jsonOutputArea.getText().trim();
        
        if (jsonContent.isEmpty()) {
            AlertHelper.showWarning("Aucun contenu", "Aucun résultat JSON à copier.");
            return;
        }
        
        try {
            // Copier dans le presse-papiers
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(jsonContent);
            clipboard.setContent(content);
            
            AlertHelper.showSuccess("Copié !", 
                "Le code JSON a été copié dans le presse-papiers.");
            
        } catch (Exception e) {
            AlertHelper.showError("Erreur de copie", 
                "Impossible de copier le contenu : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDownload() {
        if (currentJsonOutput == null || currentJsonOutput.isEmpty()) {
            AlertHelper.showWarning("Aucun contenu", "Aucun résultat JSON à télécharger.");
            return;
        }
        
        try {
            File file = FileHandler.saveJsonFile(
                (Stage) jsonOutputArea.getScene().getWindow(),
                currentJsonOutput
            );
            
            if (file != null) {
                AlertHelper.showSuccess("Téléchargement réussi", 
                    "Le fichier JSON a été sauvegardé :\n" + file.getAbsolutePath());
            }
            
        } catch (Exception e) {
            AlertHelper.showError("Erreur de sauvegarde", 
                "Impossible de sauvegarder le fichier : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) xmlInputArea.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
            
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Erreur", "Impossible de retourner au menu principal.");
        }
    }
}