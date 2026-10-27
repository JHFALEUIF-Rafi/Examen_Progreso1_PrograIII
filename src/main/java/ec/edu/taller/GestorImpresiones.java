package ec.edu.taller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class GestorImpresiones {
    private Queue<TrabajoImpresion> colaTrabajos;
    private int capacidadMaxima;

    public GestorImpresiones(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.colaTrabajos = new PriorityQueue<>(Comparator.comparingInt(TrabajoImpresion::getPrioridad));
    }

    public boolean registrarTrabajo(TrabajoImpresion trabajo) {
        if (trabajo == null) {
            return false;
        }

        if(trabajo.getPrioridad() <= 0){
            return false;
        }

        String codigo = trabajo.getCodigo();

        if (codigo == null || codigo.isBlank()) {
            return false;
        }
        if (existeTrabajo(codigo)) {
            return false;
        }
        if (colaTrabajos.size() >= capacidadMaxima) {
            return false;
        }
        return colaTrabajos.offer(trabajo);
    }

    public boolean existeTrabajo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            return false;
        }
        for (TrabajoImpresion t : colaTrabajos) {
            if (codigo.equals(t.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    public TrabajoImpresion consultarSiguienteTrabajo() {
        return colaTrabajos.peek();
    }

    public TrabajoImpresion procesarSiguienteTrabajo() {
        TrabajoImpresion trabajo = colaTrabajos.poll();
        if (trabajo != null) {
            trabajo.setEstado("PROCESADO");
        }
        return trabajo;
    }

    public int contarTrabajosPendientes() {
        return colaTrabajos.size();
    }

    public int consultarEspaciosDisponibles() {
        return capacidadMaxima - colaTrabajos.size();
    }

    public List<TrabajoImpresion> listarTrabajos() {
        PriorityQueue<TrabajoImpresion> copia = new PriorityQueue<>(
                Comparator.comparingInt(TrabajoImpresion::getPrioridad));
        copia.addAll(colaTrabajos);
        List<TrabajoImpresion> lista = new ArrayList<>();
        while (!copia.isEmpty()) {
            lista.add(copia.poll());
        }
        return lista;
    }
}
