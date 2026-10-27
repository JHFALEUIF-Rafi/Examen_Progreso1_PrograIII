package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorImpresionesContarTest {
    @Test
    void contarEnGestorNuevoDebeRetornarCero() {
        assertEquals(0, new GestorImpresiones(10).contarTrabajosPendientes());
    }

    @Test
    void contarDebeIncrementarsePorCadaRegistroAceptado() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        for (int i = 1; i <= 10; i++) {
            assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-" + i, "ESTUDIANTE", "Documento.pdf", 11 - i)));
            assertEquals(i, gestor.contarTrabajosPendientes());
        }
    }

    @Test
    void contarNoDebeCambiarPorConsultasListadosORegistrosRechazados() {
        GestorImpresiones gestor = new GestorImpresiones(1);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 1)));
        assertNotNull(gestor.consultarSiguienteTrabajo());
        assertEquals(1, gestor.listarTrabajos().size());
        assertTrue(gestor.existeTrabajo("IMP-001"));
        assertFalse(gestor.registrarTrabajo(null));
        assertFalse(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Duplicado", 2)));
        assertFalse(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Sin espacio", 1)));
        assertEquals(1, gestor.contarTrabajosPendientes());
    }

    @Test
    void contarDebeDecrementarseHastaCeroAlProcesar() {
        GestorImpresiones gestor = new GestorImpresiones(3);
        for (int i = 1; i <= 3; i++) {
            assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-" + i, "ESTUDIANTE", "Documento.pdf", i)));
        }
        for (int restantes = 2; restantes >= 0; restantes--) {
            assertNotNull(gestor.procesarSiguienteTrabajo());
            assertEquals(restantes, gestor.contarTrabajosPendientes());
        }
        assertNull(gestor.procesarSiguienteTrabajo());
        assertEquals(0, gestor.contarTrabajosPendientes());
    }
}
