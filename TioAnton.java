import java.util.concurrent.Semaphore;

public class TioAnton extends Hilo {

    private int end_tioAnton = 0;

    private Trazador trazador = new Trazador(3, "TioAnton");

    static private Semaphore BarreraEmbarcadero = new Semaphore(100);
    static private Semaphore BarreraPlaya = new Semaphore(100);
    private static Semaphore BARCA = new Semaphore(2);

    static private Semaphore MutexBarreraEmbarcadero = new Semaphore(1);
    static private Semaphore MutexBarreraPlaya = new Semaphore(1);
    static private Semaphore MutexBarreraBarca = new Semaphore(1);

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

            BajoATierra();
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
            trazador.Print("Vuelvo hacia el embarcadero con " + String.valueOf(NPERSONASBARCA) + "pasajeros");
            Navego();

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

            BarreraEmbarcadero.acquire();
            MutexBarreraEmbarcadero.acquire();
            // Añado una persona a la barca
            NBarreraEmbarcadero++;

            MutexBarreraEmbarcadero.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public static void AñadirBarreraPlaya() {

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

        trazador.Print(String.valueOf(local_embarcadero));

        if (local_embarcadero != 0) {

            if (local_embarcadero > Veiga.CAPACIDAD_BARCA) {

                int Añadimos2 = 2;

                for (int i = 0; i < Añadimos2; i++) {
                    trazador.Print("Entro");
                    try {

                        MutexBarreraBarca.acquire();
                        MutexBarreraEmbarcadero.acquire();

                        // Añado una persona a la barca
                        NPERSONASBARCA++;
                        NBarreraEmbarcadero--;
                        local_embarcadero = NBarreraEmbarcadero;
                        BARCA.acquire();
                        BarreraEmbarcadero.release();

                        MutexBarreraEmbarcadero.release();
                        MutexBarreraBarca.release();

                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                    trazador.Print(String.valueOf(local_barca) + " barca dentrop");
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
                    BarreraEmbarcadero.release();

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

        trazador.Print("PASO DE EMBARCADERO A BARCA EN BARCA  " + String.valueOf(NPERSONASBARCA) + "pasajeros");
        trazador.Print("EN EMBARCADERO  " + String.valueOf(local_embarcadero) + "pasajeros");

    }

    public void PlayaABarca() {

        int local_Playa = 0;
        int local_barca = 0;

        try {

            MutexBarreraBarca.acquire();
            MutexBarreraPlaya.acquire();
            // Añado una persona a la barca
            local_Playa = NBarreraPlaya;
            local_barca = NPERSONASBARCA;

            MutexBarreraPlaya.release();
            MutexBarreraBarca.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        if (local_Playa != 0) {

            if (local_Playa > Veiga.CAPACIDAD_BARCA) {

                int Añadimos2 = 2;

                for (int i = 0; i < 2; i++) {

                    try {

                        MutexBarreraBarca.acquire();
                        MutexBarreraPlaya.acquire();

                        // Añado una persona a la barca
                        NPERSONASBARCA++;
                        NBarreraPlaya--;
                        BARCA.acquire();
                        BarreraPlaya.release();

                        MutexBarreraPlaya.release();
                        MutexBarreraBarca.release();

                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }

                }
                trazador.Print("hasta aqui");
            }

            if (local_Playa == 1) {

                try {

                    MutexBarreraBarca.acquire();
                    MutexBarreraPlaya.acquire();

                    // Añado una persona a la barca
                    NPERSONASBARCA++;
                    NBarreraPlaya--;
                    BARCA.acquire();
                    BarreraPlaya.release();

                    MutexBarreraPlaya.release();
                    MutexBarreraBarca.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }

            }

        }

    }
}
