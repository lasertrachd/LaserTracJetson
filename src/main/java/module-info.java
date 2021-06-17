module org.LaserTrac {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.LaserTrac to javafx.fxml;
    exports org.LaserTrac;
}