import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;

public class PanelGrafica extends JPanel {

    private Map<String, Double> datosGrafica;

    public PanelGrafica() {
        datosGrafica = new LinkedHashMap<String, Double>();
        setBackground(Color.WHITE);
    }

    public void setDatosGrafica(Map<String, Double> datosGrafica) {
        this.datosGrafica = datosGrafica;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (datosGrafica == null || datosGrafica.isEmpty()) {
            g.drawString("No hay datos para graficar", 20, 30);
            return;
        }

        int ancho = getWidth();
        int alto = getHeight();
        int margenIzquierdo = 55;
        int margenInferior = 65;
        int margenSuperior = 30;
        int espacioGrafica = alto - margenSuperior - margenInferior;

        g.setColor(Color.BLACK);
        g.drawLine(margenIzquierdo, alto - margenInferior, ancho - 20, alto - margenInferior);
        g.drawLine(margenIzquierdo, margenSuperior, margenIzquierdo, alto - margenInferior);

        double maximo = datosGrafica.values().stream()
                .mapToDouble(valor -> valor)
                .max()
                .orElse(1);

        int cantidad = datosGrafica.size();
        int anchoDisponible = ancho - margenIzquierdo - 35;
        int anchoBarra = Math.max(20, anchoDisponible / (cantidad * 2));
        int separacion = anchoBarra;
        int x = margenIzquierdo + 20;

        FontMetrics medidas = g.getFontMetrics();

        for (Map.Entry<String, Double> entrada : datosGrafica.entrySet()) {

            int alturaBarra = (int) ((entrada.getValue() / maximo) * espacioGrafica);
            int y = alto - margenInferior - alturaBarra;

            g.setColor(new Color(80, 130, 190));
            g.fillRect(x, y, anchoBarra, alturaBarra);

            g.setColor(Color.BLACK);
            g.drawRect(x, y, anchoBarra, alturaBarra);

            String temperatura = String.format("%.1f", entrada.getValue());
            int textoTemp = medidas.stringWidth(temperatura);
            g.drawString(temperatura, x + (anchoBarra - textoTemp) / 2, y - 5);

            String ciudad = entrada.getKey();
            int textoCiudad = medidas.stringWidth(ciudad);
            g.drawString(ciudad, x + (anchoBarra - textoCiudad) / 2, alto - margenInferior + 18);

            x = x + anchoBarra + separacion;
        }

        g.drawString("Promedio de temperatura por ciudad", 20, 20);
        g.drawString("Temperatura", 5, 40);
    }
}
