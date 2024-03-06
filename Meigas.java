public class Meigas extends Hilo {

    private Trazador trazador= new Trazador(5, "Meigas");
    private int Numero_Meiga ;


    public Meigas(int n) {

        this.trazador = trazador;
        this.Numero_Meiga=n;


    }

    public void run() {

        for (int i = 0; i < 5; i++) {

            trazador.Print("Meiga"+Numero_Meiga);
            Pausa(1000);

        }

    }

}
// atributo propio de la clase