package es.um.redes.Confiable.Intercambio.data;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;
import java.security.*;

public class Segmento implements SegmentIface {

	public static Integer nSeq = 1;
	public static Integer nAck = 1;
	
	public static final String opCodeCNX = "CONNECT";
	public static final String opCodeMSG = "MSG";
	public static final String opCodeACK = "ACK";
	public static final String opCodeDC = "DISCONNECT";
	
	public static final String[] regularExpressions = { "<o>(.*?)</o>", // 0 - Operacion
			"<msg>(.*?)</msg>", // 1 - Mensaje
			"<c>(.*?)</c>", // 2 - Conexion
			"<cq>(.*?)</cq>", // 3 - Cerrar conexion
			"<a>(.*?)</a>", // 4 - Ack
			"<sq>(.*?)</sq>", // 5 - Seq
			"<s>(.*?)</s>", // 6 - Source
			"<d>(.*?)</d>", // 7 - Destination
			"<mss>(.*?)</mss>", // 8 - MSS
			"<dt>(.*?)</dt>", // 9 - Data
			"<dtl>(.*?)</dtl>", // 10 - Data lenght
			"<cks>(.*?)</cks>" };// 11 - Checksum

	private String body = "<msg>"; // el todo
	private byte[] data;
	private int ackN, seqN; // del segmento croncrto

	public Segmento() {
	}

	/**
	 * 
	 * @param data
	 */
	public Segmento(byte[] data) {
		this.data = data;
	}

	@Override
	public int getSeq_n() {
		return seqN;
	}

	@Override
	public int getAck_n() {
		return ackN;
	}

	@Override
	public int getData_length() {
		return data.length;
	}

	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public byte[] toByteArray() {
		return body.getBytes();
		/*
		 * Byte bOperation = data[0]; Integer i = bOperation.intValue();
		 * addMark(i.toString(), 1); // operacion
		 * addMark(Client.PORT.toString(), 6); // emisor
		 * addMark(Client.PORT.toString(), 7); // receptor if (Segmento.nSeq ==
		 * 0) { // secuencia Segmento.nSeq = 1; seqN = nSeq;
		 * addMark(Segmento.nSeq.toString(), 5); } else { Segmento.nSeq = 0;
		 * seqN = nSeq; addMark(Segmento.nSeq.toString(), 5); } if (i == 3) { //
		 * ack
		 * -------------------------------------------------------------------
		 * if (Segmento.nAck == 0) { // ack Segmento.nAck = 1; ackN =
		 * Segmento.nAck; addMark(Segmento.nAck.toString(), 5); } else {
		 * Segmento.nAck = 0; ackN = Segmento.nAck;
		 * addMark(Segmento.nAck.toString(), 5); } addMark("0", 10); // longitud
		 * } else if ((i == 1) || (i == 2) || (i == 4) || (i == 5)) { //
		 * msg---------------------- if (Segmento.nAck == 0) { // ack
		 * Segmento.nAck = 1; ackN = Segmento.nAck;
		 * addMark(Segmento.nAck.toString(), 5); } else { Segmento.nAck = 0;
		 * ackN = Segmento.nAck; addMark(Segmento.nAck.toString(), 5); } Integer
		 * aux = data.length; // longitud addMark(aux.toString(), 10);
		 * addMark(data.toString(), 9); // datos } // los mensajes de tipo
		 * desconexion son tratados con los campos comunes, // no tienen ninguno
		 * especial.
		 * 
		 * // Checksum String bodyAux = body.concat("</msg>"); //Genera un
		 * cuerpo de mensaje auxiliar falto de las etiquetas chks
		 * addMark(checksum(bodyAux), 11); //Calcula el checksum y lo incrusta
		 * en el mensaje
		 * 
		 * body = body.concat("</msg>"); return body.getBytes();
		 */
	}

	@Override
	public void fromByteArray(byte[] buf) {
		ByteBuffer bb = ByteBuffer.wrap(buf);
		CharBuffer charBuffer = StandardCharsets.UTF_8.decode(bb);
		body = charBuffer.toString();
	}

	/**
	 * @brief Funcion que calcula el checksum de un string.
	 * @param s
	 *            String a calcular su checksum
	 * @return checksum en MD5 de la cadena
	 */
	public String checksum(String s) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(s.getBytes(), 0, s.length());
			BigInteger i = new BigInteger(1, m.digest());
			return String.format("%1$032x", i);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Dado un segmento comprueba si tiene el checksum correcto. 
	 * @return boolean
	 */
	public boolean isChecksumOK() {
		Pattern pat = Pattern.compile(regularExpressions[11]);
		Matcher mat = pat.matcher(body);
		if (mat.find()) {
			String bodyTochksum = body.replace("<cks>" + mat.group(1) + "</cks>", "");
			if (checksum(bodyTochksum).equals(mat.group(1))) 
				return true;
			else
				return false;
		}
		return false;
	}

	public void addMark(String str, int mark) {
		String aux = "";
		switch (mark) {
		case 1: // operacion
			aux = "<o>" + str + "</o>";
			body = body + aux;
			break;
		case 2: // conexion
			aux = "<c>" + str + "</c>";
			body = body + aux;
			break;
		case 3: // cerrar conexion
			aux = "<cq>" + str + "<cq>";
			body = body + aux;
			break;
		case 4: // ack
			aux = "<a>" + str + "</a>";
			body = body + aux;
			break;
		case 5: // secuencia
			aux = "<sq>" + str + "</sq>";
			body = body + aux;
			break;
		case 6: // origen
			aux = "<s>" + str + "</s>";
			body = body + aux;
			break;
		case 7: // destino
			aux = "<d>" + str + "</d>";
			body = body + aux;
			break;
		case 8: // mss
			aux = "<mss>" + str + "</mss>";
			body = body + aux;
			break;
		case 9: // datos
			aux = "<dt>" + str + "</dt>";
			body = body + aux;
			break;
		case 10: // datos longitud
			aux = "<dtl>" + str + "<dtl>";
			body = body + aux;
			break;
		case 11: // checksum
			aux = "<cks>" + str + "<cks>";
			body = body + aux;
			break;
		}
	}

	public void clear() {
		this.body = "<msg>"; // el todo
	}

	public void makeConnect(String source, String destination, int mss) {
		addMark(opCodeCNX, 1); // operacion
		addMark(source, 6); // origen
		addMark(destination, 7); // destino
		addMark(Integer.toString(mss), 8); // mss
		addMark("0", 4); // numero de ack
		addMark("0", 5); // numero de secuencia
		addMark("0", 10); // longitud de datos
		String bodyAux = body.concat("</msg>"); // checksum
		addMark(checksum(bodyAux), 11);
		body = body.concat("</msg>");
	}

	public void makeAck(String source, String destination) {
		//TODO ARREGLAR EL NUMERO DE SECUENCIA
		addMark(opCodeACK, 1); // operacion
		addMark(source, 6); // origen
		addMark(destination, 7); // destino
		addMark(String.valueOf(ackN), 4); //ack
		addMark(String.valueOf(nSeq), 5); //seq
		addMark("0", 10); // longitud de datos
		String bodyAux = body.concat("</msg>"); // checksum
		addMark(checksum(bodyAux), 11);
		body = body.concat("</msg>");
	}

	public void makeMsg(String source, String destination) {
		addMark(opCodeMSG, 1); // operacion
		addMark(source, 6); // origen
		addMark(destination, 7); // destino
		addMark(String.valueOf(ackN), 4); //ack
		addMark(String.valueOf(seqN), 5); //seq
		addMark(String.valueOf(data.length), 10); // longitud de datos
		ByteBuffer bb = ByteBuffer.wrap(data);
		CharBuffer charBuffer = StandardCharsets.UTF_8.decode(bb);
		String aux = charBuffer.toString();
		addMark(aux, 9); // datos
		String bodyAux = body.concat("</msg>"); // checksum
		addMark(checksum(bodyAux), 11);
		body = body.concat("</msg>");
	}

	public void makeDisconnect(String source, String destination) {
		addMark(opCodeDC, 1); // operacion
		addMark(source, 6); // origen
		addMark(destination, 7); // destino
		addMark(String.valueOf(ackN), 4); //ack
		addMark(String.valueOf(nSeq), 5); //seq
		addMark("0", 10); // longitud de datos
		String bodyAux = body.concat("</msg>"); // checksum
		addMark(checksum(bodyAux), 11);
		body = body.concat("</msg>");
	}

	public String getOpcode() {
		Pattern pat = Pattern.compile(regularExpressions[0]);
		Matcher mat = pat.matcher(body);
		if (mat.find()) {
			return mat.group(1);
		}
		return null;
	}

	public String getSourceIP() {
		Pattern pat = Pattern.compile(regularExpressions[6]);
		Matcher mat = pat.matcher(body);
		if (mat.find())
			return mat.group(1);
		return null;
	}

	public String getDestinationIP() {
		Pattern pat = Pattern.compile(regularExpressions[7]);
		Matcher mat = pat.matcher(body);
		if (mat.find())
			return mat.group(1);
		return null;
	}

	@Override
	public String toString() {
		return "Segmento = " + body + "]";
	}
}
