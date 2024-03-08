package fr.btsciel.mp3_tags;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class GestionTag {
    private final Tag tag;
    private final Path fileSource;
    private final byte[] bytes;
    private final byte[] bytesID3;

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

        if (new String(bytesID3).equals("ID3")) {
            tag.setTitre(new String(bytes, 3, 30));
            tag.setArtiste(new String(bytes, 33, 30));
            tag.setAlbum(new String(bytes, 63, 30));
            tag.setAnnee(new String(bytes, 93, 4));
            tag.setCommentaire(new String(bytes, 97, 28));
            tag.setTrack(bytes[126]);
            tag.setGenre(bytes[127]);
        }
    }

    public void ecrireTags() throws IOException {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) 0x00;
        }
        for (int i = 0; i < tag.getTitre().length(); i++) {
            bytes[3 + i] = (byte) tag.getTitre().charAt(i);
        }
        for (int i = 0; i < tag.getArtiste().length(); i++) {
            bytes[33 + i] = (byte) tag.getArtiste().charAt(i);
        }
        for (int i = 0; i < tag.getAlbum().length(); i++) {
            bytes[63 + i] = (byte) tag.getAlbum().charAt(i);
        }
        for (int i = 0; i < tag.getAnnee().length(); i++) {
            bytes[93 + i] = (byte) tag.getAnnee().charAt(i);
        }
        for (int i = 0; i< tag.getCommentaire().length(); i++) {
            bytes[97 + i] = (byte) tag.getCommentaire().charAt(i);
        }

        bytes[126] = tag.getTrack();
        bytes[127] = tag.getGenre();

        RandomAccessFile randomAccessFile = new RandomAccessFile(fileSource.toAbsolutePath().toString(), "rw");
        long size = randomAccessFile.length();
        randomAccessFile.seek(size - 128);
        randomAccessFile.write(bytes);
        randomAccessFile.close();
    }

    public void effacerTags() {
        tag.setTitre("");
        tag.setArtiste("");
        tag.setAlbum("");
        tag.setAnnee("");
        tag.setCommentaire("");
        tag.setTrack((byte) 0x00);
        tag.setGenre((byte) 0x00);
    }

    public boolean isMP3() {
        return new String(bytesID3).equals("ID3");
    }

    public Tag getTag() {
        return tag;
    }
}