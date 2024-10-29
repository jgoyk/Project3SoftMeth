package com.example.project3softmeth;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClinicManagerController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void runButtonOnClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private Label commandText;


}