import java.util.concurrent.Semaphore;

public class Meigas extends Hilo {

    private int Numero_Meiga;
    private Trazador trazador;
    private static int NumeroMeigasEsperando;
    public static int NEsperandoEnHuerto;
    public static int NesperandoTerminarConjuro;
    public static int nEncargosRecibidosPorMeigas = 0;
    private static int vida = 0;
    public static int nMeigasTerminadasEsperando = 0;
    public static int nMeigasTerminadasHanEnviadoASinforiano = 0;

    static private Semaphore EsperandoComprobacionEnvio = new Semaphore(0);
    static private Semaphore EsperandoEncargoMeigas = new Semaphore(0);
    static public Semaphore EsperandoHuerto = new Semaphore(0);
    static public Semaphore EsperandoTemrminarConjuro = new Semaphore(0);
    static public Semaphore EntregandoArmasASinforiano = new Semaphore(0);
    static public Semaphore EsperandoMeigasASinforiano = new Semaphore(0);

    static private Semaphore MutexMeigas = new Semaphore(1);
    static public Semaphore MutexMeigasVida = new Semaphore(1);
    static public Semaphore MutexHuerto = new Semaphore(1);
    static public Semaphore MutexEntregaSinforiano = new Semaphore(1);

    static private Receta EncargoAux;
    public Receta Encargo;

    public Meigas(int n) {

        this.Numero_Meiga = n;
        this.trazador = new Trazador(3, "Meiga" + this.Numero_Meiga);

    }

    public void run() {

        this.trazador.Print("Inicio Hilo Meiga n " + String.valueOf(Numero_Meiga));
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
            // EntregoARMASinforiano();

            // Hay que limpiar las variables
            // Para el siguiente Lote

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
        this.trazador.Print("Esperando encargo de Maruxa");
        try {
            MutexMeigas.acquire();
            NumeroMeigasEsperando++;
            this.trazador.Print("Hay Esperando un Encargo de Maruxa" + String.valueOf(NumeroMeigasEsperando));
            MutexMeigas.release();
            EsperandoEncargoMeigas.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.trazador.Print("Maruxa me ha soltado");
    }

    /* RECIBO ENCARGO DE MARUXA */
    public void ReciboEncargo() {
        this.Encargo = EncargoAux;
        this.trazador.Print("He recibido un encargo" + this.Encargo.armaActual.name());
        // Como compruebo que hay uno para mi.

        try {
            MutexHuerto.acquire();
            // Ya no estoy esperando
            NumeroMeigasEsperando--;
            nEncargosRecibidosPorMeigas++;
            MutexHuerto.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        EsperandoComprobacionEnvio.release();
        this.trazador.Print("He soltado a maruxa");

    }

    /* MARUXA LLAMA A ESTA FUNCION PARA ENVIAR UN ENCARGO A UNA MEIGA */
    public static void EnvioEncargo(Veiga.Arma Arma, Paso[] receta) {

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
        // Comprueban que no son la ultima
        // Si son la ultima, sueltan a NEsperandoHuerto

        try {
            MutexHuerto.acquire();
            NEsperandoEnHuerto++;
            this.trazador.Print("Acabo de llegar al Huerto " + "Hay esperando : " + NEsperandoEnHuerto);
            if (NEsperandoEnHuerto == nEncargosRecibidosPorMeigas + 1) {

                this.trazador.Print("Estamos todas en el huerto");
                this.trazador
                        .Print("Esperando en el huerto" + Meigas.NEsperandoEnHuerto + "Encargos Recibidos por Meigas"
                                + Meigas.nEncargosRecibidosPorMeigas);
                Meigas.EsperandoHuerto.release(Meigas.NEsperandoEnHuerto);
                NEsperandoEnHuerto = 0;

            }
            MutexHuerto.release();
            EsperandoHuerto.acquire();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.trazador.Print("Empiezo a recitar el conjuro");
        Pausa(Veiga.TMIN_CONJURO, Veiga.TMAX_CONJURO);
        this.trazador.Print("He terminado conjuro");
        try {
            MutexHuerto.acquire();
            NesperandoTerminarConjuro++;
            /* SE SUMA UNO MAS POR QUE TB ESPERAMOS A MARUXA */
            if (NesperandoTerminarConjuro == nEncargosRecibidosPorMeigas + 1) {

                this.trazador.Print("Hemos recitado todas");
                this.trazador
                        .Print("Han recitado ya" + Meigas.NesperandoTerminarConjuro + "Encargos Recibidos por Meigas"
                                + Meigas.nEncargosRecibidosPorMeigas);

                EsperandoTemrminarConjuro.release(NesperandoTerminarConjuro);
                NesperandoTerminarConjuro = 0;
                nEncargosRecibidosPorMeigas = 0;
            }
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
            MutexEntregaSinforiano.acquire();
            nMeigasTerminadasEsperando++;
            MutexEntregaSinforiano.release();
            EsperandoMeigasASinforiano.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.trazador.Print("Entrego" + this.Encargo.armaActual.name());

        Sinforiano.RecibirArmaMeigas(this.Encargo);

        try {
            MutexEntregaSinforiano.acquire();
            nMeigasTerminadasHanEnviadoASinforiano++;
            this.trazador.Print("Han Entregado Armas" + String.valueOf(nMeigasTerminadasHanEnviadoASinforiano));
            if (nMeigasTerminadasEsperando == nMeigasTerminadasHanEnviadoASinforiano) {
                this.Encargo.armaActual = Veiga.Arma.FIN_LOTE;
                Sinforiano.RecibirArmaMeigas(this.Encargo);
                nMeigasTerminadasEsperando = 1;
                nMeigasTerminadasHanEnviadoASinforiano = 1;
                MutexEntregaSinforiano.release();
                MutexHuerto.acquire();
                NEsperandoEnHuerto = 0;
                MutexHuerto.release();
            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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