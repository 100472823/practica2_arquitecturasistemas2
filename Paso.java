
public class Paso {

    public Ingrediente ingrediente;
    public int tiempo_de_coccion;

    public enum Ingrediente {
        PLUMA, AGUA, GUANO, MANDRAGORA,SABINA;

        private static Ingrediente[] valores = values();

        public static Ingrediente Aleatorio() {
            int i = (int) (Math.random() * valores.length);
            return valores[i];
        }
    }

}
