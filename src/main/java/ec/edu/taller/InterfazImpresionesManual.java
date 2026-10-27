package ec.edu.taller;

import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

public class InterfazImpresionesManual extends JFrame {
    static final int CAPACIDAD = 10;

    private final GestorImpresiones gestor;

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

    public InterfazImpresionesManual(GestorImpresiones gestor) {
        super("Centro de impresion universitario");
        this.gestor = gestor;
        initComponents();
        cargarDatosQuemados();
        listar();
        actualizarContadores();
    }

    private void initComponents() {
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("REGISTRAR TRABAJO", crearPanelRegistrar());
        pestanas.addTab("GESTIONAR TRABAJOS", crearPanelGestionar());

        pestanas.addChangeListener(e -> {
            if (pestanas.getSelectedIndex() == 1) {
                listar();
                actualizarContadores();
            }
        });

        setContentPane(pestanas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 560);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelRegistrar() {
        JPanel panel = new JPanel(null);
        Font fuente = new Font("SansSerif", Font.BOLD, 14);

        JLabel lblCodigo = new JLabel("CODIGO");
        JLabel lblSolicitante = new JLabel("SOLICITANTE");
        JLabel lblDocumento = new JLabel("DOCUMENTO");
        JLabel lblPrioridad = new JLabel("PRIORIDAD");
        for (JLabel l : new JLabel[] {lblCodigo, lblSolicitante, lblDocumento, lblPrioridad}) {
            l.setFont(fuente);
        }

        txtCodigo = new JTextField();
        cmbSolicitante = new JComboBox<>(new String[] {"ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO"});
        txtDocumento = new JTextField();
        spnPrioridad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        btnRegistrar = new JButton("REGISTRAR TRABAJO");
        btnLimpiar = new JButton("LIMPIAR ELEMENTOS");

        lblCodigo.setBounds(50, 50, 140, 28);
        txtCodigo.setBounds(200, 50, 300, 28);
        lblSolicitante.setBounds(50, 90, 140, 28);
        cmbSolicitante.setBounds(200, 90, 300, 28);
        lblDocumento.setBounds(50, 130, 140, 28);
        txtDocumento.setBounds(200, 130, 300, 28);
        lblPrioridad.setBounds(50, 170, 140, 28);
        spnPrioridad.setBounds(200, 170, 120, 28);
        btnRegistrar.setBounds(50, 250, 230, 40);
        btnLimpiar.setBounds(330, 250, 230, 40);

        panel.add(lblCodigo);
        panel.add(txtCodigo);
        panel.add(lblSolicitante);
        panel.add(cmbSolicitante);
        panel.add(lblDocumento);
        panel.add(txtDocumento);
        panel.add(lblPrioridad);
        panel.add(spnPrioridad);
        panel.add(btnRegistrar);
        panel.add(btnLimpiar);

        btnRegistrar.addActionListener(e -> registrar());
        btnLimpiar.addActionListener(e -> limpiar());
        return panel;
    }

    private JPanel crearPanelGestionar() {
        JPanel panel = new JPanel(null);
        Font fuente = new Font("SansSerif", Font.BOLD, 14);

        JLabel lblCodigo = new JLabel("CODIGO");
        JLabel lblSolicitante = new JLabel("SOLICITANTE");
        JLabel lblDocumento = new JLabel("DOCUMENTO");
        JLabel lblPrioridad = new JLabel("PRIORIDAD");
        JLabel lblEstado = new JLabel("ESTADO");
        for (JLabel l : new JLabel[] {lblCodigo, lblSolicitante, lblDocumento, lblPrioridad, lblEstado}) {
            l.setFont(fuente);
        }

        lblValorCodigo = new JLabel("-");
        lblValorSolicitante = new JLabel("-");
        lblValorDocumento = new JLabel("-");
        lblValorPrioridad = new JLabel("-");
        lblValorEstado = new JLabel("-");

        JLabel lblTitulo = new JLabel("TRABAJOS PENDIENTES (ORDENADOS: MENOR VALOR = MAYOR PRIORIDAD)");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));

        txtPendientes = new JTextArea();
        txtPendientes.setEditable(false);
        txtPendientes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(txtPendientes);

        lblPendientes = new JLabel();
        lblDisponibles = new JLabel();
        lblCapacidad = new JLabel();
        for (JLabel l : new JLabel[] {lblPendientes, lblDisponibles, lblCapacidad}) {
            l.setFont(fuente);
        }

        btnProcesar = new JButton("PROCESAR");
        btnConsultar = new JButton("CONSULTAR SIGUIENTE");
        btnListar = new JButton("LISTAR");

        lblCodigo.setBounds(50, 20, 140, 24);
        lblValorCodigo.setBounds(220, 20, 300, 24);
        lblSolicitante.setBounds(50, 46, 140, 24);
        lblValorSolicitante.setBounds(220, 46, 300, 24);
        lblDocumento.setBounds(50, 72, 140, 24);
        lblValorDocumento.setBounds(220, 72, 300, 24);
        lblPrioridad.setBounds(50, 98, 140, 24);
        lblValorPrioridad.setBounds(220, 98, 300, 24);
        lblEstado.setBounds(50, 124, 140, 24);
        lblValorEstado.setBounds(220, 124, 300, 24);
        lblTitulo.setBounds(30, 160, 580, 22);
        scroll.setBounds(30, 186, 570, 170);
        lblPendientes.setBounds(40, 375, 200, 24);
        lblDisponibles.setBounds(40, 405, 200, 24);
        lblCapacidad.setBounds(40, 435, 200, 24);
        btnProcesar.setBounds(340, 370, 230, 34);
        btnConsultar.setBounds(340, 412, 230, 34);
        btnListar.setBounds(340, 454, 230, 34);

        panel.add(lblCodigo);
        panel.add(lblValorCodigo);
        panel.add(lblSolicitante);
        panel.add(lblValorSolicitante);
        panel.add(lblDocumento);
        panel.add(lblValorDocumento);
        panel.add(lblPrioridad);
        panel.add(lblValorPrioridad);
        panel.add(lblEstado);
        panel.add(lblValorEstado);
        panel.add(lblTitulo);
        panel.add(scroll);
        panel.add(lblPendientes);
        panel.add(lblDisponibles);
        panel.add(lblCapacidad);
        panel.add(btnProcesar);
        panel.add(btnConsultar);
        panel.add(btnListar);

        btnProcesar.addActionListener(e -> procesar());
        btnConsultar.addActionListener(e -> consultar());
        btnListar.addActionListener(e -> listar());
        return panel;
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
        if (siguiente == null) {
            mostrarDetalle(null);
            aviso("La cola esta vacia: no hay trabajos pendientes.");
            return;
        }
        mostrarDetalle(siguiente);
    }

    private void procesar() {
        TrabajoImpresion procesado = gestor.procesarSiguienteTrabajo();
        if (procesado == null) {
            mostrarDetalle(null);
            aviso("La cola esta vacia: no hay trabajos para procesar.");
            return;
        }
        mostrarDetalle(procesado);
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
            new InterfazImpresionesManual(gestor).setVisible(true);
        });
    }
}
