package com.matga.proyecto_reproductor_music;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Aplicacion JavaFX
 */
public class App extends Application {

    private static Scene scene;
    

    @Override
    public void start(Stage stage) throws IOException {
        // Verificar directorio de musica
        checkMusicDirectory();
        
        scene = new Scene(loadFXML("modern"), 860, 640);
        scene.getStylesheets().add(App.class.getResource("modern-player.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("MATGA Player");
        stage.show();
    }
    
    private void checkMusicDirectory() {
        try {
            String userDir = System.getProperty("user.dir");
            System.out.println("Current working directory: " + userDir);
            
            // Comprobar con y sin acento para manejar problemas de codificacion
            String[] possibleDirNames = {"musica", "música"};
            boolean dirFound = false;
            
            for (String dirName : possibleDirNames) {
                Path musicDir = Paths.get(userDir, dirName);
                System.out.println("Checking directory: " + musicDir);
                
                if (Files.exists(musicDir) && Files.isDirectory(musicDir)) {
                    System.out.println("Found music directory: " + musicDir);
                    System.out.println("Directory contents:");
                    try {
                        Files.list(musicDir).forEach(path -> 
                            System.out.println("  " + path.getFileName()));
                    } catch (IOException e) {
                        System.err.println("Error listing directory: " + e.getMessage());
                    }
                    dirFound = true;
                    break;
                }
            }
            
            if (!dirFound) {
                // Crear el directorio si no existe
                Path musicDir = Paths.get(userDir, "musica");
                System.out.println("Music directory not found. Creating: " + musicDir);
                try {
                    Files.createDirectories(musicDir);
                    System.out.println("Created music directory at: " + musicDir);
                    showAlert(AlertType.INFORMATION, 
                        "Directorio de musica creado", 
                        "Se ha creado el directorio 'musica' en: " + musicDir + 
                        "\nPor favor, coloca tus archivos de musica aqui.");
                } catch (IOException e) {
                    System.err.println("Failed to create directory: " + e.getMessage());
                    showAlert(AlertType.ERROR, 
                        "Error", 
                        "No se pudo crear el directorio de musica: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error in checkMusicDirectory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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