module fr.btsciel.mp3_tags {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens fr.btsciel.mp3_tags to javafx.fxml;
    exports fr.btsciel.mp3_tags;
}