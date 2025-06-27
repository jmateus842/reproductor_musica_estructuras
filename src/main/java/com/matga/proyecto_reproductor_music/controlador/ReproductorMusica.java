package com.matga.proyecto_reproductor_music.controlador;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import com.matga.proyecto_reproductor_music.modelo.Cancion;
import java.io.File;

/**
 * Controlador para la reproduccion de audio.
 * Maneja la reproduccion, pausa, detencion y control de volumen de las canciones.
 */
public class ReproductorMusica {
    private MediaPlayer mediaPlayer;
    private Cancion cancionActual;
    private boolean reproduciendo;
    private double volumen = 0.7; // Volumen por defecto (70%)

    /**
     * Carga una cancion para reproducir.
     * @param cancion La cancion a cargar
     */
    public void cargarCancion(Cancion cancion) {
        if (cancion == null || cancion.getRutaArchivo() == null) {
            return;
        }
        
        this.cancionActual = cancion;
        File file = new File(cancion.getRutaArchivo());
        
        // Verificar que el archivo existe
        if (!file.exists()) {
            System.err.println("El archivo no existe: " + cancion.getRutaArchivo());
            return;
        }
        
        try {
            // Crear un nuevo Media y MediaPlayer
            Media media = new Media(file.toURI().toString());
            
            // Liberar recursos del reproductor anterior si existe
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(volumen);
            reproduciendo = false;
            
        } catch (Exception e) {
            System.err.println("Error al cargar el archivo de audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Alterna entre reproducir y pausar la cancion actual.
     */
    public void playPause() {
        if (mediaPlayer == null) {
            return;
        }
        
        if (reproduciendo) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
        reproduciendo = !reproduciendo;
    }

    /**
     * Detiene la reproduccion actual.
     */
    public void detener() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            reproduciendo = false;
        }
    }

    /**
     * Establece el volumen del reproductor.
     * @param volumen Valor entre 0.0 (silencio) y 1.0 (volumen maximo)
     */
    public void setVolumen(double volumen) {
        this.volumen = Math.max(0.0, Math.min(1.0, volumen));
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(this.volumen);
        }
    }

    /**
     * Obtiene el volumen actual del reproductor.
     * @return Volumen actual (0.0 a 1.0)
     */
    public double getVolumen() {
        return volumen;
    }

    /**
     * Obtiene el tiempo actual de reproduccion en segundos.
     * @return Tiempo actual en segundos
     */
    public double getTiempoActual() {
        return mediaPlayer != null ? mediaPlayer.getCurrentTime().toSeconds() : 0;
    }

    /**
     * Obtiene la duracion total de la cancion actual en segundos.
     * @return Duracion total en segundos
     */
    public double getDuracionTotal() {
        return mediaPlayer != null ? mediaPlayer.getTotalDuration().toSeconds() : 0;
    }

    /**
     * Establece el tiempo de reproduccion actual.
     * @param segundos Tiempo en segundos al que se desea saltar
     */
    public void seek(double segundos) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(segundos));
        }
    }

    /**
     * Establece una accion a ejecutar cuando termine la cancion actual.
     * @param action Accion a ejecutar
     */
    public void setOnEndOfMedia(Runnable action) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnEndOfMedia(action);
        }
    }

    /**
     * Libera los recursos del reproductor.
     */
    public void limpiar() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        cancionActual = null;
        reproduciendo = false;
    }

    /**
     * Obtiene la cancion que se esta reproduciendo actualmente.
     * @return Cancion actual o null si no hay ninguna
     */
    public Cancion getCancionActual() {
        return cancionActual;
    }

    /**
     * Verifica si hay una cancion reproduciendose actualmente.
     * @return true si hay una cancion en reproduccion, false en caso contrario
     */
    public boolean isReproduciendo() {
        return reproduciendo;
    }
}
