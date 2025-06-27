package com.matga.proyecto_reproductor_music.modelo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * Clase que representa una cancion en el reproductor de musica.
 */
public class Cancion {
    private String titulo;
    private String rutaArchivo; // Ruta absoluta o relativa al archivo MP3
    private Media media;
    private MediaPlayer mediaPlayer;
    private Runnable onEndOfMedia;

    /**
     * Constructor simple para crear una cancion solo con titulo y ruta.
     * @param titulo Nombre a mostrar (generalmente el nombre del archivo)
     * @param rutaArchivo Ruta al archivo MP3
     */
    public Cancion(String titulo, String rutaArchivo) {
        this.titulo = titulo;
        this.rutaArchivo = rutaArchivo;
        try {
            // Crear el objeto Media con la ruta del archivo
            File file = new File(rutaArchivo);
            this.media = new Media(file.toURI().toString());
            this.mediaPlayer = new MediaPlayer(media);
            
            // Configurar el manejador para cuando termine la cancion
            mediaPlayer.setOnEndOfMedia(() -> {
                if (onEndOfMedia != null) {
                    onEndOfMedia.run();
                }
            });
        } catch (Exception e) {
            System.err.println("Error al cargar el archivo de audio: " + e.getMessage());
        }
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    @Override
    public String toString() {
        return titulo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cancion cancion = (Cancion) obj;
        return rutaArchivo.equals(cancion.rutaArchivo);
    }

    @Override
    public int hashCode() {
        return rutaArchivo.hashCode();
    }
    
    // Metodos para controlar la reproduccion
    
    /**
     * Reproduce la cancion.
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    
    /**
     * Pausa la reproduccion de la cancion.
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
    
    /**
     * Detiene la reproduccion de la cancion y la reinicia al principio.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    
    /**
     * Establece el volumen de reproduccion.
     * @param volumen Valor entre 0.0 (silencio) y 1.0 (volumen maximo)
     */
    public void setVolumen(double volumen) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volumen);
        }
    }
    
    /**
     * Establece la posicion de reproduccion actual.
     * @param segundos Tiempo en segundos
     */
    public void setTiempoReproduccion(double segundos) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(javafx.util.Duration.seconds(segundos));
        }
    }
    
    /**
     * Obtiene el tiempo actual de reproduccion en segundos.
     * @return Tiempo actual en segundos
     */
    public double getTiempoActual() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0;
    }
    
    /**
     * Obtiene la duracion total de la cancion en segundos.
     * @return Duracion total en segundos
     */
    public double getDuracionTotal() {
        if (mediaPlayer != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return 0;
    }
    
    /**
     * Establece un manejador para cuando termine la reproduccion.
     * @param handler Manejador a ejecutar
     */
    public void setOnEndOfMedia(Runnable handler) {
        this.onEndOfMedia = handler;
    }
}
