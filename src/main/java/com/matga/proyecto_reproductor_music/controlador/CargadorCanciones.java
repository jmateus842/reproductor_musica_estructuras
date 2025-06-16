package com.matga.proyecto_reproductor_music.controlador;

import com.matga.proyecto_reproductor_music.modelo.Cancion;
import com.matga.proyecto_reproductor_music.modelo.Playlist;
import java.io.File;

/**
 * Clase utilitaria para cargar canciones desde la carpeta de musica.
 */
public class CargadorCanciones {
    /**
     * Escanea la carpeta de musica y agrega los archivos MP3 a una Playlist.
     * El titulo de la cancion es el nombre del archivo.
     * @param rutaCarpetaMusica Ruta absoluta o relativa a la carpeta con los MP3
     * @return Playlist con las canciones encontradas
     */
    public static Playlist cargarPlaylistDesdeCarpeta(String rutaCarpetaMusica) {
        Playlist playlist = new Playlist("Mi Playlist");
        File carpeta = new File(rutaCarpetaMusica);
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            System.out.println("No se encontro la carpeta de musica: " + rutaCarpetaMusica);
            return playlist;
        }
        File[] archivos = carpeta.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".mp3"));
        if (archivos != null) {
            for (File archivo : archivos) {
                String titulo = archivo.getName();
                String ruta = archivo.getPath();
                Cancion cancion = new Cancion(titulo, ruta);
                playlist.agregarCancion(cancion);
            }
        }
        return playlist;
    }
}
