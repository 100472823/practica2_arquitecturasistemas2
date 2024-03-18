import java.util.concurrent.Semaphore;

public class TioAnton extends Hilo {

    private int end_tioAnton = 0;

    private Trazador trazador = new Trazador(3, "TioAnton");

    static private Semaphore BarreraEmbarcadero = new Semaphore(0);
    static private Semaphore BarreraPlaya = new Semaphore(0);

    // Le puedo dejar este semaforo a 2. ??
    private static Semaphore BARCA = new Semaphore(0);

    static private Semaphore MutexBarreraEmbarcadero = new Semaphore(1);
    static private Semaphore MutexBarreraPlaya = new Semaphore(1);
    static private Semaphore MutexBarreraBarca = new Semaphore(1);
    static private int Añadimos2 = 0;
    static private int AñadimosPlaya = 0;

    // Cruzar Puente.
    // Barrera para esperar, a 0
    // Mutex, a uno.

    // Antes de salir a navegar quito permisos en el mutex de la barca
    // variables criticas, para añadirse gente
    // Si la capacidad, de semaforo barca, es Z == 3, entonces no hace falta nada
    // Si esta solo anton la ponemos en 1, si esta anton +1 entonces ponemos 2.

    private static int NBarreraPlaya = 0;
    private static int NBarreraEmbarcadero = 0;

    // Contando ya que anton esta dentro
    private static int NPERSONASBARCA = 0;

    public static final int playa = 2;
    public static boolean navegando;
    public static final int embarcadero = 0;

    public static int status = -1;

    public TioAnton() {

    }

    public void run() {

        trazador.Print("TioAnton");
        trazador.Print("Inicio El Hilo");

        while (end_tioAnton == 0) {

            // La barca empieza vacia, ;
            trazador.Print("Llego A embarcadero");
            EsperarEmbarcadero();
            EmbarcaderoABarca();
            // Antes de Navegar Paso de una barrera a la otra.

            trazador.Print("Navego con " + String.valueOf(NPERSONASBARCA) + "pasajeros");
            Navego();
            // DESEMBARCAR ??
            // Añado un 1, en el mutex.
            // Para que pueda desembarcar.
            BajoATierra();
            trazador.Print("En la Playa");
            EsperarPlaya();
            PlayaABarca();

            // TryCatch Para NPERSONAS BARCA??

            Navego();
            BajoATierra();
        }

    }

    public void end() {

        end_tioAnton = 1;

    }

    public void BajoATierra() {

        if (NPERSONASBARCA != 0) {

            BARCA.release(NPERSONASBARCA);
            try {

                MutexBarreraBarca.acquire();
                NPERSONASBARCA = 0;

                MutexBarreraBarca.release();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }

        }

    }

    public static void AñadirBarreraEmbarcadero() {

        try {

            MutexBarreraEmbarcadero.acquire();
            // Añado una persona a la barca
            NBarreraEmbarcadero++;
            MutexBarreraEmbarcadero.release();
            BarreraEmbarcadero.acquire();

            // Primero modifico la variable, y hago que se me queden pillados aqui.
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public static void AñadirBarreraPlaya() {
        try {

            MutexBarreraPlaya.acquire();
            // Añado una persona a la barca
            NBarreraPlaya++;
            MutexBarreraPlaya.release();
            BarreraPlaya.acquire();

            // Primero modifico la variable, y hago que se me queden pillados aqui.
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public void EsperarEmbarcadero() {

        navegando = false;
        status = embarcadero;

        // Procedo a esperar el tiempo declarado. TESPERA.

        Pausa(Veiga.TESPERA_EMBARCADERO);

        // ZaparparBarca // que puede ser de la clase barca.
    }

    public void Navego() {
        trazador.Print("En plena Travesia, Remando");
        navegando = true;

        Pausa(Veiga.TMIN_TRAVESIA, Veiga.TMAX_TRAVESIA);

        // Mientras que este zarpandoBarca, No puede bajarse nadie.

    }

    public void EsperarPlaya() {
        navegando = false;
        status = playa;
        // Desembarca todo el mundo, y espero otra vez, el tiempo

        Pausa(Veiga.TESPERA_PLAYA);
        // Durante este tiempo otra vez, Puede embarcar gente en la playa.

        // Un una vez pase el tiempo

    }

    public void EmbarcaderoABarca() {

        try {

            MutexBarreraEmbarcadero.acquire();
            trazador.Print(String.valueOf(NBarreraEmbarcadero));

            if (NBarreraEmbarcadero != 0) {

                // No separar local embarcadero y embarcadero.
                trazador.Print(String.valueOf(NBarreraEmbarcadero));
                if (NBarreraEmbarcadero >= 2) {
                    Añadimos2 = 2;
                } else if (NBarreraEmbarcadero == 1) {

                    Añadimos2 = 1;

                } else {
                    Añadimos2 = 0;
                }
                trazador.Print(String.valueOf(Añadimos2) + "Añadimos");
                MutexBarreraEmbarcadero.release();

                for (int i = 0; i < Añadimos2; i++) {

                    trazador.Print("Entro");
                    BARCA.release();
                    // Añado una persona a la barca
                    BarreraEmbarcadero.release();
                    BARCA.acquire();
                    MutexBarreraBarca.acquire();
                    NPERSONASBARCA++;
                    MutexBarreraBarca.release();
                    MutexBarreraEmbarcadero.acquire();
                    NBarreraEmbarcadero--;
                    MutexBarreraEmbarcadero.release();

                }
                MutexBarreraBarca.acquire();
                trazador.Print(" Salgo desde el embarcadero con" + String.valueOf(NPERSONASBARCA) + "pasajeros");
                MutexBarreraBarca.release();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public void PlayaABarca() {

        try {

            MutexBarreraPlaya.acquire();
            trazador.Print(String.valueOf(NBarreraPlaya) + "Esperando Playa");

            if (NBarreraPlaya != 0) {

                // No separar local embarcadero y embarcadero.
                trazador.Print(String.valueOf(NBarreraPlaya));
                if (NBarreraPlaya >= 2) {
                    AñadimosPlaya = 2;
                } else if (NBarreraPlaya == 1) {

                    AñadimosPlaya = 1;

                } else {
                    AñadimosPlaya = 0;
                }
                trazador.Print(String.valueOf(AñadimosPlaya) + "AñadimosPklaya");
                MutexBarreraPlaya.release();

                for (int i = 0; i < AñadimosPlaya; i++) {

                    trazador.Print("Entro");
                    BARCA.release();
                    // Añado una persona a la barca
                    BarreraPlaya.release();
                    BARCA.acquire();
                    MutexBarreraBarca.acquire();
                    NPERSONASBARCA++;
                    MutexBarreraBarca.release();
                    MutexBarreraPlaya.acquire();
                    NBarreraPlaya--;
                    MutexBarreraPlaya.release();

                }
                MutexBarreraBarca.acquire();
                trazador.Print("Vuelvo hacia el embarcadero con " + String.valueOf(NPERSONASBARCA) + "pasajeros");
                MutexBarreraBarca.release();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }
}
