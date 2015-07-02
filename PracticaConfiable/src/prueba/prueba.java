package prueba;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.um.redes.Confiable.Intercambio.data.Segmento;

public class prueba {

	public static void main(String[] args) {
		byte[] data = {'h','o','l','a',' ',(byte) 777,'a','0'};
		String mensaje = "<msg><o>7</o><s>777</s><d>896</d><sq>0</sq><a>1</a></msg><dt>"+data+"</dt>";
		byte[] buf = mensaje.getBytes();

		ByteBuffer bb = ByteBuffer.wrap(buf);
		CharBuffer charBuffer = StandardCharsets.UTF_8.decode(bb);
		String body = charBuffer.toString();
		Pattern pat = Pattern.compile(Segmento.regularExpressions[4]);
		Matcher mat = pat.matcher(body);
		int ackN=0;
		if (mat.find())
			ackN = Integer.parseInt(mat.group(1));

		pat = Pattern.compile(Segmento.regularExpressions[5]);
		mat = pat.matcher(body);
		int seqN=1;
		if (mat.find())
			seqN = Integer.parseInt(mat.group(1));
		
		
		byte[] ata2=new byte[data.length];
		String a = "h";
		pat = Pattern.compile(Segmento.regularExpressions[9]);
		mat = pat.matcher(mensaje);
		if (mat.find())
			ata2 = mat.group(1).getBytes();
		ByteBuffer bb2 = ByteBuffer.wrap(ata2);
		CharBuffer charBuffer2 = StandardCharsets.UTF_8.decode(bb2);		
		a = charBuffer2.toString();
		System.out.println(body);
		System.out.println(ackN);
		System.out.println(seqN);
		for (int i = 0; i < charBuffer2.length(); i++) {
			System.out.println(charBuffer2.charAt(i));
		}
		Segmento seg = new Segmento();
		System.out.println(seg.checksum(mensaje));
	}
	
	
}
