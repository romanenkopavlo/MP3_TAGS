module fr.btsciel.mp3_tags {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.btsciel.mp3_tags to javafx.fxml;
    exports fr.btsciel.mp3_tags;
}