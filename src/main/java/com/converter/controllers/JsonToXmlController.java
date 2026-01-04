package com.converter.controllers;

import com.converter.models.ConversionConfig;
import com.converter.models.ConversionResult;
import com.converter.services.*;
import com.converter.validators.JsonValidator;
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

public class JsonToXmlController {
    
    @FXML
    private Label methodLabel;
    
    @FXML
    private TextArea jsonInputArea;
    
    @FXML
    private TextArea xmlOutputArea;
    
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
    private JsonValidator jsonValidator;
    private IConverter converter;
    private String currentXmlOutput;
    
    @FXML
    private void initialize() {
        jsonValidator = new JsonValidator();
        
        jsonInputArea.textProperty().addListener((obs, oldVal, newVal) -> {
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
        
        // Listener pour activer le bouton copier
        xmlOutputArea.textProperty().addListener((obs, oldVal, newVal) -> {
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
        
        if (config.isNativeMethod()) {
            converter = new NativeJsonToXmlConverter();
        } else {
            converter = new JacksonJsonToXmlConverter();
        }
    }
    
    @FXML
    private void handleLoadFile() {
        try {
            File file = FileHandler.openJsonFile((Stage) jsonInputArea.getScene().getWindow());
            
            if (file != null) {
                String content = FileHandler.readFile(file);
                jsonInputArea.setText(content);
                
                xmlOutputArea.clear();
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
    private void handleClearInput() {  // NOUVEAU
        jsonInputArea.clear();
        xmlOutputArea.clear();
        currentXmlOutput = null;
        
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
        String jsonContent = jsonInputArea.getText().trim();
        
        if (jsonContent.isEmpty()) {
            AlertHelper.showWarning("Champ vide", "Veuillez entrer du code JSON.");
            return;
        }
        
        try {
            boolean isValid = jsonValidator.validate(jsonContent);
            
            if (isValid) {
                validationStatusLabel.setText("✓ Statut : JSON valide - Prêt pour la conversion");
                validationStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                convertButton.setDisable(false);
                AlertHelper.showSuccess("Validation réussie", "Le code JSON est bien formé et valide.");
            }
            
        } catch (Exception e) {
            validationStatusLabel.setText("✗ Statut : JSON invalide");
            validationStatusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            convertButton.setDisable(true);
            AlertHelper.showError("JSON Invalide", 
                "Le code JSON contient des erreurs :\n\n" + e.getMessage());
        }
    }
    
    @FXML
    private void handleConvert() {
        String jsonContent = jsonInputArea.getText().trim();
        
        try {
            validationStatusLabel.setText("⏳ Conversion en cours...");
            validationStatusLabel.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
            
            ConversionResult result = converter.convert(jsonContent);
            
            if (result.isSuccess()) {
                currentXmlOutput = result.getOutput();
                xmlOutputArea.setText(currentXmlOutput);
                downloadButton.setDisable(false);
                
                validationStatusLabel.setText("✓ Conversion réussie !");
                validationStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                
                AlertHelper.showSuccess("Conversion réussie", 
                    "Le JSON a été converti en XML avec succès.");
            } else {
                throw new Exception(result.getErrorMessage());
            }
            
        } catch (Exception e) {
            validationStatusLabel.setText("✗ Erreur de conversion");
            validationStatusLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            
            AlertHelper.showError("Erreur de conversion", 
                "Impossible de convertir le JSON :\n\n" + e.getMessage());
        }
    }
    
    @FXML
    private void handleCopyOutput() {  // NOUVEAU
        String xmlContent = xmlOutputArea.getText().trim();
        
        if (xmlContent.isEmpty()) {
            AlertHelper.showWarning("Aucun contenu", "Aucun résultat XML à copier.");
            return;
        }
        
        try {
            // Copier dans le presse-papiers
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(xmlContent);
            clipboard.setContent(content);
            
            AlertHelper.showSuccess("Copié !", 
                "Le code XML a été copié dans le presse-papiers.");
            
        } catch (Exception e) {
            AlertHelper.showError("Erreur de copie", 
                "Impossible de copier le contenu : " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDownload() {
        if (currentXmlOutput == null || currentXmlOutput.isEmpty()) {
            AlertHelper.showWarning("Aucun contenu", "Aucun résultat XML à télécharger.");
            return;
        }
        
        try {
            File file = FileHandler.saveXmlFile(
                (Stage) xmlOutputArea.getScene().getWindow(),
                currentXmlOutput
            );
            
            if (file != null) {
                AlertHelper.showSuccess("Téléchargement réussi", 
                    "Le fichier XML a été sauvegardé :\n" + file.getAbsolutePath());
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
            
            Stage stage = (Stage) jsonInputArea.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
            
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Erreur", "Impossible de retourner au menu principal.");
        }
    }
}