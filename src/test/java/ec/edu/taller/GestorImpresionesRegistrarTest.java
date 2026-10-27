package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorImpresionesRegistrarTest {
    @Test
    void registrarTrabajoValidoDebeRetornarTrue() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        TrabajoImpresion trabajo = new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Informe.pdf", 3);
        assertTrue(gestor.registrarTrabajo(trabajo));
        assertEquals(1, gestor.contarTrabajosPendientes());
        assertEquals("IMP-001", gestor.consultarSiguienteTrabajo().getCodigo());
    }

    @Test
    void registrarNullNoDebeModificarUnaColaConTrabajos() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "DOCENTE", "Guia.pdf", 2)));
        assertFalse(gestor.registrarTrabajo(null));
        assertEquals(1, gestor.contarTrabajosPendientes());
        assertEquals(9, gestor.consultarEspaciosDisponibles());
        assertEquals("IMP-001", gestor.consultarSiguienteTrabajo().getCodigo());
    }

    @Test
    void registrarCodigoNuloVacioOSoloEspaciosDebeRetornarFalse() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        for (String codigo : new String[] {null, "", "   "}) {
            assertFalse(gestor.registrarTrabajo(new TrabajoImpresion(codigo, "ESTUDIANTE", "Documento.pdf", 1)));
        }
        assertEquals(0, gestor.contarTrabajosPendientes());
        assertEquals(10, gestor.consultarEspaciosDisponibles());
        assertNull(gestor.consultarSiguienteTrabajo());
    }

    @Test
    void registrarDuplicadoPorCodigoDebeRechazarloAunqueCambienLosDatos() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Original.pdf", 5)));
        assertFalse(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "DOCENTE", "Otro.pdf", 1)));
        assertEquals(1, gestor.contarTrabajosPendientes());
        assertEquals(9, gestor.consultarEspaciosDisponibles());
        assertEquals(5, gestor.consultarSiguienteTrabajo().getPrioridad());
    }

    @Test
    void registrarDiezTrabajosDebeAceptarDiezYRechazarElOnce() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        for (int i = 1; i <= 10; i++) {
            assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-" + i, "ESTUDIANTE", "Documento.pdf", i)));
        }
        assertFalse(gestor.registrarTrabajo(new TrabajoImpresion("IMP-11", "DOCENTE", "Urgente.pdf", 1)));
        assertEquals(10, gestor.contarTrabajosPendientes());
        assertEquals(0, gestor.consultarEspaciosDisponibles());
        assertFalse(gestor.existeTrabajo("IMP-11"));
    }

    @Test
    void registrarDebeRespetarLaCapacidadIndicadaEnElConstructor() {
        GestorImpresiones gestor = new GestorImpresiones(2);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 3)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 2)));
        assertFalse(gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ESTUDIANTE", "Tres", 1)));
        assertEquals(2, gestor.contarTrabajosPendientes());
        assertEquals(0, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void registrarDespuesDeProcesarDebeReutilizarElEspacio() {
        GestorImpresiones gestor = new GestorImpresiones(1);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 3)));
        assertNotNull(gestor.procesarSiguienteTrabajo());
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertEquals(1, gestor.contarTrabajosPendientes());
        assertEquals(0, gestor.consultarEspaciosDisponibles());
        assertEquals("IMP-002", gestor.consultarSiguienteTrabajo().getCodigo());
    }
}
