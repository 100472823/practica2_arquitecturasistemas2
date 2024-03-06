public class Marxua extends Hilo {

    private Trazador trazador = new Trazador(2, "Marxua");

    public Marxua() {

    }

    public void run() {

        for (int i = 0; i < 5; i++) {

            trazador.Print("Marxua");
            Pausa(1000);

        }

    }

}
