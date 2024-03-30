


#!/bin/bash

# Limpiar la terminal
clear

# Compilar todos los archivos Java
echo "Compilando Barrera.java..."
javac Receta.java
echo "Compilando Hilo.java..."
javac Hilo.java
echo "Compilando Marxua.java..."
javac Marxua.java
echo "Compilando Meigas.java..."
javac Meigas.java
echo "Compilando Sinforiano.java..."
javac Sinforiano.java
echo "Compilando TioAnton.java..."
javac TioAnton.java
echo "Compilando Trazador.java..."
javac Trazador.java
echo "Compilando Paso.java..."
javac Paso.java
echo "Compilando PonteDePedra.java"
javac PonteDePedra.java
echo "Compilando Veiga.java..."
javac Veiga.java

# Ejecutar el programa principal (Veiga.java)
echo "Ejecutando Veiga.java..."
java Veiga 


# Borrar archivos .class
echo "Borrando archivos .class..."
rm -f *.class