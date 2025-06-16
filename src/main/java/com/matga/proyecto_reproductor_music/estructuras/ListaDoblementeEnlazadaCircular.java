package com.matga.proyecto_reproductor_music.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementacion de una lista doblemente enlazada circular.
 * @param <T> Tipo de elementos que contendra la lista
 */
public class ListaDoblementeEnlazadaCircular<T> implements Iterable<T> {
    private Nodo<T> cabeza;
    private int tamano;
    
    /**
     * Crea una nueva lista doblemente enlazada circular vacia.
     */
    public ListaDoblementeEnlazadaCircular() {
        this.cabeza = null;
        this.tamano = 0;
    }
    
    /**
     * Verifica si la lista esta vacia.
     * @return true si la lista esta vacia, false en caso contrario
     */
    public boolean estaVacia() {
        return cabeza == null;
    }
    
    /**
     * Obtiene el tamano de la lista.
     * @return el numero de elementos en la lista
     */
    public int tamano() {
        return tamano;
    }
    
    /**
     * Agrega un elemento al final de la lista.
     * @param dato El dato a agregar
     */
    public void agregarAlFinal(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        
        if (estaVacia()) {
            cabeza = nuevoNodo;
        } else {
            Nodo<T> ultimo = cabeza.getAnterior();
            
            nuevoNodo.setSiguiente(cabeza);
            nuevoNodo.setAnterior(ultimo);
            
            ultimo.setSiguiente(nuevoNodo);
            cabeza.setAnterior(nuevoNodo);
        }
        
        tamano++;
    }
    
    /**
     * Agrega un elemento al inicio de la lista.
     * @param dato El dato a agregar
     */
    public void agregarAlInicio(T dato) {
        agregarAlFinal(dato);
        cabeza = cabeza.getAnterior(); // La nueva cabeza es el ultimo nodo agregado
    }
    
    /**
     * Obtiene el elemento en la posicion especificada.
     * @param indice El indice del elemento a obtener (0-based)
     * @return El elemento en la posicion especificada
     * @throws IndexOutOfBoundsException si el indice esta fuera de rango
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
        
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        
        return actual.getDato();
    }
    
    /**
     * Elimina la primera ocurrencia del dato especificado.
     * @param dato El dato a eliminar
     * @return true si se elimino el elemento, false en caso contrario
     */
    public boolean eliminar(T dato) {
        if (estaVacia()) {
            return false;
        }
        
        Nodo<T> actual = cabeza;
        boolean encontrado = false;
        
        // Buscar el nodo a eliminar
        for (int i = 0; i < tamano; i++) {
            if ((dato == null && actual.getDato() == null) || 
                (dato != null && dato.equals(actual.getDato()))) {
                encontrado = true;
                break;
            }
            actual = actual.getSiguiente();
        }
        
        if (!encontrado) {
            return false;
        }
        
        // Si solo hay un nodo
        if (tamano == 1) {
            cabeza = null;
        } else {
            // Si el nodo a eliminar es la cabeza
            if (actual == cabeza) {
                cabeza = cabeza.getSiguiente();
            }
            
            // Reorganizar referencias
            Nodo<T> anterior = actual.getAnterior();
            Nodo<T> siguiente = actual.getSiguiente();
            
            anterior.setSiguiente(siguiente);
            siguiente.setAnterior(anterior);
        }
        
        tamano--;
        return true;
    }
    
    /**
     * Verifica si la lista contiene el elemento especificado.
     * @param dato El dato a buscar
     * @return true si la lista contiene el elemento, false en caso contrario
     */
    public boolean contiene(T dato) {
        if (estaVacia()) {
            return false;
        }
        
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamano; i++) {
            if ((dato == null && actual.getDato() == null) || 
                (dato != null && dato.equals(actual.getDato()))) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        
        return false;
    }
    
    /**
     * Obtiene el primer nodo de la lista.
     * @return El primer nodo de la lista, o null si la lista esta vacia
     */
    public Nodo<T> getCabeza() {
        return cabeza;
    }
    
    @Override
    public String toString() {
        if (estaVacia()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        
        for (int i = 0; i < tamano; i++) {
            sb.append(actual.getDato());
            if (i < tamano - 1) {
                sb.append(", ");
            }
            actual = actual.getSiguiente();
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Retorna un iterador sobre los elementos de la lista.
     * @return un iterador sobre los elementos de la lista
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorListaCircular();
    }
    
    /**
     * Implementacion del iterador para la lista doblemente enlazada circular.
     */
    private class IteradorListaCircular implements Iterator<T> {
        private Nodo<T> actual;
        private int contador;
        
        public IteradorListaCircular() {
            this.actual = cabeza;
            this.contador = 0;
        }
        
        @Override
        public boolean hasNext() {
            return tamano > 0 && contador < tamano;
        }
        
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay mas elementos en la lista");
            }
            
            T dato = actual.getDato();
            actual = actual.getSiguiente();
            contador++;
            return dato;
        }
    }
}
