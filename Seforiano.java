public class Seforiano extends Hilo {

    private Trazador trazador = new Trazador(1, "Meigas");

    public Seforiano() {

    }

    public void run() {

        for (int i = 0; i < 5; i++) {

            trazador.Print("Seforiano");
            Pausa(1000);

        }

    }

}
