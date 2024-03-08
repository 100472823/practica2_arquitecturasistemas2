
public class TioAnton extends Hilo {

    private int end_tioAnton = 0;

    private Trazador trazador = new Trazador(3, "TioAnton");

    public void run() {

        while (end_tioAnton == 0) {

            trazador.Print("TioAnton");
            Pausa(1000);

        }

    }

    public void end() {

        end_tioAnton = 1;

    }

}



/*
 *  esperar un rato en el embarcadero, tenga o no tenga pasajeros. 
 * llevar la barca a la isla. 
 * alli espera. repite la secuencia, mientras este vivo, la barca 
 * solo puede llevar 2 pasajeros, creo que es un semaforo, tipo barrera. 
 * 3, estan el tio anton y 2 mas. van y vuelven 
 *  
 * 
 *Supongo que hay que esperar que entren en la cueva negra 
 que tiene capacidad para 3 en su interior. 

Ambos comienzan en el embarcaero, y tiene un tiepo de espera fijo
Tiene un tiempo aleatorio, TMIN_TRAVESIVA. TMAX
 * 
 * Una vez llega tiene un tiempo de espera maximo. 
 * Comienza a remar hacia el embarcadero lo que le llva un tiempo aleatorio entre min travessia 
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
Inicio: Cuando se inicia la ejecución del hilo.
Tío Antón
Fin: Cuando termina la ejecución del hilo.
Tío Antón
En el embarcadero: Cuando llega al embarcadero
Tío Antón
Yendo con <n> pasajeros: Cuando zarpa hacia la isla
Tío Antón
Remando: Cuando está en plena travesía
Tío Antón
En la playa: Cuando llega a la playa
Tío Antón
Volviendo con <n> pasajeros: Cuando zarpa hacia el embarcadero
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */