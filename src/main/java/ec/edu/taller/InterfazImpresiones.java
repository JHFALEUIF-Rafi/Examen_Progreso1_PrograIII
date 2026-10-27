package ec.edu.taller;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

public class InterfazImpresiones extends JFrame {
    static final int CAPACIDAD = 10;

    private final GestorImpresiones gestor;

    private JPanel panelPrincipal;
    private JTabbedPane pestanas;
    private JPanel panelRegistrar;
    private JPanel panelGestionar;
    private JPanel panelInferior;

    private JTextField txtCodigo;
    private JComboBox<String> cmbSolicitante;
    private JTextField txtDocumento;
    private JSpinner spnPrioridad;
    private JButton btnRegistrar;
    private JButton btnLimpiar;

    private JLabel lblValorCodigo;
    private JLabel lblValorSolicitante;
    private JLabel lblValorDocumento;
    private JLabel lblValorPrioridad;
    private JLabel lblValorEstado;
    private JTextArea txtPendientes;
    private JLabel lblPendientes;
    private JLabel lblDisponibles;
    private JLabel lblCapacidad;
    private JButton btnProcesar;
    private JButton btnConsultar;
    private JButton btnListar;

    public InterfazImpresiones(GestorImpresiones gestor) {
        super("Centro de impresion universitario");
        if (panelPrincipal == null) {
            throw new IllegalStateException("El formulario .form no fue procesado por IntelliJ.");
        }
        this.gestor = gestor;

        setContentPane(panelPrincipal);
        cmbSolicitante.setModel(new DefaultComboBoxModel<>(
                new String[] {"ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO"}));
        spnPrioridad.setModel(new SpinnerNumberModel(1, 1, 10, 1));

        btnRegistrar.addActionListener(e -> registrar());
        btnLimpiar.addActionListener(e -> limpiar());
        btnProcesar.addActionListener(e -> procesar());
        btnConsultar.addActionListener(e -> consultar());
        btnListar.addActionListener(e -> listar());
        pestanas.addChangeListener(e -> {
            if (pestanas.getSelectedIndex() == 1) {
                listar();
                actualizarContadores();
            }
        });

        cargarDatosQuemados();
        listar();
        actualizarContadores();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void cargarDatosQuemados() {
        TrabajoImpresion[] iniciales = {
            new TrabajoImpresion("IMP-001", "ESTUDIANTE", "Informe.pdf", 5),
            new TrabajoImpresion("IMP-002", "DOCENTE", "Examen.pdf", 1),
            new TrabajoImpresion("IMP-003", "ADMINISTRATIVO", "Acta.pdf", 3),
            new TrabajoImpresion("IMP-004", "ESTUDIANTE", "Tesis.pdf", 7),
            new TrabajoImpresion("IMP-005", "DOCENTE", "Silabo.pdf", 2),
            new TrabajoImpresion("IMP-006", "ADMINISTRATIVO", "Memorando.pdf", 4),
            new TrabajoImpresion("IMP-007", "ESTUDIANTE", "Deber.pdf", 9),
            new TrabajoImpresion("IMP-008", "DOCENTE", "Rubrica.pdf", 6)
        };
        for (TrabajoImpresion t : iniciales) {
            gestor.registrarTrabajo(t);
        }
    }


    private void registrar() {
        String codigo = txtCodigo.getText();
        String documento = txtDocumento.getText().trim();
        String solicitante = (String) cmbSolicitante.getSelectedItem();
        int prioridad = (Integer) spnPrioridad.getValue();

        if (codigo.isBlank()) {
            aviso("El codigo no puede estar vacio.");
            return;
        }

        if(!codigo.matches("IMP-\\d+")){
            aviso("El codigo debe tener el formato IMP-001");
            return;
        }

        if(prioridad <= 0){
            aviso("La prioridad no puede ser un número negativo");
        }

        if (gestor.existeTrabajo(codigo)) {
            aviso("Ya existe un trabajo pendiente con el codigo " + codigo + ".");
            return;
        }
        if (gestor.consultarEspaciosDisponibles() <= 0) {
            aviso("La cola esta llena (capacidad " + CAPACIDAD + "). Procese un trabajo antes de registrar otro.");
            return;
        }

        TrabajoImpresion trabajo = new TrabajoImpresion(codigo, solicitante, documento, prioridad);
        if (gestor.registrarTrabajo(trabajo)) {
            JOptionPane.showMessageDialog(this, "Trabajo " + codigo + " registrado.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiar();
            listar();
            actualizarContadores();
        } else {
            aviso("No se pudo registrar el trabajo.");
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtDocumento.setText("");
        cmbSolicitante.setSelectedIndex(0);
        spnPrioridad.setValue(1);
        txtCodigo.requestFocus();
    }

    private void consultar() {
        TrabajoImpresion siguiente = gestor.consultarSiguienteTrabajo();
        mostrarDetalle(siguiente);
        if (siguiente == null) {
            aviso("La cola esta vacia: no hay trabajos pendientes.");
        }
    }

    private void procesar() {
        TrabajoImpresion procesado = gestor.procesarSiguienteTrabajo();
        mostrarDetalle(procesado);
        if (procesado == null) {
            aviso("La cola esta vacia: no hay trabajos para procesar.");
            return;
        }
        listar();
        actualizarContadores();
    }

    private void listar() {
        List<TrabajoImpresion> trabajos = gestor.listarTrabajos();
        StringBuilder sb = new StringBuilder();
        if (trabajos.isEmpty()) {
            sb.append("No hay trabajos pendientes.");
        } else {
            for (TrabajoImpresion t : trabajos) {
                sb.append(t).append('\n');
            }
        }
        txtPendientes.setText(sb.toString());
        txtPendientes.setCaretPosition(0);
    }

    private void mostrarDetalle(TrabajoImpresion t) {
        lblValorCodigo.setText(t == null ? "-" : t.getCodigo());
        lblValorSolicitante.setText(t == null ? "-" : t.getSolicitante());
        lblValorDocumento.setText(t == null ? "-" : t.getDocumento());
        lblValorPrioridad.setText(t == null ? "-" : String.valueOf(t.getPrioridad()));
        lblValorEstado.setText(t == null ? "-" : t.getEstado());
    }

    private void actualizarContadores() {
        lblPendientes.setText("PENDIENTES: " + gestor.contarTrabajosPendientes());
        lblDisponibles.setText("DISPONIBLES: " + gestor.consultarEspaciosDisponibles());
        lblCapacidad.setText("CAPACIDAD: " + CAPACIDAD);
    }

    private void aviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestorImpresiones gestor = new GestorImpresiones(CAPACIDAD);
            JFrame ventana;
            try {
                ventana = new InterfazImpresiones(gestor);
            } catch (IllegalStateException e) {
                ventana = new InterfazImpresionesManual(gestor);
            }
            ventana.setVisible(true);
        });
    }
}
