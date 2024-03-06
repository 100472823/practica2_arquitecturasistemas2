
public class TioAnton extends Hilo {

    private int end_tioAnton = 0;

    private Trazador trazador = new Trazador(1, "Meigas");

    public void run() {

        while (end_tioAnton == 0) {

            trazador.Print("TioAnton");
            Pausa(1000);

        }

    }

    public void end() {

        end_tioAnton = 1;

    }

}
