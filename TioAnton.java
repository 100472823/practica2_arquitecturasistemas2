import java.util.concurrent.Semaphore;

public class TioAnton extends Hilo {

    private int end_tioAnton = 0;

    private static Trazador trazador = new Trazador(3, "TioAnton");

    static private Semaphore BarreraEmbarcadero = new Semaphore(0);
    static private Semaphore BarreraPlaya = new Semaphore(0);

    static private Semaphore BarreraBARCA = new Semaphore(0);

    static private Semaphore MutexTioAnton = new Semaphore(1);
    static private Semaphore MutexBarca = new Semaphore(1);

    // Implementar mas mutex o ver que hacer, son la variable, de, NPERSONAS BARCA

    // Antes de salir a navegar quito permisos en el mutex de la barca
    // Simplemente dejarlo con un adquire y no hacer release?

    // variables criticas, para añadirse gente
    // Si la capacidad, de semaforo barca, es Z == 3, entonces no hace falta nada
    // Si esta solo anton la ponemos en 1, si esta anton +1 entonces ponemos 2.

    private static int NBarreraPlaya = 0;
    private static int NBarreraEmbarcadero = 0;
    private static int NPERSONASBARCA = 0;
    private static int NBarreraBarcaDespuesRelease = 0;

    public static final int playa = 2;

    public static final int embarcadero = 0;

    // Haria falta un mutex
    public static int status = -1;
    public static boolean navegando;

    public TioAnton() {

    }

    public void run() {

        trazador.Print("Inicio El Hilo Tio Anton");

        while (end_tioAnton == 0) {

            // La barca empieza vacia, ;
            trazador.Print("Llego A embarcadero");

            EsperarEmbarcadero();
            trazador.Print("Esperando en el embarcadero");
            EmbarcaderoABarca();
            // Antes de Navegar Paso de una barrera a la otra.
            Navego();

            BajoATierra();
            trazador.Print("En la Playa");
            EsperarPlaya();
            // Cambiar nombre
            PlayaABarca();

            Navego();
            BajoATierra();
        }

    }

    public void end() {

        end_tioAnton = 1;

    }

    public void BajoATierra() {

        if (NPERSONASBARCA != 0) {

            try {
                MutexBarca.acquire();
                trazador.Print("Voy a hazer releases de" + NPERSONASBARCA);
                BarreraBARCA.release(NPERSONASBARCA);

                NPERSONASBARCA = 0;
                NBarreraBarcaDespuesRelease = 0;
                MutexBarca.release();

            } catch (InterruptedException e) {
                e.printStackTrace();

            }

        }

    }

    public static void AñadirBarreraEmbarcadero() {

        try {

            MutexTioAnton.acquire();
            // Añado una persona a la barca
            NBarreraEmbarcadero++;
            MutexTioAnton.release();

            BarreraEmbarcadero.acquire();

            MutexTioAnton.acquire();
            // Añado una persona a la barca
            NBarreraEmbarcadero--;
            MutexTioAnton.release();
            // Acquiere barca
            // Me tengo que quidar aqui pillado por que no me quedo pillado.
            MutexBarca.acquire();
            NPERSONASBARCA++;
            MutexBarca.release();
            // trazador.Print("AÑADO " + String.valueOf(NPERSONASBARCA) + "pasajeros a la //
            // cola de BARCA COLA MEIGAS");
            BarreraBARCA.acquire();

            // Primero modifico la variable, y hago que se me queden pillados aqui.
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public static void AñadirBarreraPlaya() {

        try {

            MutexTioAnton.acquire();
            // Añado una persona a la barca
            NBarreraPlaya++;
            MutexTioAnton.release();
            trazador.Print("Me quedo pillado en la Barrera de la playa");
            BarreraPlaya.acquire();

            MutexTioAnton.acquire();
            // Añado una persona a la barca
            NBarreraPlaya--;
            MutexTioAnton.release();
            MutexBarca.acquire();
            NPERSONASBARCA++;
            MutexBarca.release();
            // trazador.Print("AÑADO " + String.valueOf(NPERSONASBARCA) + "pasajeros a la //
            // cola de BARCA COLA MEIGAS");
            trazador.Print("Me quedo pillado en la Barrera de la barca ya he sumado me deberia de quedar pillado");
            BarreraBARCA.acquire();

            // Primero modifico la variable, y hago que se me queden pillados aqui.
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    /* MODIFICAR NOMBRES */
    /* SACAR FUNCION ABRIR */

    public void EsperarEmbarcadero() {

        navegando = false;
        status = embarcadero;

        try {
            MutexTioAnton.acquire();
            trazador.Print(NBarreraEmbarcadero + "Hay esperando en el embarcadero");
            MutexTioAnton.release();

            BarreraEmbarcadero.release(Veiga.CAPACIDAD_BARCA);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // No separar local embarcadero y embarcadero.

        // Procedo a esperar el tiempo declarado. TESPERA.

        Pausa(Veiga.TESPERA_EMBARCADERO);

    }

    public void Navego() {
        try {

            MutexBarca.acquire();
            trazador.Print("Voy a NAVEGAR CON " + NPERSONASBARCA);
            MutexBarca.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        trazador.Print("En plena Travesia, Remando");
        navegando = true;

        Pausa(Veiga.TMIN_TRAVESIA, Veiga.TMAX_TRAVESIA);

    }

    public void EsperarPlaya() {

        navegando = false;
        status = playa;

        try {
            MutexTioAnton.acquire();
            trazador.Print(NBarreraPlaya + "Hay esperando La Playa");
            MutexTioAnton.release();

            BarreraPlaya.release(Veiga.CAPACIDAD_BARCA);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Pausa(Veiga.TESPERA_PLAYA);

    }

    public void EmbarcaderoABarca() {
        /*
         * PASO EL ESTAR PILLADO DEL EMBARCADERO A QUE ESTEN PILLADOS EN EL DE LA BARCA
         */
        trazador.Print("Entro");
        try {

            // Habra que hacer un acquire
            // para cerrar el embarcadero, si solo se monta 1 persona
            /* Cambiar de BARCA */
            /* Comprobar x-capacidad */
            MutexBarca.acquire();
            if (NPERSONASBARCA == 1) {

                BarreraEmbarcadero.acquire();
                // si solo hay una persona en el embarcadero
                // entraria 1, y se quedaria la puerta abierta para
                // el siguiente que pase

            } else if (NPERSONASBARCA == 0) {

                for (int i = 0; i < 2; i++) {

                    BarreraEmbarcadero.acquire();

                }

            }

            trazador.Print(" Salgo desde el embarcadero con" + String.valueOf(NPERSONASBARCA) + "pasajeros");
            MutexBarca.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public void PlayaABarca() {
        /*
         * PASO EL ESTAR PILLADO DEL EMBARCADERO A QUE ESTEN PILLADOS EN EL DE LA BARCA
         */
        trazador.Print("Entro");
        try {

            // Habra que hacer un acquire
            // para cerrar el embarcadero, si solo se monta 1 persona
            /* Cambiar de BARCA */
            /* Comprobar x-capacidad */
            MutexBarca.acquire();
            if (NPERSONASBARCA == 1) {

                BarreraPlaya.acquire();
                // si solo hay una persona en el embarcadero
                // entraria 1, y se quedaria la puerta abierta para
                // el siguiente que pase

            } else if (NPERSONASBARCA == 0) {

                for (int i = 0; i < 2; i++) {

                    BarreraPlaya.acquire();

                }

            }

            trazador.Print(" Salgo desde la playa con" + String.valueOf(NPERSONASBARCA) + "pasajeros");
            MutexBarca.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

}
