package fr.btsciel.mp3_tags;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Button buttonFichier;
    public Label labelChemin;
    public Label labelFichier;
    public Button playButton;
    public Button stopButton;
    public Button lireTagsButton;
    public Label titreLabel;
    public TextField titreText;
    public Label artisteLabel;
    public TextField artisteText;
    public Label albumLabel;
    public TextField albumText;
    public Label anneeLabel;
    public TextField anneeText;
    public Label commentLabel;
    public TextField commentText;
    public TextField genreText;
    public Label genreLabel;
    public Label trackLabel;
    public TextField trackText;
    public Button modifierButton;
    public Button enregistrerButton;
    public Media media;
    public MediaPlayer mediaPlayer;
    public File fichierSelectionner;
    public GestionTag gestionTag;
    public Path path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        unsetEditableTextFields();

        playButton.setDisable(true);
        stopButton.setDisable(true);

        lireTagsButton.setDisable(true);
        modifierButton.setDisable(true);
        enregistrerButton.setDisable(true);

        enregistrerButton.setOpacity(0);

        labelChemin.setText("");
        labelFichier.setText("");

        setGreyColor();
        albumText.backgroundProperty().bind(titreText.backgroundProperty());
        artisteText.backgroundProperty().bind(titreText.backgroundProperty());
        anneeText.backgroundProperty().bind(titreText.backgroundProperty());
        commentText.backgroundProperty().bind(titreText.backgroundProperty());
        genreText.backgroundProperty().bind(titreText.backgroundProperty());
        trackText.backgroundProperty().bind(titreText.backgroundProperty());

        buttonFichier.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            ouvrirChoixFichier();
        });

        lireTagsButton.setOnAction(event -> {
            try {
                lireTagsButton.setDisable(true);
                modifierButton.setDisable(false);

                gestionTag = new GestionTag(path);
                gestionTag.lireTags();

                titreLabel.setText("Titre");
                titreText.setText(gestionTag.getTag().getTitre());

                artisteLabel.setText("Artiste");
                artisteText.setText(gestionTag.getTag().getArtiste());

                albumLabel.setText("Album");
                albumText.setText(gestionTag.getTag().getAlbum());

                anneeLabel.setText("Annee");
                anneeText.setText(gestionTag.getTag().getAnnee());

                commentLabel.setText("Commentaire");
                commentText.setText(gestionTag.getTag().getCommentaire());

                trackLabel.setText("Track");
                trackText.setText(String.valueOf(gestionTag.getTag().getTrack()));

                genreLabel.setText("Genre");
                genreText.setText(String.valueOf(gestionTag.getTag().getGenre()));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        modifierButton.setOnAction(event -> {
            setEditableTextFields();
            setBlueColor();
            enregistrerButton.setOpacity(100);
            enregistrerButton.setDisable(false);
            buttonFichier.setDisable(true);
            modifierButton.setDisable(true);
            lireTagsButton.setDisable(true);
        });

        enregistrerButton.setOnAction(event -> {
            unsetEditableTextFields();
            setGreyColor();
            enregistrerButton.setOpacity(0);
            enregistrerButton.setDisable(true);

            buttonFichier.setDisable(false);
            modifierButton.setDisable(false);
            lireTagsButton.setDisable(true);
        });

        playButton.setOnAction(event -> {
            mediaPlayer.play();
            playButton.setDisable(true);
            stopButton.setDisable(false);
        });

        stopButton.setOnAction(event -> {
            mediaPlayer.stop();
            stopButton.setDisable(true);
            playButton.setDisable(false);
        });
    }

    private void ouvrirChoixFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        fileChooser.setInitialDirectory(new File("./mp3"));
        fichierSelectionner = fileChooser.showOpenDialog(null);
        if (fichierSelectionner != null) {
            path = fichierSelectionner.toPath();
            modifierButton.setDisable(true);
            lireTagsButton.setDisable(false);
            playButton.setDisable(false);
            stopButton.setDisable(true);

            labelChemin.setText(fichierSelectionner.toString());
            labelFichier.setText(fichierSelectionner.getAbsoluteFile().getName());

            titreLabel.setText("");
            albumLabel.setText("");
            artisteLabel.setText("");
            anneeLabel.setText("");
            commentLabel.setText("");
            genreLabel.setText("");
            trackLabel.setText("");

            titreText.setText("");
            albumText.setText("");
            artisteText.setText("");
            anneeText.setText("");
            commentText.setText("");
            genreText.setText("");
            trackText.setText("");

            String fileName =fichierSelectionner.getName();
            int point = fileName.lastIndexOf(".");

            System.out.println(fileName.substring(point + 1));

            if ((fileName.substring(point + 1)).equals("mp3")) {
                String uriPath = fichierSelectionner.toURI().toString();
                media = new Media(uriPath);
                mediaPlayer = new MediaPlayer(media);
            } else {
                Alert dialogWindow = new Alert(Alert.AlertType.ERROR);
                dialogWindow.setTitle("Error");
                dialogWindow.setHeaderText(null);
                dialogWindow.setContentText("Extension file error");
                dialogWindow.showAndWait();
                Platform.exit();
            }
        }
    }

    private void setGreyColor() {
        titreText.getStyleClass().clear();
        titreText.getStyleClass().addAll("text-field", "text-input","normal-text-field");
    }

    private void setBlueColor() {
        titreText.getStyleClass().clear();
        titreText.getStyleClass().addAll("text-field", "text-input","modification-text-field");
    }

    private void setEditableTextFields() {
        titreText.setEditable(true);
        albumText.setEditable(true);
        artisteText.setEditable(true);
        anneeText.setEditable(true);
        commentText.setEditable(true);
        genreText.setEditable(true);
        trackText.setEditable(true);
    }

    private void unsetEditableTextFields() {
        titreText.setEditable(false);
        albumText.setEditable(false);
        artisteText.setEditable(false);
        anneeText.setEditable(false);
        commentText.setEditable(false);
        genreText.setEditable(false);
        trackText.setEditable(false);
    }
}