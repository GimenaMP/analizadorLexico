package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Entrada de prueba
        String entradaPrueba = "int x; x := 5; if (x > 3) { print(x); }";

        // Análisis léxico (tokenización)
        List<Token> tokens = analizarLexico(entradaPrueba);

        // Imprimir la tabla de tokens
        System.out.println("Tabla de Tokens:");
        for (Token token : tokens) {
            System.out.println(token);
        }

        // Crear el parser con los tokens generados
        Parser parser = new Parser(tokens);

        // Analizar sintácticamente
        System.out.println("\nAnálisis sintáctico:");
        try {
            Nodo arbolSintactico = parser.parsear();
            System.out.println("Árbol de derivación:");
            imprimirArbol(arbolSintactico, 0); // Imprimir el árbol de derivación
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    // Método para analizar la entrada y generar los tokens
    private static List<Token> analizarLexico(String entrada) {
        Lexer lexer = new Lexer(entrada);
        lexer.tokenize(); // Generar los tokens
        return lexer.getTokens(); // Obtener la lista de tokens
    }

    // Método para imprimir el árbol de derivación (recursivamente)
    private static void imprimirArbol(Nodo nodo, int nivel) {
        for (int i = 0; i < nivel; i++) {
            System.out.print("  ");  // Imprimir espacios para la jerarquía del árbol
        }
        System.out.println(nodo.getValor());
        for (Nodo hijo : nodo.getHijos()) {
            imprimirArbol(hijo, nivel + 1);
        }
    }
}
