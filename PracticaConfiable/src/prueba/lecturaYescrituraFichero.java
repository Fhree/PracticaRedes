package prueba;

import java.io.*;
import java.util.Scanner;

import es.um.redes.Confiable.Intercambio.Log;
import es.um.redes.Confiable.Intercambio.data.*;

public class lecturaYescrituraFichero {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Introduzca donde almacenar el fichero:");
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine();
		Log log = new Log(path);
		Segmento segment = new Segmento();
		segment.makeAck("casa", "facultad");
		log.send(50, segment, 100);
		log.close();
	}
	
}
