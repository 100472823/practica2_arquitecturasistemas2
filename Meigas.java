import java.util.concurrent.Semaphore;

public class Meigas extends Hilo {

    private Trazador trazador = new Trazador(5, "Meigas");
    private int Numero_Meiga;

    static private Semaphore EnvioEncargo = new Semaphore(0);
    static private Semaphore EsperandEncargoMeigas = new Semaphore(0);
    Receta Encargo;

    public Meigas(int n) {

        this.Numero_Meiga = n;

    }

    public void run() {

        // EsperandoEncargo();
        // LO que necesite para ese Encargo
        // SiguienteEncargo();

        /*
         * 
         * for (int i = 0; i < 5; i++) {
         * 
         * trazador.Print("Meiga" + Numero_Meiga);
         * Pausa(1000);
         * 
         * }
         * 
         */

        // TioAnton.AñadirBarreraEmbarcadero();
        // TioAnton.AñadirBarreraPlaya();

    }

    public void EsperandoEncargo() {
        // Meigas Esperando Mutex
        try {
            EsperandEncargoMeigas.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void SiguienteEncargo() {

        EnvioEncargo.release();

    }

    public void EnvioEncargo(Veiga.Arma Arma, Paso[] receta) {

        this.Encargo.armaActual = Arma;
        this.Encargo.receta_Arma = receta;
        EsperandEncargoMeigas.release();
        try {
            EnvioEncargo.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
// atributo propio de la clase