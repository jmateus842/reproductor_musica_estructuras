package com.tests;

import com.matga.proyecto_reproductor_music.modelo.Cancion;
import com.matga.proyecto_reproductor_music.modelo.Playlist;

/**
 * Clase de prueba para las clases Cancion y Playlist.
 * Realiza pruebas basicas de funcionalidad.
 */
public class PruebaReproductorMusica {
    
    public static void main(String[] args) {
        System.out.println("=== PRUEBAS DEL REPRODUCTOR DE MUSICA ===\n");
        
        // Pruebas de la clase Cancion
        System.out.println("=== PRUEBAS DE LA CLASE CANCION ===");
        probarClaseCancion();
        
        // Pruebas de la clase Playlist
        System.out.println("\n=== PRUEBAS DE LA CLASE PLAYLIST ===");
        probarClasePlaylist();
    }
    
    /**
     * Pruebas para la clase Cancion.
     */
    private static void probarClaseCancion() {
        System.out.println("\nCreando canciones de prueba...");
        
        // Crear algunas canciones de prueba (solo titulo y ruta)
        Cancion cancion1 = new Cancion("Bohemian Rhapsody.mp3", "ruta/bohemian.mp3");
        Cancion cancion2 = new Cancion("Billie Jean.mp3", "ruta/billie.mp3");
        Cancion cancion3 = new Cancion("Sweet Child O'Mine.mp3", "ruta/sweet.mp3");
        
        // Probar metodos de la clase Cancion
        System.out.println("\nInformacion de las canciones:");
        System.out.println("1. " + cancion1);
        System.out.println("2. " + cancion2);
        System.out.println("3. " + cancion3);
        
        // Probar metodos getters
        System.out.println("\nDetalles de la primera cancion:");
        System.out.println("Titulo: " + cancion1.getTitulo());
        System.out.println("Ruta: " + cancion1.getRutaArchivo());
        
        System.out.println("\nPrueba de Cancion completada exitosamente!");
    }
    
    /**
     * Pruebas para la clase Playlist.
     */
    private static void probarClasePlaylist() {
        System.out.println("\nCreando playlist de prueba...");
        
        // Crear una nueva playlist
        Playlist miPlaylist = new Playlist("Mis Favoritas");
        System.out.println("Playlist creada: " + miPlaylist);
        
        // Crear algunas canciones para la playlist (solo titulo y ruta)
        Cancion cancion1 = new Cancion("Bohemian Rhapsody.mp3", "/ruta/bohemian.mp3");
        Cancion cancion2 = new Cancion("Billie Jean.mp3", "/ruta/billie.mp3");
        Cancion cancion3 = new Cancion("Sweet Child O'Mine.mp3", "/ruta/sweet.mp3");
        
        // Agregar canciones a la playlist
        System.out.println("\nAgregando canciones a la playlist...");
        miPlaylist.agregarCancion(cancion1);
        miPlaylist.agregarCancion(cancion2);
        miPlaylist.agregarCancion(cancion3);
        
        // Mostrar informacion de la playlist
        System.out.println("Playlist actualizada: " + miPlaylist);
        System.out.println("Total de canciones: " + miPlaylist.getTotalCanciones());
        
        // Probar navegacion entre canciones
        System.out.println("\nProbando navegacion de canciones:");
        System.out.println("Cancion actual: " + miPlaylist.getCancionActual());
        
        System.out.println("Siguiente cancion: " + miPlaylist.siguiente());
        System.out.println("Siguiente cancion: " + miPlaylist.siguiente());
        System.out.println("Siguiente cancion (debe volver al inicio): " + miPlaylist.siguiente());
        
        System.out.println("\nProbando retroceder:");
        System.out.println("Anterior cancion: " + miPlaylist.anterior());
        System.out.println("Anterior cancion: " + miPlaylist.anterior());
        System.out.println("Anterior cancion (debe ir al final): " + miPlaylist.anterior());
        
        // Probar reproduccion por indice
        System.out.println("\nReproduciendo cancion en el indice 1:");
        System.out.println("Cancion seleccionada: " + miPlaylist.reproducir(1));
        
        // Probar eliminacion de cancion
        System.out.println("\nEliminando la cancion actual...");
        miPlaylist.eliminarCancion(miPlaylist.getCancionActual());
        System.out.println("Cancion actual despues de eliminar: " + miPlaylist.getCancionActual());
        System.out.println("Total de canciones restantes: " + miPlaylist.getTotalCanciones());
        
        // Probar si la playlist esta vacia
        System.out.println("\nLa playlist esta vacia? " + (miPlaylist.estaVacia() ? "Si" : "No"));
        
        // Vaciar la playlist
        System.out.println("Vaciando la playlist...");
        miPlaylist.eliminarCancion(cancion1);
        miPlaylist.eliminarCancion(cancion3);
        
        System.out.println("La playlist esta vacia ahora? " + (miPlaylist.estaVacia() ? "Si" : "No"));
        
        System.out.println("\nPrueba de Playlist completada exitosamente!");
    }
}
