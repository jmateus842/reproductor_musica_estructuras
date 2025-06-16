package com.tests;

import com.matga.proyecto_reproductor_music.estructuras.ListaDoblementeEnlazadaCircular;

/**
 * Clase de prueba para la implementacion de ListaDoblementeEnlazadaCircular.
 */
public class PruebaListaDoblementeEnlazadaCircular {
    
    public static void main(String[] args) {
        System.out.println("=== Prueba de Lista Doblemente Enlazada Circular ===\n");
        
        // Crear una nueva lista
        ListaDoblementeEnlazadaCircular<String> lista = new ListaDoblementeEnlazadaCircular<>();
        
        // Prueba 1: Lista vacia
        System.out.println("--- Prueba 1: Lista vacia ---");
        System.out.println("Lista: " + lista);
        System.out.println("Tamano: " + lista.tamano());
        System.out.println("Esta vacia? " + lista.estaVacia());
        
        // Prueba 2: Agregar elementos al final
        System.out.println("\n--- Prueba 2: Agregar elementos al final ---");
        lista.agregarAlFinal("Primero");
        lista.agregarAlFinal("Segundo");
        lista.agregarAlFinal("Tercero");
        
        System.out.println("Lista despues de agregar 3 elementos: " + lista);
        System.out.println("Tamano: " + lista.tamano());
        System.out.println("Esta vacia? " + lista.estaVacia());
        
        // Prueba 3: Agregar elemento al inicio
        System.out.println("\n--- Prueba 3: Agregar elemento al inicio ---");
        lista.agregarAlInicio("Cero");
        System.out.println("Lista despues de agregar al inicio: " + lista);
        
        // Prueba 4: Obtener elementos por indice
        System.out.println("\n--- Prueba 4: Obtener elementos por indice ---");
        System.out.println("Elemento en indice 0: " + lista.obtener(0));
        System.out.println("Elemento en indice 2: " + lista.obtener(2));
        
        // Prueba 5: Verificar si contiene elementos
        System.out.println("\n--- Prueba 5: Verificar si contiene elementos ---");
        System.out.println("Contiene \"Segundo\"? " + lista.contiene("Segundo"));
        System.out.println("Contiene \"Cuarto\"? " + lista.contiene("Cuarto"));
        
        // Prueba 6: Eliminar elemento
        System.out.println("\n--- Prueba 6: Eliminar elemento ---");
        System.out.println("Eliminando \"Segundo\"...");
        boolean eliminado = lista.eliminar("Segundo");
        System.out.println("Eliminado? " + eliminado);
        System.out.println("Lista despues de eliminar: " + lista);
        
        // Prueba 7: Iterar sobre la lista
        System.out.println("\n--- Prueba 7: Iterar sobre la lista ---");
        System.out.print("Elementos: ");
        for (String elemento : lista) {
            System.out.print(elemento + " ");
        }
        System.out.println("\n");
        
        System.out.println("=== Fin de las pruebas ===");
    }
}
