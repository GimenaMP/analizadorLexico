package org.example;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int posicion = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token actual() {
        return posicion < tokens.size() ? tokens.get(posicion) : new Token("EOF", "EOF", -1);
    }

    private void error(String mensaje) {
        throw new RuntimeException("Error en línea " + actual().getLinea() + ": " + mensaje);
    }

    public Nodo parsear() {
        Nodo raiz = new Nodo("Programa");
        while (!actual().getTipo().equals("EOF")) {
            if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("int")) {
                declaracion(raiz);
            } else if (actual().getTipo().equals("IDENTIFICADOR")) {
                asignacion(raiz);
            } else if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("if")) {
                seleccion(raiz);
            } else if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("for")) {
                ciclo(raiz);
            } else {
                error("Sentencia no válida: " + actual().getValor());
            }
        }
        return raiz;
    }

    private void declaracion(Nodo nodoPadre) {
        if (!(actual().getTipo().equals("RESERVADA") && actual().getValor().equals("int"))) {
            error("Se esperaba 'int', pero se encontró: " + actual().getValor());
        }
        Nodo tipoNodo = new Nodo("int");
        nodoPadre.agregarHijo(tipoNodo);
        posicion++; // consumir 'int'

        if (!actual().getTipo().equals("IDENTIFICADOR")) {
            error("Se esperaba un identificador");
        }
        Nodo idNodo = new Nodo(actual().getValor());
        tipoNodo.agregarHijo(idNodo);
        posicion++; // consumir identificador

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals(";"))) {
            error("Se esperaba ';'");
        }
        posicion++; // consumir ';'
    }

    private void asignacion(Nodo nodoPadre) {
        if (!actual().getTipo().equals("IDENTIFICADOR")) {
            error("Se esperaba un identificador");
        }
        Nodo idNodo = new Nodo(actual().getValor());
        nodoPadre.agregarHijo(idNodo);
        posicion++; // consumir identificador

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals("="))) {
            error("Se esperaba '='");
        }
        posicion++; // consumir '='

        if (!actual().getTipo().equals("CONSTANTE") && !actual().getTipo().equals("IDENTIFICADOR")) {
            error("Se esperaba una constante o identificador");
        }
        Nodo valorNodo = new Nodo(actual().getValor());
        idNodo.agregarHijo(valorNodo);
        posicion++; // consumir valor

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals(";"))) {
            error("Se esperaba ';' al final de la asignación");
        }
        posicion++; // consumir ';'
    }

    private void seleccion(Nodo nodoPadre) {
        if (!(actual().getTipo().equals("RESERVADA") && actual().getValor().equals("if"))) {
            error("Se esperaba 'if'");
        }
        Nodo ifNodo = new Nodo("if");
        nodoPadre.agregarHijo(ifNodo);
        posicion++; // consumir 'if'

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals("("))) {
            error("Se esperaba '('");
        }
        posicion++; // consumir '('

        if (!actual().getTipo().equals("IDENTIFICADOR")) {
            error("Se esperaba una condición (identificador)");
        }
        ifNodo.agregarHijo(new Nodo(actual().getValor()));
        posicion++; // consumir condición

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals(")"))) {
            error("Se esperaba ')'");
        }
        posicion++; // consumir ')'

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals("{"))) {
            error("Se esperaba '{'");
        }
        posicion++; // consumir '{'

        while (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals("}"))) {
            if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("int")) {
                declaracion(ifNodo);
            } else if (actual().getTipo().equals("IDENTIFICADOR")) {
                asignacion(ifNodo);
            } else if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("if")) {
                seleccion(ifNodo);
            } else if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("for")) {
                ciclo(ifNodo);
            } else {
                error("Sentencia no válida dentro de 'if'");
            }
        }

        posicion++; // consumir '}'
    }

    private void ciclo(Nodo nodoPadre) {
        if (!(actual().getTipo().equals("RESERVADA") && actual().getValor().equals("for"))) {
            error("Se esperaba 'for'");
        }
        Nodo forNodo = new Nodo("for");
        nodoPadre.agregarHijo(forNodo);
        posicion++; // consumir 'for'

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals("("))) {
            error("Se esperaba '(' después de 'for'");
        }
        posicion++; // consumir '('

        // Inicialización
        if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("int")) {
            declaracion(forNodo);
        } else if (actual().getTipo().equals("IDENTIFICADOR")) {
            asignacion(forNodo);
        } else {
            error("Inicialización inválida en 'for'");
        }

        // Condición
        if (!actual().getTipo().equals("IDENTIFICADOR")) {
            error("Se esperaba una condición en el 'for'");
        }
        forNodo.agregarHijo(new Nodo(actual().getValor()));
        posicion++; // consumir condición

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals(";"))) {
            error("Se esperaba ';' después de la condición");
        }
        posicion++; // consumir ';'

        // Actualización
        if (!actual().getTipo().equals("IDENTIFICADOR")) {
            error("Se esperaba una actualización (identificador)");
        }
        Nodo actualizacion = new Nodo(actual().getValor());
        forNodo.agregarHijo(actualizacion);
        posicion++; // consumir identificador

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals(")"))) {
            error("Se esperaba ')' después de la actualización");
        }
        posicion++; // consumir ')'

        if (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals("{"))) {
            error("Se esperaba '{' para iniciar el bloque del 'for'");
        }
        posicion++; // consumir '{'

        while (!(actual().getTipo().equals("SIMBOLO") && actual().getValor().equals("}"))) {
            if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("int")) {
                declaracion(forNodo);
            } else if (actual().getTipo().equals("IDENTIFICADOR")) {
                asignacion(forNodo);
            } else if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("if")) {
                seleccion(forNodo);
            } else if (actual().getTipo().equals("RESERVADA") && actual().getValor().equals("for")) {
                ciclo(forNodo);
            } else {
                error("Sentencia no válida dentro del 'for'");
            }
        }

        posicion++; // consumir '}'
    }
}
