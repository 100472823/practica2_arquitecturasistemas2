
import java.util.concurrent.Semaphore;

public class Barca {

    // semaforos // barreras y todo ira dentro de la clase barca.

    // semaforos, como solo hay una barca, tb los voy a declarar static.
    Semaphore PERSONABARCA = new Semaphore(2);

    // estas seria la variable que estamos protegiendo que es el numero de personas
    // que hay en la barca actualmente con barrerea,
    // teniendo un limite de 2+ anton
    private static int PERSONASBARCA = 1;

    public Barca() {

    }

    public void Embarco() {

        if (TioAnton.navegando == true) {

            // Devolver que no es posible acceder a la barca en este momentos

        }

        try {

            PERSONABARCA.acquire();
            // Añado una persona a la barca
            PERSONASBARCA++;

        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    // LLamariamos, A la barrera para meter a otra persona

    public void Desembarco(int numero_desembarcos) throws InterruptedException {

        PERSONASBARCA--;
        PERSONABARCA.release(numero_desembarcos);

        // llamariamos a la barrera para sacar personas
    }

    public int GetnPersonas() {

        int return1 = 0;
        try {

            PERSONABARCA.acquire();
            // Añado una persona a la barca
            return1 = PERSONASBARCA;
            PERSONABARCA.release();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        return return1;

    }

}