
public class TioAnton extends Hilo {

    private int end_tioAnton = 0;

    public Barca BarcaAnton = new Barca();

    private Trazador trazador = new Trazador(3, "TioAnton");

    public static final int playa = 2;
    public static boolean navegando;
    public static final int embarcadero = 0;

    public static int status = -1;

    public TioAnton() {

    }

    public void run() {

        while (end_tioAnton == 0) {

            trazador.Print("TioAnton");
            Pausa(1000);

            // Estan en el embarcadero.
            EmbarcaderoEspera();

            // quien estan en pausa seria anton.
            // Empezariamos con una pausa, de x tiempo

        }

    }

    public void end() {

        end_tioAnton = 1;

    }

    public void EmbarcaderoEspera() {

        navegando = false;
        status = embarcadero;

        // Procedo a esperar el tiempo declarado. TESPERA.

        Pausa(Veiga.TESPERA_EMBARCADERO);

        // ZaparparBarca // que puede ser de la clase barca.

        ZarpandoBarca();

    }

    public void ZarpandoBarca() {
        navegando = true;

        Pausa(Veiga.TMIN_TRAVESIA, Veiga.TMAX_TRAVESIA);

        // Mientras que este zarpandoBarca, No puede bajarse nadie.

        if (status == embarcadero) {
            PlayaBarca();

        }
        if (status == playa) {

            EmbarcaderoEspera();

        }

    }

    public void PlayaBarca() {
        navegando = false;
        status = playa;
        // Desembarca todo el mundo, y espero otra vez, el tiempo

        Pausa(Veiga.TESPERA_PLAYA);
        // Durante este tiempo otra vez, Puede embarcar gente en la playa.

        // Un una vez pase el tiempo

        ZarpandoBarca();

    }

}

/*
 * esperar un rato en el embarcadero, tenga o no tenga pasajeros.
 * llevar la barca a la isla.
 * alli espera. repite la secuencia, mientras este vivo, la barca
 * solo puede llevar 2 pasajeros, creo que es un semaforo, tipo barrera.
 * 3, estan el tio anton y 2 mas. van y vuelven
 * 
 * 
 * Supongo que hay que esperar que entren en la cueva negra
 * que tiene capacidad para 3 en su interior.
 * 
 * Ambos comienzan en el embarcaero, y tiene un tiepo de espera fijo
 * Tiene un tiempo aleatorio, TMIN_TRAVESIVA. TMAX
 * 
 * Una vez llega tiene un tiempo de espera maximo.
 * Comienza a remar hacia el embarcadero lo que le llva un tiempo aleatorio
 * entre min travessia
 * y max travesia
 * 
 * Repite esto hasta que muere de viejo, por decision del creador hilo principal
 * 
 * 
 * Segun va haciendo las cosas, tio anton va escribiendo lo que va diciendo
 * 
 * 
 * 
 * Tío Antón
 * Inicio: Cuando se inicia la ejecución del hilo.
 * Tío Antón
 * Fin: Cuando termina la ejecución del hilo.
 * Tío Antón
 * En el embarcadero: Cuando llega al embarcadero
 * Tío Antón
 * Yendo con <n> pasajeros: Cuando zarpa hacia la isla
 * Tío Antón
 * Remando: Cuando está en plena travesía
 * Tío Antón
 * En la playa: Cuando llega a la playa
 * Tío Antón
 * Volviendo con <n> pasajeros: Cuando zarpa hacia el embarcadero
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */