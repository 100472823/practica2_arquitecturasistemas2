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

    public void EsperandoSur(Trazador trazador) {

        trazador.Print("Esperando Para Cruzar SN");
        try {
            MutexPuenteDePedra.acquire();
            EsperandoSur++;
            Status = EsperandoNorteS;
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

    public void EsperandoNorte(Trazador trazador) {

        try {
            MutexPuenteDePedra.acquire();
            EsperandoNorte++;
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

    public void CruzoElPuente(Trazador trazador) {

        try {
            MutexPuenteDePedra.acquire();
            DentroDelPuente++;
            MutexPuenteDePedra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Pausa(Veiga.TMIN_PUENTE, Veiga.TMAX_PUENTE);

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
            if (EsperandoNorte != 0 && EsperandoSur != 0) {
                BEsperandoNorte.release(EsperandoNorte);
                Control = 1;
            }
            if (EsperandoNorte != 0 && Control == 0) {

                BEsperandoNorte.release(EsperandoNorte);

            }
            if (EsperandoSur != 0 && Control == 0) {

                BEsperandoSur.release(EsperandoSur);

            }

            MutexPuenteDePedra.release();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Antes de Salir Si hay Gente Esperando En Alguno de Los Lados
        // Eso si, si hay uno dentro ya no puede entrar nadie mas

        // Ya he cruzado el puente
        if (Status == EsperandoNorteS) {

            trazador.Print("He Cruzado NS");

        }
        if (Status == EsperandoSurS) {

            trazador.Print("He Cruzado SN");

        }
        Status = 0;
    }
    /*
     * NECESITO QUE CUANDO LLEGUE COMPRUEBE SI HAY ALGUIEN USANDO EL PUENTE; SI HAY
     * ENTONCES PUEDEN PASAR LAS QUE ESTEN; HASTA QUE TERMINEN DE CRUZARLOS
     */

}
