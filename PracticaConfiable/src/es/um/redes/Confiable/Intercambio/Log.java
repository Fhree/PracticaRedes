package es.um.redes.Confiable.Intercambio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.um.redes.Confiable.Intercambio.data.Segmento;

/*
 * 
 * 
 *  FUNCIONA 100%
 * 
 *
 *	TODO REVISAR ANTES DE ENTREGA 
 * 
 */

public class Log {

	private File file;
	private FileWriter fw;
	private BufferedWriter bw;

	public Log(String path) {
		try {
			file = new File("C:\\Users\\Fhree\\Dropbox\\REDES\\log-" + path + ".txt");
			fw = new FileWriter(file,true);
			bw = new BufferedWriter(fw);
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			bw.write("Inicio del log ("+dateFormat.format(date)+")"+ "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void send(long time, Segmento segment,int mss) {
		try {
			writeFile("Enviado ", time, segment, mss);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receive(long time, Segmento segment,int mss) {
		try {
			writeFile("Recibido ", time, segment, mss);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void timeout(long time) {
		try {
			bw.write("Timeout (t="+time+")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(String input, long time, Segmento segment, int mss) throws IOException {
		switch (segment.getOpcode()) {
		case "CONNECT":
			bw.write(input + "(t= " + time + "): " + segment.getOpcode() + ", seq = "
					+ segment.getSeq_n() + ", mss = " + mss + "\n");
			break;
		case "MSG":
			bw.write(input + "(t= " + time + "): " + segment.getOpcode() + ", seq = "
					+ segment.getSeq_n() + ", len = " + segment.getData_length()
					+ ", ack = " + segment.getAck_n()+ "\n");
			break;
		case "ACK":
			bw.write(input + "(t= " + time + "): " + segment.getOpcode() + ", seq = "
					+ segment.getSeq_n() + ", ack = " + segment.getAck_n()+ "\n");
			break;
		case "DISCONNECT":
			bw.write(input + "(t= " + time + "): " + segment.getOpcode() + ", seq = "
					+ segment.getSeq_n() + ", ack = " + segment.getAck_n()+ "\n");
			break;
		default:
			bw.write("Error al leer segmento."+ "\n");
			break;
		}
	}
	
	public void close(){
		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(long time) {
		try {
			bw.write("Cierre de log en el tiempo : " + time + " ms.\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
