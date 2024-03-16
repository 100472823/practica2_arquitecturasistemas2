import java.util.concurrent.Semaphore;

public class TioAnton extends Hilo {

    private int end_tioAnton = 0;

    private Trazador trazador = new Trazador(3, "TioAnton");

    // Si hay gente en alguna de estas colas, son el orden
    // en el que van a entrar en la barca.
    static private Semaphore BarreraEmbarcadero = new Semaphore(100);
    static private Semaphore BarreraPlaya = new Semaphore(100);
    // Distincion de Semaforo Mutex Semaforos y Barreras de capacidad

    // La modificacion de estas variables, es la zona critica necesitamos un mutex.
    static private Semaphore MutexBarreraEmbarcadero = new Semaphore(1);
    static private Semaphore MutexBarreraPlaya = new Semaphore(1);
    private static int NBarreraPlaya = 0;
    private static int NBarreraEmbarcadero = 0;

    static private Semaphore MutexBarreraBarca = new Semaphore(1);
    // Contando ya que anton esta dentro
    private static int NPERSONASBARCA = 0;
    private static Semaphore BARCA = new Semaphore(2);

    // Terminar de Implementarlo todo desde run.

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

            BajoATierra();
            trazador.Print("Llego A embarcadero");
            EsperarEmbarcadero();
            EmbarcaderoABarca();
            // Antes de Navegar Paso de una barrera a la otra.
            // Antes de salir a navegar quito permisos en el mutex de la barca
            // variables criticas, para añadirse gente
            // Si la capacidad, de semaforo barca, es Z == 3, entonces no hace falta nada
            // Si esta solo anton la ponemos en 1, si esta anton +1 entonces ponemos 2.
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
            trazador.Print("Vuelvo hacia el embarcadero con " + String.valueOf(NPERSONASBARCA) + "pasajeros");
            Navego();
            // Antes de navegar paso de Barrera Playa
            // A barrera barca,
            // Quito los permisios de barca para modificar el mutex. // Duda semaforo?
            // Si la capacidad, de semaforo barca, es == 3, entonces no hace falta nada
            // Si esta solo anton la ponemos en 1, si esta anton +1 entonces ponemos 2.

        }

    }

    public void end() {

        end_tioAnton = 1;

    }

    public void BajoATierra() {

        if (NPERSONASBARCA != 0) {

            BARCA.release(NPERSONASBARCA);
        }

    }

    public static void AñadirBarreraEmbarcadero() {

        try {

            BarreraEmbarcadero.acquire();
            MutexBarreraEmbarcadero.acquire();
            // Añado una persona a la barca
            NBarreraEmbarcadero++;

            MutexBarreraEmbarcadero.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public void AñadirBarreraPlaya() {

        try {

            BarreraPlaya.acquire();
            MutexBarreraPlaya.acquire();
            // Añado una persona a la barca
            NBarreraPlaya++;

            MutexBarreraPlaya.release();

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

        int local_embarcadero = 0;
        int local_barca = 0;

        try {

            MutexBarreraBarca.acquire();
            MutexBarreraEmbarcadero.acquire();
            // Añado una persona a la barca
            local_embarcadero = NBarreraEmbarcadero;
            local_barca = NPERSONASBARCA;

            MutexBarreraEmbarcadero.release();
            MutexBarreraBarca.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        if (local_embarcadero != 0) {

            if (local_embarcadero == Veiga.CAPACIDAD_BARCA) {

                for (int i = 0; i < Veiga.CAPACIDAD_BARCA; i++) {

                    try {

                        MutexBarreraBarca.acquire();
                        MutexBarreraEmbarcadero.acquire();

                        // Añado una persona a la barca
                        local_barca = NPERSONASBARCA++;
                        local_embarcadero = NBarreraEmbarcadero--;
                        BARCA.acquire();

                        MutexBarreraEmbarcadero.release();
                        MutexBarreraBarca.release();

                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }

                }

            }

            if (local_embarcadero == 1) {

                try {

                    MutexBarreraBarca.acquire();
                    MutexBarreraEmbarcadero.acquire();

                    // Añado una persona a la barca
                    local_barca = NPERSONASBARCA++;
                    local_embarcadero = NBarreraEmbarcadero--;
                    BARCA.acquire();

                    MutexBarreraEmbarcadero.release();
                    MutexBarreraBarca.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

            }

            if (local_embarcadero == 0) {

                // SAl de la funcionm break;
            }

        }

        trazador.Print("PASO DE EMBARCADERO A BARCA EN BARCA  " + String.valueOf(local_barca) + "pasajeros");
        trazador.Print("EN EMBARCADERO  " + String.valueOf(local_embarcadero) + "pasajeros");

    }

    public static void PlayaABarca() {

        int local_Playa = 0;
        int local_barca = 0;

        try {

            MutexBarreraBarca.acquire();
            MutexBarreraPlaya.acquire();
            // Añado una persona a la barca
            local_Playa = NBarreraEmbarcadero;
            local_barca = NPERSONASBARCA;

            MutexBarreraPlaya.release();
            MutexBarreraBarca.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        if (local_Playa != 0) {

            if (local_Playa == Veiga.CAPACIDAD_BARCA) {

                for (int i = 0; i < Veiga.CAPACIDAD_BARCA; i++) {

                    try {

                        MutexBarreraBarca.acquire();
                        MutexBarreraPlaya.acquire();

                        // Añado una persona a la barca
                        NPERSONASBARCA++;
                        NBarreraPlaya--;
                        BARCA.acquire();

                        MutexBarreraEmbarcadero.release();
                        MutexBarreraBarca.release();

                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }

                }

            }

            if (local_Playa == 1) {

                try {

                    MutexBarreraBarca.acquire();
                    MutexBarreraPlaya.acquire();

                    // Añado una persona a la barca
                    NPERSONASBARCA++;
                    NBarreraPlaya--;
                    BARCA.acquire();

                    MutexBarreraPlaya.release();
                    MutexBarreraBarca.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

            }

            if (local_Playa == 0) {

                // SAl de la funcionm break;
            }

        }

    }

}
