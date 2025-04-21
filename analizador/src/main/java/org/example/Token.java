package org.example;

public class Token {
    private final String tipo;
    private final String valor;
    private final int linea;

    public Token(String tipo, String valor, int linea) {
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public int getLinea() {
        return linea;
    }

    @Override
    public String toString() {
        return String.format("Token{tipo='%s', valor='%s', linea=%d}", tipo, valor, linea);
    }
}
