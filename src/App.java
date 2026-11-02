import controladores.TemperaturasControlador;
import servicios.TemperaturasServicio;
import vistas.TemperaturasVista;

public class App {

    public static void main(String[] args) {
        TemperaturasServicio servicio = new TemperaturasServicio();
        TemperaturasControlador controlador = new TemperaturasControlador(servicio);
        TemperaturasVista ventana = new TemperaturasVista(controlador);
        ventana.setVisible(true);
    }
}
