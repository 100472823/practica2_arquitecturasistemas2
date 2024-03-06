public class Barrera {
    
}
//bar = new Semaf(0)
// si estan en 0. 

// cuando alguien hace adquire, se queda esperando que esta en 0, es semaforo
// alguien desde fuera, cambia un semaforo, entra, y decrementa en 1 el contador interno de la barrera
// esto sera un fifo en principio
// abrira, con capacidad 2. Con 2 meigas. 
// avanza y lo siguiente es embarcar


// tenemos un semaforo de capacidad iniciado a 3. 
// entra y disminuye el semaforo, asi hasta que esta en 0. 
// cuando esta a 0 termine, cuando estos que salen. hacen un release. 
