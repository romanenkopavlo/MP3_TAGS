package fr.btsciel.mp3_tags;

import javafx.scene.control.Alert;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class GestionTag {
    private final Tag tag;
    private final Path fileSource;
    private final byte[] bytes;
    private final byte[] bytesID3;
    private boolean error = false;

    public GestionTag(Path fileSource) {
        this.fileSource = fileSource;
        bytesID3 = new byte[3];
        bytes = new byte[128];
        tag = new Tag();
    }

    public void lireTags() throws IOException {
        InputStream is = Files.newInputStream(fileSource);
        DataInputStream dis = new DataInputStream(is);

        dis.read(bytesID3);
        dis.skipBytes((int)(Files.size(fileSource)) - 131);
        dis.read(bytes);
        dis.close();

        System.out.println(new String(bytesID3));

        if (new String(bytesID3).equals("ID3")) {
            tag.setTitre(new String(bytes, 3, 30));
            tag.setArtiste(new String(bytes, 33, 30));
            tag.setAlbum(new String(bytes, 63, 30));
            tag.setAnnee(new String(bytes, 93, 4));
            tag.setCommentaire(new String(bytes, 97, 28));
            tag.setTrack(bytes[126]);
            tag.setGenre(bytes[127]);
        } else {
            error = true;
            Alert dialogWindow = new Alert(Alert.AlertType.ERROR);
            dialogWindow.setTitle("Error");
            dialogWindow.setHeaderText(null);
            dialogWindow.setContentText("Extension file error");
            dialogWindow.showAndWait();
        }
    }

    public void ecrireTags() {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) 0x00;
        }
        for (int i = 0; i < tag.getTitre().length(); i++) {
            bytes[3 + i] = (byte) tag.getTitre().charAt(i);
        }

    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Tag getTag() {
        return tag;
    }
}