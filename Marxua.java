import java.net.PasswordAuthentication;
import java.util.concurrent.Semaphore;

public class Marxua extends Hilo {

    private static Trazador trazador = new Trazador(2, "Marxua");

    private static int n;
    private static Veiga.Arma armaActual;
    private static int recibo = 0;

    static private Semaphore MutexArma = new Semaphore(1);
    // Declaramos una barrera, para que cuando tengamos el arma, la procesemos
    // y luego recibamos lo siguiente
    static private Semaphore BarreraArmaEnvio = new Semaphore(0);
    static private Semaphore BarreraArmaRecibo = new Semaphore(0);

    public Marxua() {

    }

    public void run() {

        trazador.Print("Marxua");

        // Esperando recibir arma

        try {

            BarreraArmaRecibo.acquire();

            while (!armaActual.name().equals(Veiga.Arma.FIN_SIMULACION.name())) {

                while (!armaActual.equals(Veiga.Arma.FIN_LOTE.name())) {
                    ConstruirEncargo();
                    SiguienteArma();
                }
                HuertoXiana();

                // COMO se que se ha acabado el lote igualmente antes de terminal la simulacion
                // en el caso
                // En el que fuera necesario, tendremos que ir al huerto de Xiana, y esperar,
                // por
                // Todas las meigas que le damos encargo, habra que tomar cuenta

                SiguienteArma();

            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    private void ConstruirEncargo() {
        // Que haga los ingredientes y Pasos, correspondientes a cada
        // Arma

        Paso[] receta_Arma = Receta();
        Pausa(Veiga.TESPERA_EMBARCADERO);
        Receta ArmayReceta = new Receta(armaActual, receta_Arma);
        Meigas.EnvioEncargo(armaActual, receta_Arma);

        // le paso el objeto de la clase receta, a la meiga
        // tendremos una funcion, y luego maruxa, se queda pillada hasta
        // que la recibe

        // Tenemos Receta que es un array, de pasos.
        // Entonces, lo que tenemos que hacer es que
        // Se genere este array,
        // Y que lo imprima
        // Problema, voy a tener, n ingredientes que es aleatorio, tendre
        // Que saber cuantos ingredientes tengo
        // Para poder imprimirlos.

    }

    public void SiguienteArma() {

        // Liberamos La barrera para que sinforiano
        // pueda agregar la nueva arma recibida
        BarreraArmaEnvio.release();
        try {

            BarreraArmaRecibo.acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public static void EnvioArma(Veiga.Arma armaRecibida) {

        // Para que sinforiano, se quede pillado,
        // Hasta que termine con esta entrega,
        // Y una vez termine con la actual, continue con la siguiente
        try {
            MutexArma.acquire();
            armaActual = armaRecibida;
            MutexArma.release();
            BarreraArmaRecibo.release();
            // trazador.Print("Me quedo pillado, con el adquire");

            BarreraArmaEnvio.acquire();

            // Logica: Entrego el arma, y
            // Se queda esperando
            // Hasta que sea necesario la entrega
            // de la siguiente

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    private Paso[] Receta() {
        trazador.Print("Redactar receta");
        Paso[] pasos;
        // Declaro un Mutex, para N, para que me guarde, Cuantos Pasos hay en
        // La ultima receta, para poder imprimirlo
        n = (int) (Math.random() * (Veiga.MAX_INGREDIENTES_RECETA) + 1);
        Paso paso;
        pasos = new Paso[n];
        for (int i = 0; i < n; i++) {
            paso = new Paso();
            paso.ingrediente = paso.ingrediente.Aleatorio();
            paso.tiempo_de_coccion = (int) (Math.random() * (Veiga.TMAX_COCCION) + 1);
            pasos[i] = paso;
            trazador.Print(paso.ingrediente.name() + "(" + paso.tiempo_de_coccion + ")");
        }
        return (pasos);
    }

    public void HuertoXiana() {

        // Una vez que mando fin lote, me vengo al huerto y espero que lleguen todas las
        // meigas. que tienen encargo

        // Hago una barrera y que vayan sumando hasta que lleguen todas, MeigasEnHuerto
        // == MeigasConEncargo
        // Recitan todas juntas, el conjuto que les lleva un tiempo aleatorio, A CADA
        // UNA.

        try {
            Meigas.MutexHuerto.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (Meigas.NEsperandoEnHuerto != Meigas.nEncargosRecibidosPorMeigas) {
            // Me quedo en bucle comprobando que han llegado todas solo termino
            // Cuando han llegado
        }

        Meigas.EsperandoHuerto.release(Meigas.nEncargosRecibidosPorMeigas);
        Meigas.MutexHuerto.release();

        Pausa(Veiga.TMIN_CONJURO, Veiga.TMAX_CONJURO);
        // Cuando todas y ella han concluido el conjuro
        try {
            Meigas.MutexHuerto.acquire();
            while (Meigas.NesperandoTerminarConjuro != Meigas.nEncargosRecibidosPorMeigas) {
                // Me quedo en bucle comprobando que han llegado todas solo termino
                // Cuando han llegado
            }

            Meigas.EsperandoTemrminarConjuro.release(Meigas.nEncargosRecibidosPorMeigas);
            Meigas.MutexHuerto.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Falta Mutex variables protegida

    }

}
