/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.converter.controllers;

import com.converter.models.ConversionConfig;
import com.converter.utils.AlertHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author pc
 */
public class MainMenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private RadioButton xmlToJsonRadio;
    
    @FXML
    private RadioButton jsonToXmlRadio;
    
    @FXML
    private RadioButton nativeMethodRadio;
    
    @FXML
    private RadioButton jacksonMethodRadio;
    
    @FXML
    private ToggleGroup conversionTypeGroup;
    
    @FXML
    private ToggleGroup conversionMethodGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleContinue() {
        try {
            boolean isXmlToJson = xmlToJsonRadio.isSelected();
            boolean isNativeMethod = nativeMethodRadio.isSelected();
        
            ConversionConfig config = new ConversionConfig(isXmlToJson, isNativeMethod);
        
            String fxmlFile = isXmlToJson ? "/fxml/XmlToJsonView.fxml" : "/fxml/JsonToXmlView.fxml";
        
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
        
            // Passer la configuration au controller approprié
            if (isXmlToJson) {
                XmlToJsonController controller = loader.getController();
                controller.setConfig(config);
            } else {
                JsonToXmlController controller = loader.getController();
                controller.setConfig(config);
            }
        
            Stage stage = (Stage) xmlToJsonRadio.getScene().getWindow();
            Scene scene = new Scene(root, 900, 650);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);
        
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Erreur", "Impossible de charger la vue de conversion.");
        }
    }    
    
}
