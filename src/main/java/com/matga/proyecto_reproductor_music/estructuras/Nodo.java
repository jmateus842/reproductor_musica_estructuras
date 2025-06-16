package com.matga.proyecto_reproductor_music.estructuras;

/**
 * Clase que representa un nodo para una lista doblemente enlazada circular.
 * @param <T> Tipo de dato que almacenara el nodo
 */
public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente;
    private Nodo<T> anterior;
    
    /**
     * Crea un nuevo nodo con el dato especificado.
     * Al crearse, el nodo apunta a si mismo en ambas direcciones.
     * @param dato El dato a almacenar en el nodo
     */
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = this;
        this.anterior = this;
    }
    
    // Getters y Setters
    
    public T getDato() {
        return dato;
    }
    
    public void setDato(T dato) {
        this.dato = dato;
    }
    
    public Nodo<T> getSiguiente() {
        return siguiente;
    }
    
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
    
    public Nodo<T> getAnterior() {
        return anterior;
    }
    
    public void setAnterior(Nodo<T> anterior) {
        this.anterior = anterior;
    }
    
    @Override
    public String toString() {
        return dato != null ? dato.toString() : "null";
    }
}
