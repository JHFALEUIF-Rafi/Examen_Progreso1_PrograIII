package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GestorImpresionesProcesarTest {
    @Test
    void procesarDebeRespetarPrioridadNumericaYActualizarEstado() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Informe.pdf", 5)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "DOCENTE", "Guia.pdf", 1)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ADMINISTRATIVO", "Memoria.pdf", 3)));
        String[] codigos = {"IMP-002", "IMP-003", "IMP-001"};
        int[] prioridades = {1, 3, 5};
        for (int i = 0; i < codigos.length; i++) {
            TrabajoImpresion procesado = gestor.procesarSiguienteTrabajo();
            assertNotNull(procesado);
            assertEquals(codigos[i], procesado.getCodigo());
            assertEquals(prioridades[i], procesado.getPrioridad());
            assertEquals("PROCESADO", procesado.getEstado());
            assertFalse(gestor.existeTrabajo(codigos[i]));
            assertEquals(2 - i, gestor.contarTrabajosPendientes());
            assertEquals(8 + i, gestor.consultarEspaciosDisponibles());
        }
        assertNull(gestor.procesarSiguienteTrabajo());
        assertNull(gestor.consultarSiguienteTrabajo());
    }

    @Test
    void procesarEmpatesDebeConservarTodosSinExigirOrdenEntreIguales() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 2)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ESTUDIANTE", "Tres", 1)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-004", "ESTUDIANTE", "Cuatro", 3)));
        Set<String> empatados = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            TrabajoImpresion procesado = gestor.procesarSiguienteTrabajo();
            assertNotNull(procesado);
            assertEquals(1, procesado.getPrioridad());
            assertEquals("PROCESADO", procesado.getEstado());
            assertTrue(empatados.add(procesado.getCodigo()), "No se debe procesar dos veces el mismo codigo");
        }
        assertEquals(new HashSet<>(Arrays.asList("IMP-002", "IMP-003")), empatados);
        assertEquals("IMP-001", gestor.procesarSiguienteTrabajo().getCodigo());
        assertEquals("IMP-004", gestor.procesarSiguienteTrabajo().getCodigo());
        assertEquals(0, gestor.contarTrabajosPendientes());
    }

    @Test
    void procesarNoDebeCambiarElEstadoDeOtrosPendientes() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        TrabajoImpresion pendiente = new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 5);
        assertTrue(gestor.registrarTrabajo(pendiente));
        String estadoAnterior = pendiente.getEstado();
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertEquals("IMP-002", gestor.procesarSiguienteTrabajo().getCodigo());
        assertEquals(estadoAnterior, pendiente.getEstado());
        assertEquals(estadoAnterior, gestor.consultarSiguienteTrabajo().getEstado());
        assertEquals("IMP-001", gestor.consultarSiguienteTrabajo().getCodigo());
    }

    @Test
    void procesarConColaVaciaDebeRetornarNullSinAlterarContadores() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertNull(gestor.procesarSiguienteTrabajo());
        assertNull(gestor.procesarSiguienteTrabajo());
        assertEquals(0, gestor.contarTrabajosPendientes());
        assertEquals(10, gestor.consultarEspaciosDisponibles());
    }
}
