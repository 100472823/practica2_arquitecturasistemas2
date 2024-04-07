import java.util.concurrent.Semaphore;

public class PonteDePedra extends Hilo {

    public static final int vacio = 0;
    public static final int CruzandoNorteSur = 1;
    public static final int CruzandoSurNorte = 2;
    public static final int EsperandoNorteS = 3;
    public static final int EsperandoSurS = 4;

    public static int Status = vacio;
    public static int EsperandoNorte = 0;
    public static int EsperandoSur = 0;
    public static int DentroDelPuente = 0;
    public static int Control = 0;
    // VACIO ES QUE EL CONTADOR DEL PUENTE ESTE VACIO

    static private Semaphore MutexPuenteDePedra = new Semaphore(1);
    static public Semaphore BEsperandoNorte = new Semaphore(0);
    static public Semaphore BEsperandoSur = new Semaphore(0);
    /* PARA CRUZAR EL PUENTE SOLO SE PUEDE EN 2 DIRECCIONES, O NORTE SUR O */

    // SOlo se puede en una direccion cada vez

    // Si no Esta ocupado Cruza, y si esta ocupado, Espera que no lo este.
    // Cuando el puente no esta ocupado, Una de las que esta esperando cruza sin
    // Mallas, y Con ella Todas las que estan esperando
    // Y quieren cruar, en la misma direccion. Si llega algunma MAs en cualquir
    // direccion y el puente estara ocupado tendra que esperar

    public static void EsperandoSur(Trazador trazador) {

        trazador.Print("Esperando Para Cruzar SN");
        try {
            MutexPuenteDePedra.acquire();
            EsperandoSur++;
            Status = EsperandoSurS;
            if (DentroDelPuente == vacio) {
                BEsperandoSur.release(EsperandoSur);
            }
            MutexPuenteDePedra.release();
            BEsperandoSur.acquire();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            MutexPuenteDePedra.acquire();
            EsperandoSur = 0;
            MutexPuenteDePedra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        trazador.Print("Cruzando SN");
        CruzoElPuente(trazador);
    }

    public static void EsperandoNorte(Trazador trazador) {

        try {
            MutexPuenteDePedra.acquire();
            EsperandoNorte++;
            trazador.Print("Estoy Esperando para cerzar desde el");
            Status = EsperandoNorteS;
            if (DentroDelPuente == vacio) {
                BEsperandoNorte.release(EsperandoNorte);
            }
            MutexPuenteDePedra.release();
            BEsperandoNorte.acquire();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            MutexPuenteDePedra.acquire();
            EsperandoNorte = 0;
            MutexPuenteDePedra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CruzoElPuente(trazador);
    }

    public static void CruzoElPuente(Trazador trazador) {

        try {
            MutexPuenteDePedra.acquire();
            DentroDelPuente++;
            if (Status == EsperandoNorteS) {

                trazador.Print("Cruzando de NS");

            }
            if (Status == EsperandoSurS) {

                trazador.Print("Cruzando de SN");

            }
            MutexPuenteDePedra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Pausa(Veiga.TMIN_PUENTE, Veiga.TMAX_PUENTE);
        // Ya he cruzado el puente
        if (Status == EsperandoNorteS) {

            trazador.Print("He Cruzado NS");

        }
        if (Status == EsperandoSurS) {

            trazador.Print("He Cruzado SN");

        }
        Status = 0;
        try {
            MutexPuenteDePedra.acquire();

            DentroDelPuente = 0;
            /*
             * Si Hay gente esperando en ambos sitios, le damos prioridad al NORTE
             * En el Caso de que haya gente Solo en el Norte, Esperando Pueden Pasar
             * En El caso de que haya gente en el Sur Esperando, Pueden Pasar Siempre que no
             * haya gente en el Norte
             * 
             */

            /*
             * En el Caso de haya gente en los 2 lados Es decir que sus Colas Sean Distintas
             * de 0 Tiene Preferencia el Norte
             */
            if (EsperandoNorte != 0 && EsperandoSur != 0) {
                BEsperandoNorte.release(EsperandoNorte);
                trazador.Print(
                        "Hay Gente Esperando en El Norte y En el Sur, los Dejo Pasar a Los Del norte que tienen Preferencia");

            }
            // Si solo La cola Del Norte Tiene Gente, Es Decir, La del Sur Puede Tener Gente
            // O no,
            // Entrarian Al Puente Los que Estan Esperando en el Norte
            if (EsperandoNorte != 0) {

                BEsperandoNorte.release(EsperandoNorte);
                trazador.Print("Hay Gente Esperando en El Norte, los Dejo Pasar");

            } // Solo En El Caso Especifico En el Cual;
              // No hay Nadie Esperando En el norte, Y hay gente Esperando en El Sur
              // Entonces Entrarian Los Del Sur
            if (EsperandoSur != 0 && EsperandoNorte == 0) {

                BEsperandoSur.release(EsperandoSur);
                trazador.Print("Hay Gente Esperando en El Sur, los Dejo Pasar");
            }

            MutexPuenteDePedra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        trazador.Print("He terminado de salir del puente");
        // Antes de Salir Si hay Gente Esperando En Alguno de Los Lados
        // Eso si, si hay uno dentro ya no puede entrar nadie mas

    }
    /*
     * NECESITO QUE CUANDO LLEGUE COMPRUEBE SI HAY ALGUIEN USANDO EL PUENTE; SI HAY
     * ENTONCES PUEDEN PASAR LAS QUE ESTEN; HASTA QUE TERMINEN DE CRUZARLOS
     */

}
