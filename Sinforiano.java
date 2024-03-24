import java.util.Random;
import java.util.concurrent.Semaphore;

public class Sinforiano extends Hilo {

    private Trazador trazador = new Trazador(1, "Sinforiano");

    public static int NumeroAleatorioLotes_Vida;
    // hace falta mutex?
    private static int LotesCompletosTerminados;

    static private Semaphore MutexAleatorioLotes_Vida = new Semaphore(1);

    private static int NumeroArmasEnLote;
    static private Receta EncargoTerminado;

    static private Semaphore MutexNumeroArmasEnLote = new Semaphore(1);

    // Habra que hacer un mutex, en el numeroAleatorio de Lotes
    // Entonces dentro de los lotes
    // Eligo numero aleatorio de armas
    // Tipo de ese lote

    public Sinforiano() {

    }

    public void run() {

        trazador.Print(" Inicio Sinforiano");

        NumeroAleatorioDeLotes();

        try {
            MutexAleatorioLotes_Vida.acquire();

            while (NumeroAleatorioLotes_Vida > LotesCompletosTerminados) {

                MutexAleatorioLotes_Vida.release();
                MakeArmas();
                // Comprobar logica con el tema de finlote
                // EncargoRecibido,
            }

        } catch (InterruptedException e) {

        }

        Marxua.EnvioArma(Veiga.Arma.FIN_SIMULACION);
        trazador.Print("Entrego" + Veiga.Arma.FIN_SIMULACION.name());
        // Cuando termine el while, tengo que pasarle, el FIN SIMULACION
        // UNA vez termine el ultimo lote.
    }

    public void NumeroAleatorioDeLotes() {

        // Se realiza una vez indica el numero de los
        // Lotes a realizar antes de que termine la vida
        int min = 1;
        int max = Veiga.MAX_LOTES;

        // Hace falta que tenga un mutex si en principio nadie mas va a acceder a la
        // variable para modificarla.
        try {

            MutexAleatorioLotes_Vida.acquire();
            NumeroAleatorioLotes_Vida = min + (int) (Math.random() * (max - min));
            trazador.Print("n_lotes" + String.valueOf(NumeroAleatorioLotes_Vida));
            MutexAleatorioLotes_Vida.release();
        } catch (InterruptedException e) {

        }

    }

    public void MakeArmas() {

        // Incluye, seleccionar el numero de armas que vamos
        // a producir en el lote actual.
        // Que armas, hay en ese lote.

        int min = 1;
        int max = Veiga.MAX_ARMAS_LOTE;

        try {

            MutexNumeroArmasEnLote.acquire();
            NumeroArmasEnLote = min + (int) (Math.random() * (max - min));
            trazador.Print("n_armas" + String.valueOf(NumeroArmasEnLote));
            MutexNumeroArmasEnLote.release();
        } catch (InterruptedException e) {

        }

        // Elegir el tipo de arma y transferir
        try {

            MutexNumeroArmasEnLote.acquire();

            for (int i = 0; i < NumeroArmasEnLote; i++) {

                Veiga.Arma armaAleatoria = Veiga.Arma.Aleatoria();

                trazador.Print("Entrego" + armaAleatoria.name());
                Marxua.EnvioArma(armaAleatoria);

            }
            // Cuando termine el bucle, tenemos que traspasar el VEIGA.FINLOTE
            Marxua.EnvioArma(Veiga.Arma.FIN_LOTE);
            trazador.Print("Entrego" + Veiga.Arma.FIN_LOTE.name());
            LotesCompletosTerminados++;
            MutexNumeroArmasEnLote.release();

            // EsperoHasta que me avisen de que tienen lotes terminados para mi
            Meigas.EntregandoArmasASinforiano.acquire();
            // Entrego armas hasta fin lote, una vez que termino un lote
            // Tengo que esperar hasta que me devuelvan las armas de una en una
            // Cuando recoga todas las armas entonces enviara el siguiente lote

        } catch (InterruptedException e) {

        }

    }

    public static void EnviarArmaMeigas(Receta EncargoCompleto) {

        EncargoTerminado = EncargoCompleto;
        // Suelta a sinforiano antes para que pueda continuar, y pilla a la meiga por
        // que va de arma en arma
        // Se pilla la meiga por que van de una en una y tiene que procesar el encargo

    }

}
