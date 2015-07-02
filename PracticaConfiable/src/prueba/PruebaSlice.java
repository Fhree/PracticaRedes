package prueba;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;


public class PruebaSlice {
	public static void main(String[] args) {
		byte[] a = {'h', 'o' , 'l' , '5'};
		ByteBuffer bb = ByteBuffer.wrap(a);
		
		
		bb.position(2);
		ByteBuffer slice = bb.slice();
		
		
		
		CharBuffer charBuffer = StandardCharsets.UTF_8.decode(bb.get(a,1,2));
		String body = charBuffer.toString();
		
		System.out.println(bb.limit());
	}
}
