package com.matga.proyecto_reproductor_music.modelo;

import com.matga.proyecto_reproductor_music.estructuras.ListaDoblementeEnlazadaCircular;
import java.util.Iterator;

/**
 * Clase que representa una lista de reproduccion de canciones.
 * Utiliza una lista doblemente enlazada circular para almacenar las canciones.
 */
public class Playlist implements Iterable<Cancion> {
    private String nombre;
    private ListaDoblementeEnlazadaCircular<Cancion> canciones;
    private int cancionActualIndex;

    /**
     * Crea una nueva playlist con un nombre.
     * @param nombre Nombre de la playlist
     */
    public Playlist(String nombre) {
        this.nombre = nombre != null ? nombre : "Nueva Playlist";
        this.canciones = new ListaDoblementeEnlazadaCircular<>();
        this.cancionActualIndex = -1; // -1 indica que no hay cancion actual
    }

    /**
     * Agrega una cancion al final de la playlist.
     * @param cancion Cancion a agregar
     */
    public void agregarCancion(Cancion cancion) {
        if (cancion != null) {
            canciones.agregarAlFinal(cancion);
            if (cancionActualIndex == -1) {
                cancionActualIndex = 0;
            }
        }
    }

    /**
     * Elimina una cancion de la playlist.
     * @param cancion Cancion a eliminar
     * @return true si se elimino la cancion, false en caso contrario
     */
    public boolean eliminarCancion(Cancion cancion) {
        if (cancion == null) return false;
        
        boolean eliminada = canciones.eliminar(cancion);
        
        // Ajustar el indice de la cancion actual si es necesario
        if (canciones.estaVacia()) {
            cancionActualIndex = -1;
        } else if (cancionActualIndex >= canciones.tamano()) {
            cancionActualIndex = 0;
        }
        
        return eliminada;
    }

    /**
     * Obtiene la cancion actual en reproduccion.
     * @return La cancion actual o null si la playlist esta vacia
     */
    public Cancion getCancionActual() {
        if (cancionActualIndex == -1 || canciones.estaVacia()) {
            return null;
        }
        return canciones.obtener(cancionActualIndex);
    }

    /**
     * Avanza a la siguiente cancion en la playlist.
     * @return La siguiente cancion o null si no hay canciones
     */
    public Cancion siguiente() {
        if (canciones.estaVacia()) {
            cancionActualIndex = -1;
            return null;
        }
        
        cancionActualIndex = (cancionActualIndex + 1) % canciones.tamano();
        return canciones.obtener(cancionActualIndex);
    }

    /**
     * Retrocede a la cancion anterior en la playlist.
     * @return La cancion anterior o null si no hay canciones
     */
    public Cancion anterior() {
        if (canciones.estaVacia()) {
            cancionActualIndex = -1;
            return null;
        }
        
        cancionActualIndex = (cancionActualIndex - 1 + canciones.tamano()) % canciones.tamano();
        return canciones.obtener(cancionActualIndex);
    }

    /**
     * Reproduce una cancion especifica de la playlist.
     * @param index Indice de la cancion a reproducir
     * @return La cancion seleccionada o null si el indice es invalido
     */
    public Cancion reproducir(int index) {
        if (index >= 0 && index < canciones.tamano()) {
            cancionActualIndex = index;
            return getCancionActual();
        }
        return null;
    }

    /**
     * Obtiene el nombre de la playlist.
     * @return Nombre de la playlist
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece un nuevo nombre para la playlist.
     * @param nombre Nuevo nombre para la playlist
     */
    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre.trim();
        }
    }

    /**
     * Obtiene el numero total de canciones en la playlist.
     * @return Numero de canciones en la playlist
     */
    public int getTotalCanciones() {
        return canciones.tamano();
    }

    /**
     * Verifica si la playlist esta vacia.
     * @return true si la playlist no tiene canciones, false en caso contrario
     */
    public boolean estaVacia() {
        return canciones.estaVacia();
    }

    /**
     * Obtiene el indice de la cancion actual en reproduccion.
     * @return Indice de la cancion actual o -1 si no hay cancion actual
     */
    public int getIndiceCancionActual() {
        return cancionActualIndex;
    }
    
    /**
     * Obtiene el indice de la cancion actual en reproduccion.
     * @return Indice de la cancion actual o -1 si no hay cancion actual
     */
    public int getCancionActualIndex() {
        return cancionActualIndex;
    }

    @Override
    public Iterator<Cancion> iterator() {
        return canciones.iterator();
    }

    @Override
    public String toString() {
        return String.format("%s (%d canciones)", nombre, getTotalCanciones());
    }
}
