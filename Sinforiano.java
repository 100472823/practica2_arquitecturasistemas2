import java.util.Random;
import java.util.concurrent.Semaphore;

public class Sinforiano extends Hilo {

    private static Trazador trazador = new Trazador(1, "Sinforiano");

    public static int NumeroAleatorioLotes_Vida;
    // hace falta mutex?
    private static int LotesCompletosTerminados;

    static private Semaphore BarreraEnvioArmasMeigas = new Semaphore(0);
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
                // Cuando termine de hacer las armas de un lote las procesa
                // y una vez despues de procesarlas,
                // Entonces comprueba
                EsperandoTerminarMeigas();
                while (!EncargoTerminado.armaActual.name().equals(Veiga.Arma.FIN_LOTE.name())) {
                    // Suelto a la meiga
                    // Imprimo la proceso
                    ProcesarArmaMeigas();
                    // Me quedo pillado esperando a la siguiente arma
                    EsperarSiguienteMeiga();
                    trazador.Print("Me han despertado inicio otra vez bucle");
                }
                // Soltar a la que manda FINLOTE
                ProcesarArmaMeigas();

                // La ultima vez, le mandan FINLOTE, procesa, y al comprobar en el while
                // Ahi es cuando sale
                // Se pillan todas las meigas que le van pasando ARMAS
            }

        } catch (InterruptedException e) {

        }
        // Si no hay que hacer mas lotes aviso a maruxa de que me muero
        Marxua.EnvioArma(Veiga.Arma.FIN_SIMULACION);
        trazador.Print("Entrego" + Veiga.Arma.FIN_SIMULACION.name());
        // Cuando termine el while, tengo que pasarle, el FIN SIMULACION
        // UNA vez termine el ultimo lote.
        trazador.Print("Aviso a Tio Anton");

        trazador.Print("Fin Hilo Sinforiano");
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
            trazador.Print("Entrego" + Veiga.Arma.FIN_LOTE.name());
            Marxua.EnvioArma(Veiga.Arma.FIN_LOTE);
            LotesCompletosTerminados++;
            MutexNumeroArmasEnLote.release();

            // Entrego armas hasta fin lote, una vez que termino un lote
            // Tengo que esperar hasta que me devuelvan las armas de una en una
            // Cuando recoga todas las armas entonces enviara el siguiente lote

        } catch (InterruptedException e) {

        }

    }

    /* Las meigas me pasan por argumento Las Armas */
    public static void RecibirArmaMeigas(Receta EncargoCompleto) {

        EncargoTerminado = new Receta(EncargoCompleto.armaActual, EncargoCompleto.receta_Arma);
        // Suelta a sinforiano antes para que pueda continuar, y pilla a la meiga por
        // Imprimo el arma que me ha llegado de encargo.
        // Suelto a la meiga

    }

    /* Proceso El arma Imprimiendolo, y suelto a la Siguiente Meiga */
    public void ProcesarArmaMeigas() {

        trazador.Print("He recibido" + EncargoTerminado.armaActual.name() + "Arma");
        // Me vuelvo a quedar pillado hasta que haya el siguiente arma que procesar.
        // Libero a las meigas
        Meigas.EsperandoMeigasASinforiano.release();

    }

    /* Me quedo pillado hasta que la siguiente meiga me suelte */
    public void EsperarSiguienteMeiga() {
        trazador.Print("Esperando Recibir Armas Completa de Meiga");
        try {
            Meigas.EntregandoArmasASinforiano.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void EsperandoTerminarMeigas() {
        trazador.Print("Esperando que las meigas tengan Armas Para mi");
        try {
            Meigas.EsperandoMeigasASinforiano.release();
            Meigas.EntregandoArmasASinforiano.acquire();
            // LLego suelto a la 1 meiga
            // y me quedo esperando que me avise

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
