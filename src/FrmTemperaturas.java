import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrmTemperaturas extends JFrame {

    private JTextField txtRuta;
    private JTextField txtFechaInicial;
    private JTextField txtFechaFinal;
    private JTextField txtFechaConsulta;

    private JButton btnCargar;
    private JButton btnGraficar;
    private JButton btnConsultar;

    private JTable tablaPromedios;
    private DefaultTableModel modeloTabla;
    private PanelGrafica panelGrafica;

    private JLabel lblMasCalurosa;
    private JLabel lblMenosCalurosa;
    private JLabel lblCantidadDatos;

    private GestorTemperaturas gestor;

    public FrmTemperaturas() {
        gestor = new GestorTemperaturas();

        setTitle("Temperaturas por ciudad");
        setSize(950, 650);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel lblTitulo = new JLabel("Análisis de temperaturas por ciudad");
        lblTitulo.setBounds(20, 10, 500, 25);
        add(lblTitulo);

        JLabel lblRuta = new JLabel("Archivo CSV:");
        lblRuta.setBounds(20, 45, 100, 25);
        add(lblRuta);

        txtRuta = new JTextField("data/temperaturas_2025.csv");
        txtRuta.setBounds(115, 45, 260, 25);
        add(txtRuta);

        btnCargar = new JButton("Cargar CSV");
        btnCargar.setBounds(390, 45, 130, 25);
        add(btnCargar);

        lblCantidadDatos = new JLabel("Registros cargados: 0");
        lblCantidadDatos.setBounds(540, 45, 250, 25);
        add(lblCantidadDatos);

        JLabel lblFechaInicial = new JLabel("Fecha inicial:");
        lblFechaInicial.setBounds(20, 90, 100, 25);
        add(lblFechaInicial);

        txtFechaInicial = new JTextField("01/01/2025");
        txtFechaInicial.setBounds(115, 90, 120, 25);
        add(txtFechaInicial);

        JLabel lblFechaFinal = new JLabel("Fecha final:");
        lblFechaFinal.setBounds(255, 90, 100, 25);
        add(lblFechaFinal);

        txtFechaFinal = new JTextField("31/01/2025");
        txtFechaFinal.setBounds(340, 90, 120, 25);
        add(txtFechaFinal);

        btnGraficar = new JButton("Graficar promedios");
        btnGraficar.setBounds(480, 90, 170, 25);
        add(btnGraficar);

        JLabel lblFechaConsulta = new JLabel("Fecha a consultar:");
        lblFechaConsulta.setBounds(20, 135, 130, 25);
        add(lblFechaConsulta);

        txtFechaConsulta = new JTextField("01/01/2025");
        txtFechaConsulta.setBounds(145, 135, 120, 25);
        add(txtFechaConsulta);

        btnConsultar = new JButton("Ciudad mayor y menor");
        btnConsultar.setBounds(285, 135, 190, 25);
        add(btnConsultar);

        lblMasCalurosa = new JLabel("Más calurosa: ");
        lblMasCalurosa.setBounds(500, 130, 400, 25);
        add(lblMasCalurosa);

        lblMenosCalurosa = new JLabel("Menos calurosa: ");
        lblMenosCalurosa.setBounds(500, 155, 400, 25);
        add(lblMenosCalurosa);

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Ciudad");
        modeloTabla.addColumn("Promedio");

        tablaPromedios = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaPromedios);
        scrollTabla.setBounds(20, 200, 300, 360);
        add(scrollTabla);

        panelGrafica = new PanelGrafica();
        panelGrafica.setBounds(340, 200, 570, 360);
        add(panelGrafica);

        JLabel lblAyuda = new JLabel("Formato de fecha: dd/MM/yyyy. Ejemplo: 01/01/2025");
        lblAyuda.setBounds(20, 570, 500, 25);
        add(lblAyuda);

        btnCargar.addActionListener(e -> cargarCsv());
        btnGraficar.addActionListener(e -> graficarPromedios());
        btnConsultar.addActionListener(e -> consultarFecha());
    }

    private void cargarCsv() {
        try {
            gestor.cargarCsv(txtRuta.getText().trim());
            lblCantidadDatos.setText("Registros cargados: " + gestor.getDatos().size());
            JOptionPane.showMessageDialog(this, "Archivo cargado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el archivo: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void graficarPromedios() {
        try {
            LocalDate fechaInicial = LocalDate.parse(txtFechaInicial.getText().trim(), GestorTemperaturas.FORMATO_FECHA);
            LocalDate fechaFinal = LocalDate.parse(txtFechaFinal.getText().trim(), GestorTemperaturas.FORMATO_FECHA);

            Map<String, Double> promedios = gestor.promedioPorCiudad(fechaInicial, fechaFinal);

            modeloTabla.setRowCount(0);

            promedios.forEach((ciudad, promedio) -> 
                modeloTabla.addRow(new Object[]{ciudad, String.format("%.2f", promedio)})
            );

            panelGrafica.setDatosGrafica(promedios);

            if (promedios.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos en ese rango de fechas", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "Revise las fechas ingresadas: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarFecha() {
        try {
            LocalDate fecha = LocalDate.parse(txtFechaConsulta.getText().trim(), GestorTemperaturas.FORMATO_FECHA);

            Optional<RegistroTemperatura> mayor = gestor.ciudadMasCalurosa(fecha);
            Optional<RegistroTemperatura> menor = gestor.ciudadMenosCalurosa(fecha);

            if (mayor.isPresent() && menor.isPresent()) {
                lblMasCalurosa.setText("Más calurosa: " + mayor.get().getCiudad() + " - " + mayor.get().getTemperatura() + " °C");
                lblMenosCalurosa.setText("Menos calurosa: " + menor.get().getCiudad() + " - " + menor.get().getTemperatura() + " °C");
            } else {
                lblMasCalurosa.setText("Más calurosa: sin datos");
                lblMenosCalurosa.setText("Menos calurosa: sin datos");
            }

        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, "Revise la fecha ingresada: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}