import java.util.concurrent.Semaphore;

public class Meigas extends Hilo {

    private static Trazador trazador = new Trazador(5, "Meigas");
    private int Numero_Meiga;
    private static int NumeroMeigasEsperando;
    public static int NEsperandoEnHuerto;
    public static int NesperandoTerminarConjuro;
    public static int nEncargosRecibidosPorMeigas = 0;

    static private Semaphore EsperandoComprobacionEnvio = new Semaphore(0);
    static private Semaphore EsperandoEncargoMeigas = new Semaphore(0);
    static public Semaphore EsperandoHuerto = new Semaphore(0);
    static public Semaphore EsperandoTemrminarConjuro = new Semaphore(0);
    static public Semaphore EntregandoArmasASinforiano = new Semaphore(0);

    static private Semaphore MutexMeigas = new Semaphore(1);
    static public Semaphore MutexHuerto = new Semaphore(1);

    static private Receta EncargoAux;
    public Receta Encargo;

    public Meigas(int n) {

        this.Numero_Meiga = n;

    }

    public void run() {

        /*************************
         * IMPLEMENTACION DE HILOS MEIGAS***********************
         */

        // Inician las 30 meigas esperando en la casa de maruxa.
        // Las 30 se quedan esperando encarco.
        // FALTA WHILE
        EsperandoEncargo();
        // En cuanto Maruxa libera por que tiene un encargo
        // Compruebo ese encargo, y se lo confirmo a Maruxa que lo he recibido por que
        // se quedara ahi
        // Hasta que yo se lo confirme
        ReciboEncargo();

        // BUSCAR INGREDIENTES INDIVIDUALESingredientes
        Pausa(Veiga.TMAX_COCCION);
        HuertoXiana();
        EntregoARMASinforiano();

        // Cuando todas han terminado , el conjuro, se entrega su arma del lote a
        // sinforiano, y
        // se vuelve a esperar a la casa

        /*************************
         * * IMPLEMENTACION DE HILOS MEIGAS
         ********************/

        /****************** PARTE PRUEBAS BARCA *********************************** */

        // TioAnton.AñadirBarreraEmbarcadero();
        // trazador.Print("Meiga" + Numero_Meiga);
        // TioAnton.AñadirBarreraPlaya();
        /****************** PARTE PRUEBAS BARCA *********************************** */
    }

    public void EsperandoEncargo() {
        // Todas las meigas se quedan esperando a que maruxa, tenga un encargo

        try {
            MutexMeigas.acquire();
            NumeroMeigasEsperando++;
            MutexMeigas.release();
            EsperandoEncargoMeigas.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void ReciboEncargo() {

        // Como compruebo que hay uno para mi.
        this.Encargo = EncargoAux;
        // FALTA TRY CATCH MUTEX INTERVIENEN 2HILOS
        try {
            MutexHuerto.acquire();
            nEncargosRecibidosPorMeigas++;
            MutexHuerto.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        EsperandoComprobacionEnvio.release();

    }

    public static void EnvioEncargo(Veiga.Arma Arma, Paso[] receta) {

        EncargoAux.armaActual = Arma;
        EncargoAux.receta_Arma = receta;

        EsperandoEncargoMeigas.release();
        try {
            // Me quedo pillado hasta que me confirmen que han recibido el encargo , y yo
            // continuare, pasando uno nuevo

            EsperandoComprobacionEnvio.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void HuertoXiana() {

        // Nada mas llegar, se suman uno y se pillan
        try {
            MutexHuerto.acquire();
            NEsperandoEnHuerto++;
            MutexHuerto.release();
            EsperandoHuerto.acquire();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Pausa(Veiga.TMIN_CONJURO, Veiga.TMAX_CONJURO);

        try {
            MutexHuerto.acquire();
            NesperandoTerminarConjuro++;
            MutexHuerto.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            EsperandoTemrminarConjuro.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void EntregoARMASinforiano() {

        // Suelto a sinforiano

        // Entrego armas a la funcion de sinforiano
        Sinforiano.EnviarArmaMeigas(this.Encargo);
        EntregandoArmasASinforiano.release();
        // Me quedo pillado hasta que sinforiano, me haga release, confirmando que ha
        // recibido las armas , y
        // Se vuelva a quedar pillado ya que es de 1 en 1.

    }

}
// atributo propio de la clase