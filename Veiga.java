import java.util.concurrent.Semaphore;

public class Veiga {

    public static final int N_MEIGAS = 30;
    public static final int MAX_LOTES = 10;
    public static final int MAX_ARMAS_LOTE = 30;
    public static final int MAX_INGREDIENTES_RECETA = 10;
    public static final int N_TREBEDES = 3;
    public static final int CAPACIDAD_BARCA = 2;
    public static final int CAPACIDAD_CUEVA = 3;
    public static final int GRUPO_BOSQUE = 7;
    public static final int MAX_RECARGA_PILA = 100;
    public static final int DOSIS_BENDITA = 30;
    // Tiempos máximos y mínimos
    public static final int TMIN_COCCION = 3000;
    public static final int TMAX_COCCION = 5000;
    public static final int TMIN_TRAVESIA = 100;
    public static final int TMAX_TRAVESIA = 3000;
    public static final int TESPERA_EMBARCADERO = 3000;
    public static final int TESPERA_PLAYA = 3000;
    public static final int TMIN_CONJURO = 1000;
    public static final int TMAX_CONJURO = 5000;
    public static final int TMIN_PUENTE = 1000;
    public static final int TMAX_PUENTE = 2000;
    public static final int TMIN_BOSQUE = 1000;
    public static final int TMAX_BOSQUE = 2000;
    public static final int TMIN_RECOLECTAR_AGUA = 1000;
    public static final int TMAX_RECOLECTAR_AGUA = 5000;
    public static final int TMIN_RECOLECTAR_MANDRAGORA = 1000;
    public static final int TMAX_RECOLECTAR_MANDRAGORA = 5000;
    public static final int TMIN_RECOLECTAR_PLUMA = 1000;
    public static final int TMAX_RECOLECTAR_PLUMA = 5000;
    public static final int TMIN_RECOLECTAR_GUANO = 1000;
    public static final int TMAX_RECOLECTAR_GUANO = 5000;

    // Arma . java, crear
    public enum Arma {
        FIN_SIMULACION, FIN_LOTE,
        MOSQUETE, LANZA, ALABARDA, TRABUCO;

        private static Arma[] valores = values();

        public static Arma Aleatoria() {
            int i = (int) (Math.random() * (valores.length - 2) + 2);
            return valores[i];
        }
    }

    // variables globales propias

    public static void main(String[] args) throws InterruptedException {

        // Crear NMeigas. que son 30, 30 objetos de meigas.
        // uno de, Marxua, y de seforiano.

        Sinforiano Seforiano = new Sinforiano();

        // estos escriben durante 1s
        Marxua Marxua = new Marxua();
        Meigas[] Megias_Array = new Meigas[N_MEIGAS];
        // Marxua.start();
        // Seforiano.start();

        for (int i = 0; i < N_MEIGAS; i++) {
            Megias_Array[i] = new Meigas(i);
            Megias_Array[i].start();

        }

        // crear un hilo de anton este estara en bucle infinito
        // Tio Anton se muere de viejo, y sera cuando el programa principal lo decida
        TioAnton Anton = new TioAnton();

        Anton.start();

        // Como funciona el tema de los hilos, por que si anton esta esperando ????
        // si el main, sigue fucnionando con anton.barcaAnton.embarco; ???????

        // Quien se esta metiendo en la barca. // HAce falta saber quien ??????????

        // Cuando alguien quiera añadirse a la cola del embarcadero.
        // metodo embarcadero, y playa

        // Gestionar colas Embarcadero y Playa con Cola Barca.

        // Cuando alguien quiera entrar ya sea en el embarcadero o en la playa,
        // 1 pasaran por las colas de los sitios y cuando la barca,
        // se encuentre en este, pasaran a la barca

        // Estaria en bucle;

        Seforiano.join();

        for (int i = 0; i < N_MEIGAS; i++) {
            Megias_Array[i].join();

        }

        // una vez que estas han terminado, que terminen todas las demas

        // Una vez que esto termine avisar de que tenemos que
        // acabar en el tio anton.

    }

}
