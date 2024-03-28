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
    public static int MeigasDentroFDC = 0;
    public static int MeigasEsperandoFDC = 0;
    public static int MeigasDentroCN = 0;
    public static int MeigasEsperandoCN = 0;
    public static int MeigasEsperandoACocinarPSC = 0;
    public static int MeigasCocinandoPSC = 0;

    static private Semaphore EsperandoComprobacionEnvio = new Semaphore(0);
    static private Semaphore EsperandoEncargoMeigas = new Semaphore(0);
    static public Semaphore EsperandoHuerto = new Semaphore(0);
    static public Semaphore EsperandoTemrminarConjuro = new Semaphore(0);
    static public Semaphore EntregandoArmasASinforiano = new Semaphore(0);
    static public Semaphore EsperandoMeigasASinforiano = new Semaphore(0);
    static public Semaphore BFuenteDoCaño = new Semaphore(0, true);
    static public Semaphore BCuevaNegra = new Semaphore(0, true);
    static public Semaphore BPlazaSanCosme = new Semaphore(0);

    static private Semaphore MutexMeigas = new Semaphore(1);
    static public Semaphore MutexMeigasVida = new Semaphore(1);
    static public Semaphore MutexHuerto = new Semaphore(1);
    static public Semaphore MutexEntregaSinforiano = new Semaphore(1);
    static public Semaphore MutexFonteDoCaño = new Semaphore(1);
    static public Semaphore MutexCuevaNegra = new Semaphore(1);
    static public Semaphore MutexPlazaSanCosme = new Semaphore(1);

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
            EntregoARMASinforiano();

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
        Meigas.EntregandoArmasASinforiano.release();

        try {
            MutexEntregaSinforiano.acquire();
            nMeigasTerminadasHanEnviadoASinforiano++;
            this.trazador.Print("Han Entregado Armas" + String.valueOf(nMeigasTerminadasHanEnviadoASinforiano));
            /*
             * CONTEAMOS LAS QUE TERMINAN DE ENVIAR PASA SABER CUANDO TENEMOS QUE PASAR
             * FIN DE LOTE
             */
            if (nEncargosRecibidosPorMeigas == nMeigasTerminadasHanEnviadoASinforiano) {
                this.Encargo.armaActual = Veiga.Arma.FIN_LOTE;
                Sinforiano.RecibirArmaMeigas(this.Encargo);
                Meigas.EntregandoArmasASinforiano.release();
                nMeigasTerminadasEsperando = 0;
                nMeigasTerminadasHanEnviadoASinforiano = 0;
                nEncargosRecibidosPorMeigas = 0;

            }

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MutexEntregaSinforiano.release();

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

    public void ComprobarIngredientes() {

        /*
         * Dependiendo de que Ingrediente es
         * Tendre que ir a un sitio para conseguirlo
         */

        // PARA CADA INGREDEINTE, RECOLECTA
        // ESPERA QUE ALGUNO DE LOS TREDBEDES ESTE LIBRE HAY 3
        // AÑADE EL INGREDEINTE EN LA MARMITA.
        // REMUEVE EL INGREDEINTE POR SU TIEMPO DE COOCION
        // LIBERA EL TREBEDE
        // ESCOGE EL SIGUIENTE Y REPITE

        for (int i = 0; i < Veiga.MAX_INGREDIENTES_RECETA; i++) {

            if (this.Encargo.receta_Arma[i] == null) {
                // Si el elemento es nulo, sal del bucle
                break;
            }

            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.PLUMA.name())) {
                IslaSoña();

            }

            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.AGUA.name())) {
                FonteDoCaño();

            }

            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.GUANO.name())) {
                CuevaNegra();

            }

            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.MANDRAGORA.name())) {

            }

            /********************************
             * PLAZA SAN COSME*********************************************************
             */
            // SOLO HAY 3 NTREBEDES
            // SOLO PUEDEN COCINAR 3 A LA VEZ
            try {
                MutexPlazaSanCosme.acquire();
                MeigasEsperandoACocinarPSC++;
                if (MeigasCocinandoPSC < Veiga.N_TREBEDES) {

                    BPlazaSanCosme.release();

                }
                MutexPlazaSanCosme.release();
                BPlazaSanCosme.acquire();

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                MutexPlazaSanCosme.acquire();
                MeigasCocinandoPSC++;
                MeigasEsperandoACocinarPSC--;
                MutexPlazaSanCosme.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Pausa(this.Encargo.receta_Arma[i].tiempo_de_coccion);

        }

        try {
            MutexPlazaSanCosme.acquire();
            MeigasCocinandoPSC--;
            MutexPlazaSanCosme.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void IslaSoña() {

        TioAnton.AñadirBarreraEmbarcadero();
        /* Me baja en la playa */

        Pausa(Veiga.TMIN_RECOLECTAR_PLUMA, Veiga.TMAX_RECOLECTAR_PLUMA);

        TioAnton.AñadirBarreraPlaya();

    }

    public void FonteDoCaño() {

        /* Acercarse a la fuente y Esperar su turno a servirse */

        try {
            MutexFonteDoCaño.acquire();
            MeigasEsperandoFDC++;
            if (MeigasDentroFDC == 0) {
                BFuenteDoCaño.release();
            }
            MutexFonteDoCaño.release();
            BFuenteDoCaño.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            MutexFonteDoCaño.acquire();
            MeigasEsperandoFDC--;
            MeigasDentroFDC++;
            MutexFonteDoCaño.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Pausa(Veiga.TMIN_RECOLECTAR_AGUA, Veiga.TMAX_RECOLECTAR_AGUA);

        try {
            MutexFonteDoCaño.acquire();
            MeigasDentroFDC--;
            MutexFonteDoCaño.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void CuevaNegra() {

        // Esperar Turno Siguiendo la Ley de Mallas
        // MAX3, La primera Comprueba que haya 0 Personas
        // No es que Tenga que esperar a que haya 3 Meigas
        // Si no que mientras pueda entrar no hay problema
        try {
            MutexCuevaNegra.acquire();
            MeigasEsperandoCN++;
            this.trazador.Print("Hay Dentro" + MeigasDentroCN + "Hay esperando" + MeigasEsperandoCN);
            if (MeigasDentroCN < Veiga.CAPACIDAD_CUEVA) {
                BCuevaNegra.release();
            }
            MutexCuevaNegra.release();
            BCuevaNegra.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            MutexCuevaNegra.acquire();
            MeigasEsperandoCN--;
            MeigasDentroCN++;
            this.trazador.Print("Hay Dentro" + MeigasDentroCN);
            MutexCuevaNegra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Dentro de la Cueva solo puede haber 3
        Pausa(Veiga.TMIN_RECOLECTAR_GUANO, Veiga.TMAX_RECOLECTAR_GUANO);
        // Salir de la Cueva

        try {
            MutexCuevaNegra.acquire();
            MeigasDentroCN--;
            MutexCuevaNegra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void PlazaSanCosme() {

    }
}
// atributo propio de la clase
