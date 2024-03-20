import java.util.Random;

public class Sinforiano extends Hilo {

    private Trazador trazador = new Trazador(1, "Sinforiano");

    public static int NumeroAleatorioLotes;
    // Habra que hacer un mutex, en el numeroAleatorio de Lotes
    // Entonces dentro de los lotes
    // Eligo numero aleatorio de armas
    // Tipo de ese lote

    public Sinforiano() {

    }

    public void run() {

        trazador.Print("Sinforiano");
        Pausa(1000);

        NumeroDeLotes();

    }
    // Para cada uno de los lotes.
    // Lo transfiere a Maruxua.
    // Trasfiere, ArmaFinLote, ??

    public void NumeroDeLotes() {

        // Se realiza una vez indica el numero de los
        // Lotes a realizar antes de que termine la vida

        Random random = new Random();

        int NumeroAlearorioLotes = random.nextInt(Veiga.MAX_ARMAS_LOTE) + 1;

    }

    public void SeleccionArmas() {

        // Seleccionamos Un numero aleatorio, entre 1 y max lotes
        // cuando llegue a max lotes, Sinforiano muere.

        // Seleccionamos un arma, del enum, de armas, que tenemos en veiga.
        // , pasarselo a maruxa

    }

}
