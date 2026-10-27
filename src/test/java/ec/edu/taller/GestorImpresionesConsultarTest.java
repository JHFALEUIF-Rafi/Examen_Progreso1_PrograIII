package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorImpresionesConsultarTest {
    @Test
    void consultarDebeElegirElMenorNumeroDePrioridadAunqueLlegueDespues() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Informe.pdf", 5)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "DOCENTE", "Guia.pdf", 1)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ADMINISTRATIVO", "Memoria.pdf", 3)));
        TrabajoImpresion siguiente = gestor.consultarSiguienteTrabajo();
        assertNotNull(siguiente);
        assertEquals("IMP-002", siguiente.getCodigo());
        assertEquals(1, siguiente.getPrioridad());
        assertEquals(3, gestor.contarTrabajosPendientes());
        assertEquals(7, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void consultarRepetidamenteNoDebeEliminarNiProcesar() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        TrabajoImpresion trabajo = new TrabajoImpresion("IMP-001", "DOCENTE", "Guia.pdf", 1);
        assertTrue(gestor.registrarTrabajo(trabajo));
        String estadoAnterior = trabajo.getEstado();
        assertEquals("IMP-001", gestor.consultarSiguienteTrabajo().getCodigo());
        assertEquals("IMP-001", gestor.consultarSiguienteTrabajo().getCodigo());
        assertEquals(estadoAnterior, trabajo.getEstado());
        assertEquals(estadoAnterior, gestor.consultarSiguienteTrabajo().getEstado());
        assertEquals(1, gestor.contarTrabajosPendientes());
        assertTrue(gestor.existeTrabajo("IMP-001"));
    }

    @Test
    void consultarConColaVaciaDebeRetornarNullSinCambiarEspacios() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertNull(gestor.consultarSiguienteTrabajo());
        assertNull(gestor.consultarSiguienteTrabajo());
        assertEquals(0, gestor.contarTrabajosPendientes());
        assertEquals(10, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void consultarDebeActualizarElSiguienteTrasRegistroYProcesamiento() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 4)));
        assertEquals("IMP-001", gestor.consultarSiguienteTrabajo().getCodigo());
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertEquals("IMP-002", gestor.consultarSiguienteTrabajo().getCodigo());
        assertEquals("IMP-002", gestor.procesarSiguienteTrabajo().getCodigo());
        assertEquals("IMP-001", gestor.consultarSiguienteTrabajo().getCodigo());
    }
}
