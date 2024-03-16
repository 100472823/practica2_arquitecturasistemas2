import java.util.Random;

public class Seforiano extends Hilo {

    private Trazador trazador = new Trazador(1, "Meigas");

    public static int NumeroAleatorioLotes;

    public Seforiano() {

    }

    public void run() {

        /*
         * 
         * for (int i = 0; i < 5; i++) {
         * 
         * trazador.Print("Seforiano");
         * Pausa(1000);
         * 
         * }
         * 
         */

    }

    public void NumeroDeLotes() {

        // Se realiza una vez indica el numero de los
        // Lotes a realizar antes de que termine la vida

        Random random = new Random();

        int NumeroAlearorioLotes = random.nextInt(Veiga.MAX_ARMAS_LOTE) + 1;

    }

    public void SeleccionArmas() {

        // Seleccionamos Un numero aleatorio, entre 1 y max lotes
        // cuando llegue a max lotes, seforiano muere.

        // Seleccionamos un arma, pasarselo a maruxa

    }

}
