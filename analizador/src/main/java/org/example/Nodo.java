package org.example;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private String valor;
    private List<Nodo> hijos;

    public Nodo(String valor) {
        this.valor = valor;
        this.hijos = new ArrayList<>();
    }

    public String getValor() {
        return valor;
    }

    public List<Nodo> getHijos() {
        return hijos;
    }

    public void agregarHijo(Nodo hijo) {
        hijos.add(hijo);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(valor);
        if (!hijos.isEmpty()) {
            sb.append(" -> [");
            for (Nodo hijo : hijos) {
                sb.append(hijo.getValor()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Eliminar la última coma
            sb.append("]");
        }
        return sb.toString();
    }
}
