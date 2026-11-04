package vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Map;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

public class PanelGrafica extends JPanel {

    private static final String SERIE = "Temperatura promedio";
    private static final String TITULO = "Promedio de temperatura por ciudad";
    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 14);

    private final DefaultCategoryDataset dataset;
    private final JFreeChart grafica;

    public PanelGrafica() {
        setLayout(new BorderLayout());

        dataset = new DefaultCategoryDataset();

        grafica = ChartFactory.createBarChart(
                TITULO,                               // título
                "Ciudad",                             // eje X
                "Temperatura (°C)",                   // eje Y
                dataset,
                PlotOrientation.VERTICAL,
                false,  // leyenda
                true,   // tooltips
                false); // urls

        CategoryPlot plot = grafica.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setNoDataMessage("No hay datos para graficar");

        // Que no se corten los nombres de ciudad y quede espacio para la etiqueta del valor
        CategoryAxis ejeX = plot.getDomainAxis();
        ejeX.setMaximumCategoryLabelWidthRatio(1.0f);
        ValueAxis ejeY = plot.getRangeAxis();
        ejeY.setUpperMargin(0.12);

        grafica.setTitle(new TextTitle(TITULO, FUENTE_TITULO));

        BarRenderer barras = (BarRenderer) plot.getRenderer();
        barras.setBarPainter(new StandardBarPainter()); // barras planas, sin degradado
        barras.setSeriesPaint(0, new Color(80, 130, 190));
        barras.setDrawBarOutline(true);
        barras.setSeriesOutlinePaint(0, Color.BLACK);
        barras.setDefaultItemLabelGenerator(
                new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.0")));
        barras.setDefaultItemLabelsVisible(true);

        add(new ChartPanel(grafica), BorderLayout.CENTER);
    }

    /** Recibe ciudad -> promedio y redibuja la gráfica. */
    public void setDatosGrafica(Map<String, Double> datos) {
        dataset.clear();
        if (datos != null) {
            datos.forEach((ciudad, promedio) -> dataset.addValue(promedio, SERIE, ciudad));
        }
    }

    public void setTitulo(String titulo) {
        grafica.setTitle(new TextTitle(titulo, FUENTE_TITULO));
    }
}
