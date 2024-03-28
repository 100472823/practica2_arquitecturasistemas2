
import java.util.concurrent.Semaphore;

public class Marxua extends Hilo {

    private static Trazador trazador = new Trazador(2, "Marxua");

    private static int n;
    private static Veiga.Arma armaActual;

    static private Semaphore MutexArma = new Semaphore(1);
    // Declaramos una barrera, para que cuando tengamos el arma, la procesemos
    // y luego recibamos lo siguiente
    static private Semaphore BarreraArmaEnvio = new Semaphore(0);
    static private Semaphore BarreraArmaRecibo = new Semaphore(0);

    public Marxua() {

    }

    public void run() {

        trazador.Print(" Inicio Hilo Marxua");

        // Esperando recibir Lote
        trazador.Print("Esperando Recibir Lote");
        try {

            BarreraArmaRecibo.acquire();

            while (!armaActual.name().equals(Veiga.Arma.FIN_SIMULACION.name())) {

                while (!armaActual.name().equals(Veiga.Arma.FIN_LOTE.name())) {
                    ConstruirEncargo();
                    SiguienteArma();
                }
                trazador.Print("He recibido FIN LOTE, Me voy al HUERTO");
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

        // Si el arma, es fin de simulacion, tendremos que avisar
        // A todas las meigas
        trazador.Print("Recibo Fin Simulacion");
        Meigas.MaruxaAvisaMuerte();

    }

    private void ConstruirEncargo() {
        // Que haga los ingredientes y Pasos, correspondientes a cada
        // Arma
        trazador.Print("He recibido" + armaActual.name());
        Paso[] receta_Arma = Receta();
        Pausa(Veiga.TESPERA_EMBARCADERO);
        Receta ArmayReceta = new Receta(armaActual, receta_Arma);
        trazador.Print("Entrego Encargo a Meiga");
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

    /* SOLICITO A SINFORIANO SIGUIENTE ARMA */
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

    /* ENVIO ARMA SINFORIANO */

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

    /* GENERACION DE RECETA ARMA */
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

    /* HUERTO XIANA MARUXA */
    public void HuertoXiana() {

        trazador.Print("En el Huerto");
        // Me sumo a la barrera, por que soy la que primero llego

        try {
            Meigas.MutexHuerto.acquire();
            Meigas.NEsperandoEnHuerto++;
            if (Meigas.NEsperandoEnHuerto == Meigas.nEncargosRecibidosPorMeigas + 1) {
                Meigas.EsperandoHuerto.release(Meigas.nEncargosRecibidosPorMeigas);
                trazador.Print("Estamos todas en el huerto");
                trazador.Print("Esperando en el huerto" + Meigas.NEsperandoEnHuerto + "Encargos Recibidos por Meigas"
                        + Meigas.nEncargosRecibidosPorMeigas);
                Meigas.NEsperandoEnHuerto = 0;
            }
            Meigas.MutexHuerto.release();
            Meigas.EsperandoHuerto.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        trazador.Print("Empiezo a recitar el conjuro");
        Pausa(Veiga.TMIN_CONJURO, Veiga.TMAX_CONJURO);
        trazador.Print("He recitado");

        // Aqui comprobaamos que no somos los ultimos, y hacemos el release a todos
        // los que estan esperando
        try {
            Meigas.MutexHuerto.acquire();
            Meigas.NesperandoTerminarConjuro++;
            if (Meigas.NesperandoTerminarConjuro == Meigas.nEncargosRecibidosPorMeigas + 1) {

                trazador.Print("Esperando terminen conjuro" + Meigas.NesperandoTerminarConjuro
                        + "Encargos Recibidos por Meigas"
                        + Meigas.nEncargosRecibidosPorMeigas);
                Meigas.EsperandoTemrminarConjuro.release(Meigas.NesperandoTerminarConjuro);
                trazador.Print("Hemos recitado todas");
                Meigas.NesperandoTerminarConjuro = 0;
            }

            Meigas.MutexHuerto.release();
            Meigas.EsperandoTemrminarConjuro.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
