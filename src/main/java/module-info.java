module com.matga.proyecto_reproductor_music {
    // Explicitly requiring java.base to resolve IOException issues
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires transitive javafx.graphics;

    opens com.matga.proyecto_reproductor_music to javafx.fxml;
    opens com.matga.proyecto_reproductor_music.controlador to javafx.fxml;
    opens com.matga.proyecto_reproductor_music.modelo to javafx.base;
    
    exports com.matga.proyecto_reproductor_music;
    exports com.matga.proyecto_reproductor_music.controlador;
    exports com.matga.proyecto_reproductor_music.modelo;
}
