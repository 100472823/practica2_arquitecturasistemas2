import java.util.concurrent.Semaphore;

public class Meigas extends Hilo {

    private int Numero_Meiga;
    private static Trazador trazador = new Trazador(5, "Meigas");
    private static int NumeroMeigasEsperando;
    public static int NEsperandoEnHuerto;
    public static int NesperandoTerminarConjuro;
    public static int nEncargosRecibidosPorMeigas = 0;
    private static int vida = 0;

    static private Semaphore EsperandoComprobacionEnvio = new Semaphore(0);
    static private Semaphore EsperandoEncargoMeigas = new Semaphore(0);
    static public Semaphore EsperandoHuerto = new Semaphore(0);
    static public Semaphore EsperandoTemrminarConjuro = new Semaphore(0);
    static public Semaphore EntregandoArmasASinforiano = new Semaphore(0);
    static public Semaphore EsperandoMeigasASinforiano = new Semaphore(0);

    static private Semaphore MutexMeigas = new Semaphore(1);
    static public Semaphore MutexMeigasVida = new Semaphore(1);
    static public Semaphore MutexHuerto = new Semaphore(1);

    static private Receta EncargoAux;
    public Receta Encargo;

    public Meigas(int n) {

        this.Numero_Meiga = n;

    }

    public void run() {

        trazador.Print("Inicio Hilo Meiga n " + String.valueOf(Numero_Meiga));
        /*************************
         * IMPLEMENTACION DE HILOS MEIGAS
         ***********************/

        // FALTA WHILE
        try {
            MutexMeigasVida.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (vida == 0) {
            MutexMeigasVida.release();

            EsperandoEncargo();

            // En cuanto Maruxa libera por que tiene un encargo
            // Compruebo ese encargo, y se lo confirmo a Maruxa que lo he recibido por

            // se quedara ahi
            // Hasta que yo se lo confirme
            ReciboEncargo();

            // Dependiendo del ingrediente que necesite, realizare una cosa
            // Realizare otra

            // BUSCAR INGREDIENTES INDIVIDUALESingredientes
            Pausa(Veiga.TMIN_COCCION, Veiga.TMAX_COCCION);
            HuertoXiana();
            EntregoARMASinforiano();
            // Entran todas Al entregoArmas, que tiene una
            // Barrera, por lo tanto una vez terminen todas
            // Habran terminado el Lote
            EncargoAux.armaActual = Veiga.Arma.FIN_LOTE;
            Sinforiano.RecibirArmaMeigas(EncargoAux);

            /*************************
             * * IMPLEMENTACION DE HILOS MEIGAS
             *******************/
        }

        /****************** PARTE PRUEBAS BARCA *********************************** */

        // TioAnton.AñadirBarreraEmbarcadero();
        // trazador.Print("Meiga" + Numero_Meiga);
        // TioAnton.AñadirBarreraPlaya();
        /****************** PARTE PRUEBAS BARCA *********************************** */
    }

    /* LAS MEIGAS SE QUEDAN PILLADAS ESPERANDO EL ENCARGO DE MARUXA */
    public void EsperandoEncargo() {
        // Todas las meigas se quedan esperando a que maruxa, tenga un encargo
        trazador.Print("Esperando encargo de Maruxa");
        try {
            MutexMeigas.acquire();
            NumeroMeigasEsperando++;
            MutexMeigas.release();
            EsperandoEncargoMeigas.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        trazador.Print("Maruxa me ha soltado");
    }

    public void ReciboEncargo() {
        this.Encargo = EncargoAux;
        trazador.Print("He recibido un encargo" + this.Encargo.armaActual.name());
        // Como compruebo que hay uno para mi.

        // FALTA TRY CATCH MUTEX INTERVIENEN 2HILOS
        try {
            MutexHuerto.acquire();
            nEncargosRecibidosPorMeigas++;
            MutexHuerto.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        EsperandoComprobacionEnvio.release();
        trazador.Print("He soltado a maruxa");

    }

    /* MARUXA LLAMA A ESTA FUNCION PARA ENVIAR UN ENCARGO A UNA MEIGA */
    public static void EnvioEncargo(Veiga.Arma Arma, Paso[] receta) {

        // EncargoAux.armaActual = Arma;
        //
        // EncargoAux.receta_Arma = receta;

        EncargoAux = new Receta(Arma, receta);
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
        trazador.Print("Meiga" + String.valueOf(this.Numero_Meiga) + "Ha terminado conjuro");
        try {
            MutexHuerto.acquire();
            NesperandoTerminarConjuro++;
            MutexHuerto.release();
            EsperandoTemrminarConjuro.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /* ENTREGO ARMA DE 1 A UNA A SINFORIANO y ESPERO QUE LA PROCESE */
    /*
     * FALTA MUTEX EN LA VARIABLE ENCARGO TERMINADO Y ERROR SE MODIFICAN HABRA QUE
     * HACER AQUI UNA BARRERA
     */
    public void EntregoARMASinforiano() {

        try {
            EsperandoMeigasASinforiano.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        trazador.Print("Entrego" + this.Encargo.armaActual.name());

        Sinforiano.RecibirArmaMeigas(this.Encargo);

        // Me quedo pillado hasta que sinforiano, me haga release, confirmando que ha
        // recibido las armas , y
        // Se vuelva a quedar pillado ya que es de 1 en 1.

    }

    public static void MaruxaAvisaMuerte() {
        try {
            MutexMeigasVida.acquire();
            vida = 1;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MutexMeigasVida.release();
    }

}
// atributo propio de la clase