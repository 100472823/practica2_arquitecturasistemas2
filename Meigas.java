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
    public static int NumeroDeMandragoras = 0;
    public int NumeroDeMandragorasTotalesEnCadaUna = 0;
    public static int NumeroEsperandoBosque = 0;
    public static int Grupo_Bosque_Actual = 0;
    public static int NumeroEsperandoBosqueSalir = 0;
    public static int NDentroDelBosque = 0;

    static private Semaphore EsperandoComprobacionEnvio = new Semaphore(0);
    static private Semaphore EsperandoEncargoMeigas = new Semaphore(0);
    static public Semaphore EsperandoHuerto = new Semaphore(0);
    static public Semaphore EsperandoTemrminarConjuro = new Semaphore(0);
    static public Semaphore EntregandoArmasASinforiano = new Semaphore(0);
    static public Semaphore EsperandoMeigasASinforiano = new Semaphore(0);
    static public Semaphore BFuenteDoCaño = new Semaphore(1, true);
    static public Semaphore BCuevaNegra = new Semaphore(3, true);
    static public Semaphore BPlazaSanCosme = new Semaphore(3);
    static public Semaphore BBosqueDelLobo = new Semaphore(0);
    static public Semaphore BBosqueDelLoboSalir = new Semaphore(0);

    static private Semaphore MutexMeigas = new Semaphore(1);
    static public Semaphore MutexMeigasVida = new Semaphore(1);
    static public Semaphore MutexHuerto = new Semaphore(1);
    static public Semaphore MutexEntregaSinforiano = new Semaphore(1);
    static public Semaphore MutexFonteDoCaño = new Semaphore(1);
    static public Semaphore MutexCuevaNegra = new Semaphore(1);
    static public Semaphore MutexPlazaSanCosme = new Semaphore(1);
    static public Semaphore MutexNumeroDeMandragoras = new Semaphore(1);
    static public Semaphore MutexEsperandoBosque = new Semaphore(1);
    static public Semaphore MutexEsperandoBosqueSalir = new Semaphore(1);

    static private Receta EncargoAux;
    public Receta Encargo;

    public Meigas(int n) {

        this.Numero_Meiga = n;
        this.trazador = new Trazador(5, "Meiga" + this.Numero_Meiga);

    }

    public void run() {

        this.trazador.Print("Inicio Hilo Meiga n " + String.valueOf(Numero_Meiga));
        /*************************
         * IMPLEMENTACION DE HILOS MEIGAS
         ***********************/

        EsperandoEncargo();

        // En cuanto Maruxa libera por que tiene un encargo
        // Compruebo ese encargo, y se lo confirmo a Maruxa que lo he recibido por

        // se quedara ahi
        // Hasta que yo se lo confirme
        ReciboEncargo();
        while (!this.Encargo.armaActual.name().equals(Veiga.Arma.FIN_SIMULACION.name())) {
            // Dependiendo del ingrediente que necesite, realizare una cosa
            // Realizare otra

            // BUSCAR INGREDIENTES INDIVIDUALESingredientes
            ComprobarNumeroMandragoras();

            ComprobarIngredientes();

            HuertoXiana();
            EntregoARMASinforiano();
            EsperandoEncargo();
            ReciboEncargo();
            // Recibo El nuevo y lo compruebo, Si es el Fin de simulacion

            // Hay que limpiar las variables
            // Para el siguiente Lote

            /*************************
             * * IMPLEMENTACION DE HILOS MEIGAS
             *******************/

        }
        this.trazador.Print("Fin");
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

    public static void FinMeigas(Trazador trazador) {
        // Tengo que despertar a todas las meigas que tenga.
        // Y pasarles el FIN SIMULACION
        for (int i = 0; i < Veiga.N_MEIGAS; i++) {
            EnvioEncargo(Veiga.Arma.FIN_SIMULACION, null);
        }
        trazador.Print("He Avisado a todas las meigas que estaban esperandome");
    }

    public void ComprobarIngredientes() {
        this.trazador.Print("Comprobacion de Ingrediente");
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

        for (int i = 0; i < this.Encargo.receta_Arma.length; i++) {

            if (this.Encargo.receta_Arma[i] == null) {
                // Si el elemento es nulo, sal del bucle
                this.trazador.Print("he terminado de Hacer los Ingredientes");
                break;
            }

            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.PLUMA.name())) {
                trazador.Print("Tengo Como ingrediente: " + this.Encargo.receta_Arma[i].ingrediente.name()
                        + "Voy a la ISLA A POR PLUMAS");
                IslaSoña();

            }

            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.AGUA.name())) {
                trazador.Print("Tengo Como ingrediente: " + this.Encargo.receta_Arma[i].ingrediente.name()
                        + "Voy a la Fonte");

                FonteDoCaño();

            }

            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.GUANO.name())) {
                trazador.Print("Tengo Como ingrediente: " + this.Encargo.receta_Arma[i].ingrediente.name()
                        + "Voy a la CUEVA");

                CuevaNegra();

            }

            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.MANDRAGORA.name())) {
                BosqueDelLobo();
            }

            /********************************
             * PLAZA SAN COSME*********************************************************
             */
            // SOLO HAY 3 NTREBEDES
            // SOLO PUEDEN COCINAR 3 A LA VEZ

            try {
                // Semaforo Capacidad, De 3.
                MutexPlazaSanCosme.acquire();
                MeigasEsperandoACocinarPSC++;
                this.trazador.Print("Estoy en La Plaza San Cosme Esperando A que haya un Trebedes Disponible"
                        + "Hay esperando" + MeigasEsperandoACocinarPSC + "Hay cocinando" + MeigasCocinandoPSC);
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
                this.trazador.Print("Estoy Dentro ya tengo un Trebedes Disponible" + "Hay MeigasCocinando actual"
                        + MeigasCocinandoPSC);
                MutexPlazaSanCosme.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.trazador.Print("Estoy Cocinando");

            Pausa(this.Encargo.receta_Arma[i].tiempo_de_coccion);

            try {
                MutexPlazaSanCosme.acquire();
                MeigasCocinandoPSC--;
                MutexPlazaSanCosme.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.trazador.Print("He terminado de cocinar" + this.Encargo.receta_Arma[i].ingrediente.name()
                    + "Con un tiempo de Coocion de" + this.Encargo.receta_Arma[i].tiempo_de_coccion);
            BPlazaSanCosme.release();
        }

    }

    public void IslaSoña() {

        this.trazador.Print("De Camino A la Isla");
        TioAnton.AñadirBarreraEmbarcadero();
        /* Me baja en la playa */
        this.trazador.Print("Ya estoy en la Isla, Comiendo a Recolectar Pluma");
        Pausa(Veiga.TMIN_RECOLECTAR_PLUMA, Veiga.TMAX_RECOLECTAR_PLUMA);
        this.trazador.Print("He Terminado De Recolectar Pluma");

        this.trazador.Print("Ya tengo la pluma voy de vuelta al embarcadero");
        TioAnton.AñadirBarreraPlaya();

    }

    public void FonteDoCaño() {
        // Semaforo de Capacidad 1

        /* Acercarse a la fuente y Esperar su turno a servirse */
        this.trazador.Print("Me Acerco a la Fuente");
        try {
            MutexFonteDoCaño.acquire();
            MeigasEsperandoFDC++;

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
        this.trazador.Print("Recolectando Agua");
        Pausa(Veiga.TMIN_RECOLECTAR_AGUA, Veiga.TMAX_RECOLECTAR_AGUA);
        this.trazador.Print("He terminado de Recolectar Agua");
        try {
            MutexFonteDoCaño.acquire();
            MeigasDentroFDC--;
            MutexFonteDoCaño.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BFuenteDoCaño.release();
    }

    public void CuevaNegra() {
        // Cambiar Semaforo de Capacidad de 3
        // Esperar Turno Siguiendo la Ley de Mallas
        // MAX3, La primera Comprueba que haya 0 Personas
        // No es que Tenga que esperar a que haya 3 Meigas
        // Si no que mientras pueda entrar no hay problema
        try {
            MutexCuevaNegra.acquire();
            MeigasEsperandoCN++;
            this.trazador.Print("Hay Dentro" + MeigasDentroCN + "Hay esperando" + MeigasEsperandoCN);
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
            this.trazador.Print("Estoy Dentro de la Cueva Negra");
            this.trazador.Print("Hay Dentro" + MeigasDentroCN + "Hay esperando" + MeigasEsperandoCN);
            MutexCuevaNegra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.trazador.Print("Empiezo a Recolectar Guano");
        // Dentro de la Cueva solo puede haber 3
        Pausa(Veiga.TMIN_RECOLECTAR_GUANO, Veiga.TMAX_RECOLECTAR_GUANO);
        this.trazador.Print("Ya he recolectado el guano");
        // Salir de la Cueva

        try {
            MutexCuevaNegra.acquire();
            MeigasDentroCN--;
            this.trazador.Print("he Terminado Salgo de la Cueva, Dejo Libre para los demas");
            MutexCuevaNegra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BCuevaNegra.release();
    }

    public void BosqueDelLobo() {

        // Primero Debe Cruzar de Sur a Norte El puente
        PonteDePedra.EsperandoSur(this.trazador);
        // Una vez Cruzado El puente
        // Al llegar al bosque, si no hay meigas suficientes es decir esperando 7, no
        // De Normal Necesitamos tener Un grupo de 7;
        try {
            MutexEsperandoBosque.acquire();
            NumeroEsperandoBosque++;
            this.trazador.Print("Acabo de llegar al Bosque Esperando al grupo");
            MutexEsperandoBosque.release();
            /* Comprobacion De Limite, Para grupo */
            MutexEsperandoBosque.acquire();
            if (NumeroDeMandragoras < Veiga.GRUPO_BOSQUE) {
                // Tendre que Esperar A NumeroDeMandragoras
                // Por que En este Momento, El limite Es Menor que 7
                Grupo_Bosque_Actual = NumeroDeMandragoras;

            } else if (NumeroDeMandragoras >= Veiga.GRUPO_BOSQUE) {

                // Entonces, Si es Mayor, Entonces Tendremos que Esperar Al Bosque

                Grupo_Bosque_Actual = Veiga.GRUPO_BOSQUE;
            }
            if (NumeroEsperandoBosque == Grupo_Bosque_Actual && NDentroDelBosque == 0) {

                this.trazador.Print("Se puede formar un grupo de " + Grupo_Bosque_Actual);
                BBosqueDelLobo.release(Grupo_Bosque_Actual);
                // Numero de gente que esta esperando- El numero de gente, que va a entrar
                NumeroEsperandoBosque = NumeroEsperandoBosque - Grupo_Bosque_Actual;
            }

            MutexEsperandoBosque.release();

            BBosqueDelLobo.acquire();
            // Si le digo que Si Hay gente Dentro, No puede hacer Release
            // Tendra que Hacerlo El que Sale Del Bosque??
            // Dudas

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /* DENTRO DEL BOSQUE */
        // No Puede Haber Mas De 7 Personas; dentro del bosque
        // Duda es, si la primera meiga que tiene mandragora, va a pensar que solo esta
        // ella,
        // y tdavia no se han enviado los encargos

        try {
            MutexEsperandoBosque.acquire();
            NDentroDelBosque++;
            this.trazador.Print("Tenemos un Grupo de: " + NDentroDelBosque);
            MutexEsperandoBosque.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.trazador.Print("Voy a Cruzar El Bosque");
        Pausa(Veiga.TMIN_BOSQUE, Veiga.TMAX_BOSQUE);
        this.trazador.Print("Ya he terminado De Cruzar El bosque");
        // Ya estan en el claro,
        // Desenterrar la raiz de Mandragora, lo que le lleva a cada una
        // un tiempo distinto Aleatorio,d e TMIN, y TMAX.
        this.trazador.Print("Empiezo A buscar la Mandragora");
        Pausa(Veiga.TMIN_RECOLECTAR_MANDRAGORA, Veiga.TMAX_RECOLECTAR_MANDRAGORA);
        this.trazador.Print("Ya tengo la Mandragora");
        // Se reunen con Sus compas para salir
        try {
            MutexEsperandoBosque.acquire();
            NumeroEsperandoBosqueSalir++;
            this.trazador.Print("Estoy esperando para Salir");
            if (NumeroEsperandoBosqueSalir == NDentroDelBosque) {
                this.trazador.Print("Estamos Esperando para salir" + NumeroEsperandoBosqueSalir);
                BBosqueDelLoboSalir.release(NumeroEsperandoBosqueSalir);
                NDentroDelBosque = 0;
                NumeroEsperandoBosqueSalir = 0;
                // BBosqueDelLobo.release(Grupo_Bosque_Actual);
                // tendria que Ser asi que si hay alguien esperando
                // Entonces lo Meto,Pero si el grupo es menor, Eso se debe de COmprobar antes
                // Entonces sabriamos que no tenemos no deberia de llegar ninguno mas, Si no lo
                // comprobnariamos Es decir que seamos 3 y llegue un 4, directamente lo
                // tendriamos que esperar
                // Pero si a los que estan fuera, y estan esperando por que han visto que hay un
                // grupo ya dentro, Supongo Que sera Grupo ya de7
                // Release???

                if (Grupo_Bosque_Actual == NumeroEsperandoBosque) {
                    this.trazador.Print("Hay gente esperando porque habia un grupo dentro " + Grupo_Bosque_Actual);
                    BBosqueDelLobo.release(Grupo_Bosque_Actual);
                    // Numero de gente que esta esperando- El numero de gente, que va a entrar
                    NumeroEsperandoBosque = NumeroEsperandoBosque - Grupo_Bosque_Actual;

                }

            }
            MutexEsperandoBosque.release();
            BBosqueDelLoboSalir.acquire();
            // Acquire
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.trazador.Print("Ya hemos salido del Bosque");

        /* CALCULO PARA LOS SIGUIENTES VIAJES */
        // Si La meiga, Que comprueba Esta condicion, Solo tiene que ir, A por
        // mandragora,
        // Una unica vez, la restamos del Numero de Meigas que Van a ir a por
        // mandragoras
        // y Dejamos El Numero de Veces en 0;
        // En el Caso de que la Megia va a volver, no restamos de que tenga que ir a por
        // mandragora
        // Pero si que restamos de El numero propio Total de cada una
        if (this.NumeroDeMandragorasTotalesEnCadaUna == 1) {
            try {
                MutexEsperandoBosque.acquire();
                NumeroDeMandragoras--;
                this.trazador.Print("No tengo que volver a por mas mandragoras ahora hay" + NumeroDeMandragoras);
                MutexEsperandoBosque.release();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Esta Meiga, ya no tendra que Volver,
            this.NumeroDeMandragorasTotalesEnCadaUna = 0;

        }

        if (this.NumeroDeMandragorasTotalesEnCadaUna > 1) {
            // En este Caso La meiga, tendra que volver, Por lo que, Seguir en el Numero
            // De Mandragoras; que solo suma 1, por Elementos
            this.NumeroDeMandragorasTotalesEnCadaUna--;

        }

        PonteDePedra.EsperandoNorte(this.trazador);

        // cuando estan lo vuelven a cruzar el bosque
        // Cruzan el Puente Norte A Sur

    }

    public void ComprobarNumeroMandragoras() {
        // Tengo Que Comprobar En todo El array De Encargos, De Todas Las Meigas
        // Tengo que obligarlas Que lo Comprueben Si no, No voy a Saber Nunca El numero
        // de Meigas Unicas
        // Las que Esten Despiertas Por que Tienen Encargo
        for (int i = 0; i < this.Encargo.receta_Arma.length; i++) {
            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.MANDRAGORA.name())) {
                try {
                    MutexNumeroDeMandragoras.acquire();
                    NumeroDeMandragoras++;
                    this.trazador.Print("Tiene Mandragora Sumo " + " " + NumeroDeMandragoras);
                    MutexNumeroDeMandragoras.release();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Paso Al siguiente
                // Este Array, Contiene los ingredientes de una receza, En cuento Encuentre,
                // Mandragora, Quiero que Sume 1 y que termie
                break;
            }

        }

        // Las que Esten Despiertas Por que Tienen Encargo
        for (int i = 0; i < this.Encargo.receta_Arma.length; i++) {
            if (this.Encargo.receta_Arma[i].ingrediente.name().equals(Paso.Ingrediente.MANDRAGORA.name())) {
                try {
                    MutexNumeroDeMandragoras.acquire();
                    this.NumeroDeMandragorasTotalesEnCadaUna++;
                    this.trazador.Print("Tiene Mandragora Sumo Contador individual" + " "
                            + this.NumeroDeMandragorasTotalesEnCadaUna);
                    MutexNumeroDeMandragoras.release();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // En este Caso, quiero saber, Cuantos viajes en total van a necesitar hacer las
                // Meigas
                // Para Poder Ir Calculando Luego, Los limites Segun vayan Entrando Meigas Al
                // Bosque

            }

        }

    }

}