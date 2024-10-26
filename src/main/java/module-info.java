module com.example.project3softmeth {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project3softmeth to javafx.fxml;
    exports com.example.project3softmeth;
}