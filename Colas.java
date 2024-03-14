import java.util.concurrent.Semaphore;

public class Colas {

    // Si hay gente en alguna de estas colas, son el orden
    // en el que van a entrar en la barca.
    Semaphore Embarcadero = new Semaphore(100);
    private static int ColaEmbarcadero = 0;
    Semaphore Playa = new Semaphore(100);
    private static int ColaPlaya = 0;
    private static int local_numero_personas_barca = 0;
    private static int local_personas_embarcadero = 0;
    private static int local_personas_playa = 0;

    public Colas() {

    }

    public void AñadirColaEmbarcadero() {

        try {

            Embarcadero.acquire();
            // Añado una persona a la barca
            ColaEmbarcadero++;

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public void SalirColaEmbarcadero(int numero_desembarcos) throws InterruptedException {

        // Añado una persona a la barca
        ColaEmbarcadero--;

        Embarcadero.release(numero_desembarcos);

    }

    public void AñadirColaPlaya() {

        try {

            Playa.acquire();
            // Añado una persona a la barca
            ColaPlaya++;

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public void SalirColaPlaya(int numero_desembarcos) throws InterruptedException {

        // Añado una persona a la barca
        ColaPlaya--;

        Playa.release(numero_desembarcos);

    }

    public void BarcaEnEmbarcadero() {

        if (TioAnton.status == TioAnton.embarcadero) {

            // Comprobamos el numero de personas en la barca
            // Neceisamos un metodo, public que me devuelva, usando trycatch para acceder al
            // numero de
            // La barca. // hace falta hacer adquire????
            local_numero_personas_barca = Barca.GetnPersonas();

            // Sacamos personas embarcadero, esperando.
            local_personas_embarcadero = GetColaEmbarcadero();

            if (local_personas_embarcadero > 0 && local_numero_personas_barca <= 2) {

                if (local_personas_embarcadero == 2 && local_numero_personas_barca == 1) {

                    SalirColaEmbarcadero(2);

                    for (int i = 0; i < 2; i++) {
                        Barca.Embarco();
                    }
                }
                if (local_personas_embarcadero == 1 && local_numero_personas_barca == 2) {

                    SalirColaEmbarcadero(1);

                    Barca.Embarco();

                }

            }

        }

    }

    // Comprobamos el numero de personas en la cola del embarcadero.
    // Si hay personas esperando en el embarcadero
    // pasaran, las que puedan, a la barca
    // Resto quedan esperando en la cola

    public void BarcaEnLaPlaya() {

    }

}