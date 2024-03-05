package fr.btsciel.mp3_tags;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class GestionTag {
    private final Tag tag;
    private final Path fileSource;
    private final byte[] bytes;

    public GestionTag(Path fileSource) {
        this.fileSource = fileSource;
        bytes = new byte[128];
        tag = new Tag();
    }
    public void lireTags() throws IOException {
        InputStream is = Files.newInputStream(fileSource);
        DataInputStream dis = new DataInputStream(is);

        dis.skipBytes((int)(Files.size(fileSource)) - 128);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = dis.readByte();
        }

        tag.setTitre(new String(bytes, 3, 30));
        tag.setArtiste(new String(bytes, 33, 30));
        tag.setAlbum(new String(bytes, 63, 30));
        tag.setAnnee(new String(bytes, 93, 4));
        tag.setCommentaire(new String(bytes, 97, 28));
        tag.setTrack(bytes[126]);
        tag.setGenre(bytes[127]);

        dis.close();
    }

    public Tag getTag() {
        return tag;
    }
}