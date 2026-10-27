package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GestorImpresionesListarTest {
    @Test
    void listarConColaVaciaDebeRetornarListaVaciaNoNula() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertNotNull(gestor.listarTrabajos());
        assertTrue(gestor.listarTrabajos().isEmpty());
    }

    @Test
    void listarDebeOrdenarTodaLaListaPorPrioridadSinModificarLaCola() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        // Detecta tambien la iteracion directa (no ordenada) de PriorityQueue.
        int[] prioridades = {5, 1, 4, 2, 3};
        for (int prioridad : prioridades) {
            assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-" + prioridad, "ESTUDIANTE", "Documento.pdf", prioridad)));
        }
        for (int consulta = 0; consulta < 2; consulta++) {
            List<TrabajoImpresion> lista = gestor.listarTrabajos();
            assertNotNull(lista);
            assertEquals(5, lista.size());
            for (int i = 0; i < lista.size(); i++) {
                assertEquals(i + 1, lista.get(i).getPrioridad());
                assertEquals("IMP-" + (i + 1), lista.get(i).getCodigo());
            }
            assertEquals(5, gestor.contarTrabajosPendientes());
            assertEquals(5, gestor.consultarEspaciosDisponibles());
            assertEquals("IMP-1", gestor.consultarSiguienteTrabajo().getCodigo());
        }
        for (int prioridad = 1; prioridad <= 5; prioridad++) {
            assertEquals("IMP-" + prioridad, gestor.procesarSiguienteTrabajo().getCodigo());
        }
    }

    @Test
    void listarEmpatesDebeIncluirTodosEnPrioridadSinImponerDesempate() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 2)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ESTUDIANTE", "Tres", 1)));
        List<TrabajoImpresion> lista = gestor.listarTrabajos();
        assertNotNull(lista);
        assertEquals(3, lista.size());
        assertEquals(1, lista.get(0).getPrioridad());
        assertEquals(1, lista.get(1).getPrioridad());
        Set<String> empatados = new HashSet<>(Arrays.asList(lista.get(0).getCodigo(), lista.get(1).getCodigo()));
        assertEquals(new HashSet<>(Arrays.asList("IMP-002", "IMP-003")), empatados);
        assertEquals("IMP-001", lista.get(2).getCodigo());
        assertEquals(2, lista.get(2).getPrioridad());
        assertEquals(3, gestor.contarTrabajosPendientes());
    }

    @Test
    void modificarLaListaDevueltaNoDebeVaciarLaCola() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 2)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        List<TrabajoImpresion> lista = gestor.listarTrabajos();
        assertNotNull(lista);
        assertEquals(2, lista.size());
        // Una copia modificable y una lista inmodificable protegen la cola.
        try {
            lista.clear();
        } catch (UnsupportedOperationException aceptable) {
            // No se exige que la lista devuelta permita cambios.
        }
        assertEquals(2, gestor.contarTrabajosPendientes());
        assertEquals(8, gestor.consultarEspaciosDisponibles());
        assertTrue(gestor.existeTrabajo("IMP-001"));
        assertTrue(gestor.existeTrabajo("IMP-002"));
        assertEquals("IMP-002", gestor.procesarSiguienteTrabajo().getCodigo());
        assertEquals("IMP-001", gestor.procesarSiguienteTrabajo().getCodigo());
    }

    @Test
    void listarNoDebeCambiarElEstadoDeLosTrabajos() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        TrabajoImpresion trabajo = new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Documento.pdf", 1);
        assertTrue(gestor.registrarTrabajo(trabajo));
        String estadoAnterior = trabajo.getEstado();
        List<TrabajoImpresion> lista = gestor.listarTrabajos();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(estadoAnterior, lista.get(0).getEstado());
        assertEquals(estadoAnterior, trabajo.getEstado());
    }

    @Test
    void listarTrasProcesarDebeExcluirAlProcesadoYConservarLosPendientes() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 3)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ESTUDIANTE", "Tres", 2)));
        assertEquals("IMP-002", gestor.procesarSiguienteTrabajo().getCodigo());
        List<TrabajoImpresion> lista = gestor.listarTrabajos();
        assertNotNull(lista);
        assertEquals(2, lista.size());
        assertEquals("IMP-003", lista.get(0).getCodigo());
        assertEquals("IMP-001", lista.get(1).getCodigo());
        assertEquals(2, gestor.contarTrabajosPendientes());
    }
}
