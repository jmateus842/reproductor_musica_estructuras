package com.matga.proyecto_reproductor_music;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("modern"), 860, 640);
        scene.getStylesheets().add(App.class.getResource("modern-player.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("MATGA Player");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}