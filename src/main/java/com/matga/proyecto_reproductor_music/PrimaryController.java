package com.matga.proyecto_reproductor_music;

import com.matga.proyecto_reproductor_music.controlador.CargadorCanciones;
import com.matga.proyecto_reproductor_music.modelo.Cancion;
import com.matga.proyecto_reproductor_music.modelo.Playlist;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
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
    private Button playPauseButton;
    
    @FXML
    private Slider progressSlider;
    
    @FXML
    private Slider volumeSlider;
    
    @FXML
    private Label currentTimeLabel;
    
    @FXML
    private Label songDurationLabel;
    
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
    }
    
    private void cargarCanciones() {
        try {
            // Obtener la ruta a la carpeta music dentro de resources
            URL musicUrl = getClass().getResource("/com/matga/proyecto_reproductor_music/music");
            if (musicUrl != null) {
                File musicDir = Paths.get(musicUrl.toURI()).toFile();
                playlist = CargadorCanciones.cargarPlaylistDesdeCarpeta(musicDir.getAbsolutePath());
                
                // Actualizar la vista de la lista de reproducción
                playlistView.getItems().clear();
                // Agregar todas las canciones de la playlist al ListView
                for (Cancion cancion : playlist) {
                    playlistView.getItems().add(cancion);
                }
                
                // Seleccionar la primera canción si existe
                if (!playlist.estaVacia()) {
                    playlistView.getSelectionModel().select(0);
                    actualizarInfoCancion(playlist.getCancionActual());
                }
            } else {
                System.err.println("No se pudo encontrar la carpeta de música");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    private void configurarEventos() {
        // Manejar selección de canción en la lista
        playlistView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Reproducir la canción seleccionada
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
        
        // Inicializar la línea de tiempo para actualizar la barra de progreso
        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> actualizarBarraProgreso()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }
    
    private void actualizarInfoCancion(Cancion cancion) {
        if (cancion != null) {
            songTitleLabel.setText(cancion.getTitulo());
            artistLabel.setText("Artista desconocido");
            
            // Actualizar el tiempo total de la canción
            songDurationLabel.setText(formatTime(cancion.getDuracionTotal()));
        }
    }
    
    private String formatTime(double seconds) {
        int minutos = (int) (seconds / 60);
        int segundos = (int) (seconds % 60);
        return String.format("%d:%02d", minutos, segundos);
    }
    
    private void actualizarBarraProgreso() {
        if (cancionActual != null && !isSeeking.get()) {
            double progreso = cancionActual.getTiempoActual() / cancionActual.getDuracionTotal();
            progressSlider.setValue(progreso * 100);
            currentTimeLabel.setText(formatTime(cancionActual.getTiempoActual()));
        }
    }
    
    @FXML
    private void togglePlayPause() {
        if (playlist == null || playlist.estaVacia()) {
            return;
        }
        
        if (cancionActual == null) {
            // Si no hay canción reproduciéndose, comenzar con la seleccionada o la primera
            int index = playlistView.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                index = 0;
                playlistView.getSelectionModel().select(index);
            }
            reproducirCancion(index);
        } else if (isPlaying) {
            // Pausar la reproducción
            cancionActual.pause();
            timeline.pause();
            isPlaying = false;
            playPauseButton.setText("▶");
        } else {
            // Reanudar la reproducción
            cancionActual.play();
            timeline.play();
            isPlaying = true;
            playPauseButton.setText("⏸");
        }
    }
    
    @FXML
    private void anterior() {
        if (playlist == null || playlist.estaVacia()) {
            return;
        }
        
        // Detener la reproducción actual
        if (cancionActual != null) {
            cancionActual.stop();
            timeline.stop();
        }
        
        // Ir a la canción anterior
        Cancion anterior = playlist.anterior();
        if (anterior != null) {
            // Actualizar la selección en la lista
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
        
        // Detener la reproducción actual
        if (cancionActual != null) {
            cancionActual.stop();
            timeline.stop();
        }
        
        // Ir a la siguiente canción
        Cancion siguiente = playlist.siguiente();
        if (siguiente != null) {
            // Actualizar la selección en la lista
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
     * Reproduce la canción en el índice especificado.
     * @param index Índice de la canción en la playlist
     */
    private void reproducirCancion(int index) {
        // Detener la reproducción actual si hay una canción sonando
        if (cancionActual != null) {
            cancionActual.stop();
            timeline.stop();
        }
        
        // Obtener la canción de la playlist
        cancionActual = playlist.reproducir(index);
        if (cancionActual != null) {
            // Configurar la canción para que pase a la siguiente al terminar
            cancionActual.setOnEndOfMedia(() -> {
                Platform.runLater(() -> siguiente());
            });
            
            // Aplicar el volumen actual
            cancionActual.setVolumen(volumeSlider.getValue() / 100.0);
            
            // Reproducir la canción
            cancionActual.play();
            isPlaying = true;
            playPauseButton.setText("⏸");
            
            // Actualizar la información de la canción en la interfaz
            actualizarInfoCancion(cancionActual);
            
            // Iniciar la actualización de la barra de progreso
            timeline.play();
        }
    }
}
