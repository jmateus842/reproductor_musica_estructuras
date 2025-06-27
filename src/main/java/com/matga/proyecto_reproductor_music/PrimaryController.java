package com.matga.proyecto_reproductor_music;

import com.matga.proyecto_reproductor_music.controlador.CargadorCanciones;
import com.matga.proyecto_reproductor_music.modelo.Cancion;
import com.matga.proyecto_reproductor_music.modelo.Playlist;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrimaryController {

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private BorderPane playlistPane;
    
    @FXML
    private ListView<Cancion> playlistView;
    
    @FXML
    private Label songTitleLabel;
    
    @FXML
    private Label artistLabel;
    
    @FXML
    private Button playButton;
    
    @FXML
    private Button pauseButton;
    
    @FXML
    private Slider progressSlider;
    
    @FXML
    private Slider volumeSlider;
    
    @FXML
    private Label currentTimeLabel;
    
    @FXML
    private Label songDurationLabel;
    
    @FXML
    private Label tiempoLabel;
    
    @FXML
    private ImageView albumCoverImageView;
    
    private Playlist playlist;
    private boolean isPlaying = false;
    private boolean playlistVisible = true;
    private Cancion cancionActual;
    private Timeline timeline;
    private final AtomicBoolean isSeeking = new AtomicBoolean(false);

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void initialize() {
        cargarCanciones();
        configurarEventos();
        // No se necesita inicializacion adicional ya que se reproducira una cancion aleatoria en cargarCanciones()
    }
    
    private void cargarCanciones() {
        try {
            // Obtener la ruta al directorio del proyecto
            String projectPath = System.getProperty("user.dir");
            File musicDir = new File(projectPath, "musica");
            
            System.out.println("Buscando musica en: " + musicDir.getAbsolutePath());
            
            if (musicDir.exists() && musicDir.isDirectory()) {
                playlist = CargadorCanciones.cargarPlaylistDesdeCarpeta(musicDir.getAbsolutePath());
                
                // Actualizar la vista de la lista de reproduccion
                Platform.runLater(() -> {
                    playlistView.getItems().clear();
                    for (Cancion cancion : playlist) {
                        playlistView.getItems().add(cancion);
                    }
                    
                    // Seleccionar y reproducir una cancion aleatoria si existe
                    if (!playlist.estaVacia()) {
                        int total = playlist.getTotalCanciones();
                        int randomIndex = (int) (Math.random() * total);
                        playlistView.getSelectionModel().select(randomIndex);
                        reproducirCancion(randomIndex);
                        System.out.println("Reproduciendo cancion aleatoria: " + randomIndex);
                    }
                });
            } else {
                System.err.println("No se pudo encontrar la carpeta de musica en: " + musicDir.getAbsolutePath());
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("No se encontro la carpeta de musica en: " + musicDir.getAbsolutePath() + 
                                      "\nPor favor, crea una carpeta llamada 'musica' con archivos MP3.");
                    alert.showAndWait();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error al cargar las canciones: " + e.getMessage());
                alert.showAndWait();
            });
        }
    }
    
    private void configurarEventos() {
        // Manejar seleccion de cancion en la lista
        playlistView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Reproducir la cancion seleccionada
                int index = playlistView.getSelectionModel().getSelectedIndex();
                reproducirCancion(index);
            }
        });
        
        // Configurar el control de volumen
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (cancionActual != null) {
                double volumen = newVal.doubleValue() / 100.0;
                cancionActual.setVolumen(volumen);
            }
        });
        volumeSlider.setValue(50); // Volumen por defecto al 50%
        
        // Configurar la barra de progreso
        progressSlider.setValue(0);
        progressSlider.setMax(100);
        progressSlider.setOnMousePressed(e -> isSeeking.set(true));
        progressSlider.setOnMouseReleased(e -> {
            if (cancionActual != null && isSeeking.get()) {
                double newPosition = progressSlider.getValue() / 100.0;
                cancionActual.setTiempoReproduccion(newPosition * cancionActual.getDuracionTotal());
            }
            isSeeking.set(false);
        });
        
        // Inicializar la linea de tiempo para actualizar la barra de progreso
        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> actualizarBarraProgreso()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }
    
    private void actualizarInfoCancion(Cancion cancion) {
        if (cancion != null) {
            songTitleLabel.setText(cancion.getTitulo());
            artistLabel.setText("Artista desconocido");
            
            // Actualizar el tiempo total de la cancion
            double duracionTotal = cancion.getDuracionTotal();
            songDurationLabel.setText(formatTime(duracionTotal));
            
            // Actualizar tambien la etiqueta de tiempo en el visualizador
            tiempoLabel.setText(formatTime(duracionTotal));
            
            // Por ahora usamos la imagen por defecto para todas las canciones
            // En una implementacion mas avanzada, se podria extraer la caratula de los metadatos MP3
            try {
                // Intentar cargar la imagen desde los recursos
                Image defaultCover = new Image(getClass().getResourceAsStream("/com/matga/proyecto_reproductor_music/images/radio_canela.png"));
                albumCoverImageView.setImage(defaultCover);
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen de caratula: " + e.getMessage());
            }
            
            // Forzar una actualizacion inmediata de la barra de progreso
            actualizarBarraProgreso();
        }
    }
    
    private String formatTime(double seconds) {
        int minutos = (int) (seconds / 60);
        int segundos = (int) (seconds % 60);
        return String.format("%d:%02d", minutos, segundos);
    }
    
    private void actualizarBarraProgreso() {
        if (cancionActual != null && !isSeeking.get()) {
            double tiempoActual = cancionActual.getTiempoActual();
            double duracionTotal = cancionActual.getDuracionTotal();
            
            // Actualizar la barra de progreso
            if (duracionTotal > 0) {
                progressSlider.setValue((tiempoActual / duracionTotal) * 100);
            } else {
                progressSlider.setValue(0);
            }
            
            // Actualizar etiquetas de tiempo
            currentTimeLabel.setText(formatTime(tiempoActual));
            tiempoLabel.setText(formatTime(duracionTotal));
        }
    }
    
    @FXML
    private void togglePlayPause() {
        if (playlist == null || playlist.estaVacia()) {
            return;
        }
        
        if (cancionActual == null) {
            // Si no hay cancion reproduciendose, comenzar con la seleccionada o la primera
            int index = playlistView.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                index = 0;
                playlistView.getSelectionModel().select(index);
            }
            reproducirCancion(index);
        } else if (isPlaying) {
            pauseMusic();
        } else {
            playMusic();
        }
    }
    
    /**
     * Inicia o reanuda la reproduccion de la cancion actual.
     */
    @FXML
    private void playMusic() {
        if (cancionActual != null) {
            cancionActual.play();
            timeline.play();
            isPlaying = true;
            
            // Actualizar opacidad de botones
            playButton.setOpacity(0.3);
            pauseButton.setOpacity(1.0);
        } else {
            // Si no hay cancion actual, intentar reproducir la seleccionada
            togglePlayPause();
        }
    }
    
    /**
     * Pausa la reproduccion de la cancion actual.
     */
    @FXML
    private void pauseMusic() {
        if (cancionActual != null && isPlaying) {
            cancionActual.pause();
            timeline.pause();
            isPlaying = false;
            
            // Actualizar opacidad de botones
            playButton.setOpacity(1.0);
            pauseButton.setOpacity(0.3);
        }
    }
    
    @FXML
    private void anterior() {
        if (playlist == null || playlist.estaVacia()) {
            return;
        }
        
        // Detener la reproduccion actual
        if (cancionActual != null) {
            cancionActual.stop();
            timeline.stop();
        }
        
        // Ir a la cancion anterior
        Cancion anterior = playlist.anterior();
        if (anterior != null) {
            // Actualizar la seleccion en la lista
            int index = playlistView.getItems().indexOf(anterior);
            if (index >= 0) {
                playlistView.getSelectionModel().select(index);
            }
            reproducirCancion(playlist.getCancionActualIndex());
        }
    }
    
    @FXML
    private void siguiente() {
        if (playlist == null || playlist.estaVacia()) {
            return;
        }
        
        // Detener la reproduccion actual
        if (cancionActual != null) {
            cancionActual.stop();
            timeline.stop();
        }
        
        // Ir a la siguiente cancion
        Cancion siguiente = playlist.siguiente();
        if (siguiente != null) {
            // Actualizar la seleccion en la lista
            int index = playlistView.getItems().indexOf(siguiente);
            if (index >= 0) {
                playlistView.getSelectionModel().select(index);
            }
            reproducirCancion(playlist.getCancionActualIndex());
        }
    }

    @FXML
    private void togglePlaylist() {
        Stage stage = (Stage) mainSplitPane.getScene().getWindow();
        if (playlistVisible) {
            mainSplitPane.getItems().remove(playlistPane);
        } else {
            mainSplitPane.getItems().add(playlistPane);
            mainSplitPane.setDividerPositions(0.5);
        }
        playlistVisible = !playlistVisible;
        stage.sizeToScene();
    }
    
    /**
     * Detiene la reproduccion actual y reinicia la cancion al principio.
     */
    @FXML
    private void stopReproduccion() {
        if (cancionActual != null) {
            // Detener la reproduccion
            cancionActual.stop();
            timeline.stop();
            isPlaying = false;
            
            // Actualizar la interfaz
            playButton.setOpacity(1.0);
            pauseButton.setOpacity(0.3);
            progressSlider.setValue(0);
            currentTimeLabel.setText("0:00");
        }
    }
    
    /**
     * Reproduce la cancion en el indice especificado.
     * @param index Indice de la cancion en la playlist
     */
    private void reproducirCancion(int index) {
        // Detener la reproduccion actual si hay una cancion sonando
        if (cancionActual != null) {
            cancionActual.stop();
            timeline.stop();
        }
        
        // Obtener la cancion de la playlist
        cancionActual = playlist.reproducir(index);
        if (cancionActual != null) {
            // Configurar la cancion para que pase a la siguiente al terminar
            cancionActual.setOnEndOfMedia(() -> {
                Platform.runLater(() -> siguiente());
            });
            
            // Aplicar el volumen actual
            cancionActual.setVolumen(volumeSlider.getValue() / 100.0);
            
            // Reproducir la cancion
            cancionActual.play();
            isPlaying = true;
            
            // Actualizar opacidad de botones
            playButton.setOpacity(0.3);
            pauseButton.setOpacity(1.0);
            
            // Actualizar la informacion de la cancion en la interfaz
            actualizarInfoCancion(cancionActual);
            
            // Iniciar la actualizacion de la barra de progreso
            timeline.play();
        }
    }
}
