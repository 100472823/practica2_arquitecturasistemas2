public class Trazador {
    String tabs;

    public Trazador(int ntabs, String etiqueta) {
        tabs = "";
        for (int i = 0; i < ntabs; i++)
            tabs += "\t";
        tabs += "[" + etiqueta + "]";
    }

    public void Print(String s) {
        System.out.printf("%s%s\n", tabs, s);
    }
}
// cuando instancio el hilo, instancio el trazador, con new, trazador, le doy 
// el nombre del hilo y le doy la columba que quiera
