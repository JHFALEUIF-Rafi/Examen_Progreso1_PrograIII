package ec.edu.taller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorImpresionesExisteTest {
    @Test
    void existeDebeEncontrarCodigosConDistintasPrioridades() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 5)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-003", "ESTUDIANTE", "Tres", 3)));
        assertTrue(gestor.existeTrabajo("IMP-001"));
        assertTrue(gestor.existeTrabajo("IMP-002"));
        assertTrue(gestor.existeTrabajo("IMP-003"));
        assertEquals(3, gestor.contarTrabajosPendientes());
        assertEquals("IMP-002", gestor.consultarSiguienteTrabajo().getCodigo());
    }

    @Test
    void existeDebeRetornarFalseEnColaVacia() {
        assertFalse(new GestorImpresiones(10).existeTrabajo("IMP-001"));
    }

    @Test
    void existeDebeRetornarFalseSiCodigoNoEstaRegistrado() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "DOCENTE", "Guia.pdf", 1)));
        assertFalse(gestor.existeTrabajo("IMP-999"));
        assertEquals(1, gestor.contarTrabajosPendientes());
    }

    @Test
    void existeDebeRechazarCodigoNuloVacioOSoloEspacios() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Documento.pdf", 1)));
        assertFalse(gestor.existeTrabajo(null));
        assertFalse(gestor.existeTrabajo(""));
        assertFalse(gestor.existeTrabajo("   "));
        assertEquals(1, gestor.contarTrabajosPendientes());
    }

    @Test
    void existeDebeDejarDeEncontrarUnTrabajoProcesado() {
        GestorImpresiones gestor = new GestorImpresiones(10);
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Uno", 5)));
        assertTrue(gestor.registrarTrabajo(new TrabajoImpresion("IMP-002", "ESTUDIANTE", "Dos", 1)));
        assertEquals("IMP-002", gestor.procesarSiguienteTrabajo().getCodigo());
        assertFalse(gestor.existeTrabajo("IMP-002"));
        assertTrue(gestor.existeTrabajo("IMP-001"));
    }
}
