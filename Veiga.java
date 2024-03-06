
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

    public static void main(String[] args) throws InterruptedException {

        // Crear NMeigas. que son 30, 30 objetos de meigas.
        // uno de, Marxua, y de seforiano.
        
        Seforiano Seforiano = new Seforiano();
        Marxua Marxua = new Marxua();
        // estos escriben durante 1s

        Meigas[] Megias_Array = new Meigas[N_MEIGAS];

        Seforiano.start();
        Marxua.start();

        for (int i = 0; i < N_MEIGAS; i++) {

            Megias_Array[i].start();

        }

        // crear un hilo de anton este estara en bucle infinito
        TioAnton Anton = new TioAnton();

        Anton.start();


        Seforiano.join();
        Marxua.join();
        for (int i = 0; i < N_MEIGAS; i++) {

            Megias_Array[i].join();

        }

        // Una vez que esto termine avisar de que tenemos que 
        // acabar en el tio anton. 

    }

}
