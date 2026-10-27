package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorImpresionesEspaciosTest {
    @Test
    void espaciosInicialesDebenCoincidirConCapacidadConfigurada() {
        assertEquals(10, new GestorImpresiones(10).consultarEspaciosDisponibles());
        assertEquals(3, new GestorImpresiones(3).consultarEspaciosDisponibles());
    }

    @Test
    void espaciosDebenDisminuirHastaCeroYNoVolverseNegativos() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        for (int i = 1; i <= 10; i++) {
            assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-" + i, "ESTUDIANTE", "Documento.pdf", i)));
            assertEquals(10 - i, gestor.consultarEspaciosDisponibles());
        }
        assertFalse(gestor.registrarTrabajo(new TrabajoImpresion("IMP-11", "ESTUDIANTE", "Sin espacio", 1)));
        assertEquals(0, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void espaciosDebenRecuperarseTrasProcesarYReutilizarse() {
        GestorImpresiones gestor = new GestorImpresiones(2);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 2)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertEquals(0, gestor.consultarEspaciosDisponibles());
        assertNotNull(gestor.procesarSiguienteTrabajo());
        assertEquals(1, gestor.consultarEspaciosDisponibles());
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ESTUDIANTE", "Tres", 3)));
        assertEquals(0, gestor.consultarEspaciosDisponibles());
        assertNotNull(gestor.procesarSiguienteTrabajo());
        assertNotNull(gestor.procesarSiguienteTrabajo());
        assertEquals(2, gestor.consultarEspaciosDisponibles());
        assertNull(gestor.procesarSiguienteTrabajo());
        assertEquals(2, gestor.consultarEspaciosDisponibles());
    }

    @Test
    void dosPendientesDeDiezDebenDejarOchoEspacios() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 4)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertEquals(8, gestor.consultarEspaciosDisponibles());
        assertNotNull(gestor.consultarSiguienteTrabajo());
        assertEquals(2, gestor.listarTrabajos().size());
        assertEquals(8, gestor.consultarEspaciosDisponibles());
    }
}
