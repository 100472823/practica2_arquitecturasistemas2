
public abstract class Hilo extends Thread {
    public static void Pausa(int duracion) {
        try {
            Thread.sleep(duracion);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(666);
        }
    }

    public static void Pausa(int min, int max) {
        int duracion = min + (int) (Math.random() * (max - min));
        Pausa(duracion);
    }

    public void Join() {
        try {
            join();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(-1);
        }
    }

    // Dejamos duda como implementar adquire, y release.

    // Join
    // adquit
    // release

}