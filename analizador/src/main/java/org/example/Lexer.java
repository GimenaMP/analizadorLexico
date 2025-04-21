package org.example;

import java.util.*;
import java.util.regex.*;

public class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    private final List<String> errores = new ArrayList<>();
    private final String input;
    private int lineNumber = 1;

    private static final Pattern TOKEN_PATTERNS = Pattern.compile(
            "(?<WHITESPACE>\\s+)|" +
                    "(?<COMENTARIO>//.*)|" +
                    "(?<RESERVADA>\\b(if|else|for|print|int|bfhjk[a-zA-Z0-9]*)\\b)|" +
                    "(?<ASIGNACION>:=)|" +
                    "(?<RELACIONAL>>=|<=|>|<|=|<>)|" +
                    "(?<ARITMETICO>[+\\-*/])|" +
                    "(?<ENTERO>\\b(0|100|[1-9][0-9]?)\\b)|" +
                    "(?<CADENA>\"bfhjk\")|" +
                    "(?<SIMBOLO>[{}()\\[\\],;])|" +
                    "(?<IDENTIFICADOR>\\b[a-zA-Z][a-zA-Z0-9]{0,14}\\b)|" +
                    "(?<ERRORIDENTIFICADOR>\\b[^a-zA-Z][a-zA-Z0-9]*|[a-zA-Z][a-zA-Z0-9]{15,}\\b)|" +
                    "(?<ERRORNUMERO>\\b-?[1-9][0-9]*|10[1-9]|[1-9][0-9]{2,}\\b)"
    );

    public Lexer(String input) {
        this.input = input;
    }

    public void tokenize() {
        String[] lineas = input.split("\\n");

        for (int i = 0; i < lineas.length; i++) {
            Matcher matcher = TOKEN_PATTERNS.matcher(lineas[i]);
            lineNumber = i + 1;

            while (matcher.find()) {
                if (matcher.group("WHITESPACE") != null || matcher.group("COMENTARIO") != null) {
                    continue;
                }

                if (matcher.group("RESERVADA") != null) {
                    String valor = matcher.group();
                    // Validar si la palabra reservada está en la lista permitida
                    if (valor.equals("if") || valor.equals("else") || valor.equals("for") || valor.equals("print") || valor.equals("int")) {
                        tokens.add(new Token("Palabra reservada", valor, lineNumber));
                    } else {
                        errores.add("Línea " + lineNumber + ": Palabra reservada no admitida: " + valor);
                    }
                } else if (matcher.group("ASIGNACION") != null) {
                    tokens.add(new Token("Operador asignación", matcher.group(), lineNumber));
                } else if (matcher.group("RELACIONAL") != null) {
                    tokens.add(new Token("Operador relacional", matcher.group(), lineNumber));
                } else if (matcher.group("ARITMETICO") != null) {
                    tokens.add(new Token("Operador aritmético", matcher.group(), lineNumber));
                } else if (matcher.group("ENTERO") != null) {
                    tokens.add(new Token("Constante entera", matcher.group(), lineNumber));
                } else if (matcher.group("CADENA") != null) {
                    tokens.add(new Token("Cadena BFHJK", matcher.group(), lineNumber));
                } else if (matcher.group("SIMBOLO") != null) {
                    tokens.add(new Token("Símbolo", matcher.group(), lineNumber));
                } else if (matcher.group("IDENTIFICADOR") != null) {
                    tokens.add(new Token("Identificador", matcher.group(), lineNumber));
                } else if (matcher.group("ERRORIDENTIFICADOR") != null) {
                    String error = "Línea " + lineNumber + ": Identificador inválido: " + matcher.group();
                    errores.add(error);
                } else if (matcher.group("ERRORNUMERO") != null) {
                    String error = "Línea " + lineNumber + ": Número inválido: " + matcher.group();
                    errores.add(error);
                } else {
                    String textoInvalido = matcher.group();
                    if (!textoInvalido.trim().isEmpty()) {
                        String error = "Línea " + lineNumber + ": Carácter inválido: '" + textoInvalido + "'";
                        errores.add(error);
                    }
                }
            }
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void imprimirErrores() {
        if (errores.isEmpty()) {
            System.out.println("No se encontraron errores léxicos.");
        } else {
            System.out.println("Errores léxicos encontrados:");
            errores.forEach(System.out::println);
        }
    }
}
