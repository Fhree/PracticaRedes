package prueba;

import java.util.Scanner;
import java.util.regex.*;

public class pruebaMenu {

	public static void main(String[] args) {
		int ipB1, ipB2, ipB3, ipB4;
		System.out.println("escribe");
		Scanner sc = new Scanner(System.in);
		String prueba = sc.nextLine();
		Pattern pt = Pattern.compile("((\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}))");
		Matcher mat = pt.matcher(prueba);
		if(mat.find()){
		System.out.println(mat.group(2));
		System.out.println(mat.group(3));
		System.out.println(mat.group(4));
		System.out.println(mat.group(5));}
		else{
			System.out.println(mat.find());
		}
		ipB1 = Integer.parseInt(mat.group(2));
		byte k = Byte.parseByte(mat.group(3));
		System.out.println((char)k);
	}
}
