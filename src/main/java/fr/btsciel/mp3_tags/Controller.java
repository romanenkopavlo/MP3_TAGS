package fr.btsciel.mp3_tags;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Button buttonFichier;
    @FXML
    public Label labelChemin;
    @FXML
    public Label labelFichier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelChemin.setText("");
        labelFichier.setText("");
        buttonFichier.setOnAction(event -> ouvrirChoixFichier());
    }

    private void ouvrirChoixFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        fileChooser.setInitialDirectory(new File("./mp3"));
        File fichierSelectionner = fileChooser.showOpenDialog(null);
        if (fichierSelectionner != null) {
            labelChemin.setText(fichierSelectionner.toString());
            labelFichier.setText(fichierSelectionner.getAbsoluteFile().getName());
        }
    }
}