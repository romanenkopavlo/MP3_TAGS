package fr.btsciel.mp3_tags;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Button buttonFichier;
    @FXML
    public Label labelChemin;
    @FXML
    public Label labelFichier;
    @FXML
    public Button playButton;
    @FXML
    public Button stopButton;
    @FXML
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
    public Button modifierButton;
    public Button enregistrerButton;
    public Media media;
    public MediaPlayer mediaPlayer;
    public Tag tagFichier;
    public File fichierSelectionner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titreText.setEditable(false);
        albumText.setEditable(false);
        artisteText.setEditable(false);
        anneeText.setEditable(false);
        commentText.setEditable(false);
        genreText.setEditable(false);
        playButton.setDisable(true);
        stopButton.setDisable(true);
        lireTagsButton.setDisable(true);
        modifierButton.setDisable(true);
        enregistrerButton.setDisable(true);
        enregistrerButton.setOpacity(0);
        setGreyColor();
        labelChemin.setText("");
        labelFichier.setText("");

        buttonFichier.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            ouvrirChoixFichier();
        });

        lireTagsButton.setOnAction(event -> {
            try {
                lireTags();
                modifierButton.setDisable(false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        modifierButton.setOnAction(event -> {
            if (fichierSelectionner != null) {
                enregistrerButton.setDisable(false);
                enregistrerButton.setOpacity(100);
                titreText.setEditable(true);
                albumText.setEditable(true);
                artisteText.setEditable(true);
                anneeText.setEditable(true);
                commentText.setEditable(true);
                genreText.setEditable(true);
                buttonFichier.setDisable(true);
                modifierButton.setDisable(true);
                lireTagsButton.setDisable(true);
                setBlueColor();
            }
        });

        enregistrerButton.setOnAction(event -> {
            titreText.setEditable(false);
            albumText.setEditable(false);
            artisteText.setEditable(false);
            anneeText.setEditable(false);
            commentText.setEditable(false);
            genreText.setEditable(false);
            buttonFichier.setDisable(false);
            modifierButton.setDisable(false);
            lireTagsButton.setDisable(false);
            enregistrerButton.setDisable(true);
            enregistrerButton.setOpacity(0);
            setGreyColor();
        });
    }

    private void ouvrirChoixFichier() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        fileChooser.setInitialDirectory(new File("./mp3"));
        fichierSelectionner = fileChooser.showOpenDialog(null);
        if (fichierSelectionner != null) {
            modifierButton.setDisable(true);
            lireTagsButton.setDisable(false);
            playButton.setDisable(false);
            labelChemin.setText(fichierSelectionner.toString());
            labelFichier.setText(fichierSelectionner.getAbsoluteFile().getName());

            titreLabel.setText("");
            albumLabel.setText("");
            artisteLabel.setText("");
            anneeLabel.setText("");
            commentLabel.setText("");
            genreLabel.setText("");

            titreText.setText("");
            albumText.setText("");
            artisteText.setText("");
            anneeText.setText("");
            commentText.setText("");
            genreText.setText("");

            String uriPath = fichierSelectionner.toURI().toString();
            media = new Media(uriPath);
            mediaPlayer = new MediaPlayer(media);
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
    }
    private void lireTags() throws IOException {
        if (fichierSelectionner != null) {
            tagFichier = new Tag();
            StringBuilder song = new StringBuilder();
            StringBuilder artist = new StringBuilder();
            StringBuilder album = new StringBuilder();
            StringBuilder year = new StringBuilder();
            StringBuilder comment = new StringBuilder();
            char caractere;

            Path path = fichierSelectionner.toPath();
            InputStream in = Files.newInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(in);
            DataInputStream dis = new DataInputStream(bis);

            dis.skipNBytes(fichierSelectionner.length() - 125);

            for (int i = 0; i < 30; i++) {
                caractere = (char) dis.readByte();
                song.append(caractere);
            }
            tagFichier.setTitre(String.valueOf(song));
            titreLabel.setText("Titre");
            titreText.setText(tagFichier.getTitre());

            for (int i = 0; i < 30; i++) {
                caractere = (char) dis.readByte();
                artist.append(caractere);
            }
            tagFichier.setArtiste(String.valueOf(artist));
            artisteLabel.setText("Artiste");
            artisteText.setText(tagFichier.getArtiste());

            for (int i = 0; i < 30; i++) {
                caractere = (char) dis.readByte();
                album.append(caractere);
            }
            tagFichier.setAlbum(String.valueOf(album));
            albumLabel.setText("Album");
            albumText.setText(tagFichier.getAlbum());

            for (int i = 0; i < 4; i++) {
                caractere = (char) dis.readByte();
                year.append(caractere);
            }
            tagFichier.setAnnee(String.valueOf(year));
            anneeLabel.setText("Annee");
            anneeText.setText(tagFichier.getAnnee());

            for (int i = 0; i < 30; i++) {
                caractere = (char) dis.readByte();
                comment.append(caractere);
            }
            tagFichier.setCommentaire(String.valueOf(comment));
            commentLabel.setText("Commentaire");
            commentText.setText(tagFichier.getCommentaire());

            tagFichier.setGenre(dis.readByte());
            genreLabel.setText("Genre");
            genreText.setText(String.valueOf(tagFichier.getGenre()));
            dis.close();
        }
    }

    private void ecrireTags() {

    }

    private void setGreyColor() {
        titreText.getStyleClass().clear();
        titreText.getStyleClass().addAll("text-field", "text-input","normal-text-field");

        albumText.getStyleClass().clear();
        albumText.getStyleClass().addAll("text-field", "text-input","normal-text-field");

        artisteText.getStyleClass().clear();
        artisteText.getStyleClass().addAll("text-field", "text-input","normal-text-field");

        anneeText.getStyleClass().clear();
        anneeText.getStyleClass().addAll("text-field", "text-input","normal-text-field");

        genreText.getStyleClass().clear();
        genreText.getStyleClass().addAll("text-field", "text-input","normal-text-field");

        commentText.getStyleClass().clear();
        commentText.getStyleClass().addAll("text-field", "text-input","normal-text-field");
    }

    private void setBlueColor() {
        titreText.getStyleClass().clear();
        titreText.getStyleClass().addAll("text-field", "text-input","modification-text-field");

        albumText.getStyleClass().clear();
        albumText.getStyleClass().addAll("text-field", "text-input","modification-text-field");

        artisteText.getStyleClass().clear();
        artisteText.getStyleClass().addAll("text-field", "text-input","modification-text-field");

        anneeText.getStyleClass().clear();
        anneeText.getStyleClass().addAll("text-field", "text-input","modification-text-field");

        genreText.getStyleClass().clear();
        genreText.getStyleClass().addAll("text-field", "text-input","modification-text-field");

        commentText.getStyleClass().clear();
        commentText.getStyleClass().addAll("text-field", "text-input","modification-text-field");
    }
}