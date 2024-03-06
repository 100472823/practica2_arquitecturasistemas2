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
}