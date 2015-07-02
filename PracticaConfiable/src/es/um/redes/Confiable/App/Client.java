package es.um.redes.Confiable.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.*;

import es.um.redes.Confiable.App.Proto.AppData;
import es.um.redes.Confiable.App.Proto.ByteIO;
import es.um.redes.Confiable.Intercambio.RelieableConnection;

public class Client {
	public static final Integer PORT = 6667;
	public static String ip;
	private static String[] args;
	private int mss = 1500;

	private RelieableConnection rlc;

	public Client() {
		rlc = new RelieableConnection("cliente");
	}

	public void connect(InetAddress to) {
		try {
			rlc.setMSS(mss);
			rlc.connect(to);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			rlc.close();
		} catch (IOException e) {
			System.out.println("Error: client.disconnect()");
			e.printStackTrace();
		}
	}

	public void mainMenu() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader (isr);
		String cmd = "";
			System.out.println("------------- Cliente Iniciado -------------");
			System.out.println("  Seleccione operación introduciendo la letra indicada en cada caso.");
			System.out.println("     - Conectar con una dirección IP (C)");
			System.out.println("     - Salir de la aplicacion (Q)");
			System.out.println("--------------------------------------------");
            System.out.println();
            
			cmd = br.readLine();
			switch (cmd) {
			case "c":
			case "C":
				menuConnect(br);
				break;
			case "q":
			case "Q":
				System.out.println("Nos vemos en la proxima conexión.");
				System.exit(0);
			default:
				System.out.println("Comando no reconocido.");
				break;
            }
    }
    
	public void operationMenu()throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader (isr);
        String cmd = "";
			System.out.println("------------- Cliente Conectado con: " + ip	+ "  -------------");
			System.out.println("  Seleccione operación introduciendo la letra indicada en cada caso.");
			System.out.println("     - Subir un archivo (P)");
			System.out.println("     - Descargar un archivo (G)");
			System.out.println("     - Salir de la aplicacion (Q)");
			System.out.println("--------------------------------------------");
            System.out.println();
        
			cmd = br.readLine();
			switch (cmd) {
			case "g":
			case "G":
				menuDwnldFile();
				break;
			case "p":
			case "P":
				menuUpldFile();
				break;
			case "q":
			case "Q":
				disconnect();
				System.out.println("Nos vemos en la proxima conexión.");
				System.exit(0);
			default:
				System.out.println("Comando no reconocido.");
				break;
			}
		}

	public void uploadFile() {
    //TODO Client Function
	}

	public void menuUpldFile() {
    //TODO Client Function
	}

	public void downloadFile() {
	//TODO Client Function
	}

	public void menuDwnldFile() {
    //TODO Client Function
	}

	public byte[] makeIPfromKB(Pattern pt, BufferedReader br) throws IOException {
		System.out.println("  Introduzca una dirección IP: ");
		String cmd = br.readLine();
		Matcher mat = pt.matcher(cmd);
		byte[] addr = new byte[4];
		if (mat.find()) {
			Integer ipB1, ipB2, ipB3, ipB4;
			ipB1 = Integer.parseInt(mat.group(2));
			ipB2 = Integer.parseInt(mat.group(3));
			ipB3 = Integer.parseInt(mat.group(4));
			ipB4 = Integer.parseInt(mat.group(5));
			// check -> valores de ip en rango
			if ((ipB1 >= 0 && ipB1 < 256) || (ipB2 >= 0 && ipB2 < 256)
					|| (ipB3 >= 0 && ipB3 < 256) || (ipB4 >= 0 && ipB4 < 256)) {
				ip = ipB1 + "." + ipB2 + "." + ipB3 + "." + ipB4;
				addr[0] = ipB1.byteValue();
				addr[1] = ipB2.byteValue();
				addr[2] = ipB3.byteValue();
				addr[3] = ipB4.byteValue();
			} else {
				System.out.println("Valores incorrectos.");
				makeIPfromKB(pt, br);
			}
		}
		return addr;
	}
	
	public byte[] makeIPfromFile(Pattern pt, BufferedReader br) throws IOException {
		String cmd = br.readLine();// XXX arreglar esto, meter todo el fichero de golpe en el string y asi poder 
								   // aplciarle la ER
		Matcher mat = pt.matcher(cmd);
		byte[] addr = new byte[4];
		if (mat.find()) {
			Integer ipB1, ipB2, ipB3, ipB4;
			ipB1 = Integer.parseInt(mat.group(2));
			ipB2 = Integer.parseInt(mat.group(3));
			ipB3 = Integer.parseInt(mat.group(4));
			ipB4 = Integer.parseInt(mat.group(5));
			// check -> valores de ip en rango
			if ((ipB1 >= 0 && ipB1 < 256) || (ipB2 >= 0 && ipB2 < 256)
					|| (ipB3 >= 0 && ipB3 < 256) || (ipB4 >= 0 && ipB4 < 256)) {
				ip = ipB1 + "." + ipB2 + "." + ipB3 + "." + ipB4;
				addr[0] = ipB1.byteValue();
				addr[1] = ipB2.byteValue();
				addr[2] = ipB3.byteValue();
				addr[3] = ipB4.byteValue();
			} else {
				System.out.println("Valores incorrectos. \n"
						+ "Introduce una dirección con un fichero y una Ip validas: ");
				makeIPfromKB(pt, br);
			}
		}
		return addr;
	}

	public void menuConnect(BufferedReader br) throws IOException {
		System.out.println(" - -  Conexión - - ");
		System.out.println("    Elija como conectarse: usando un fichero (F) o \n tecleando una direccion IP (T) o \n localhost (L)\n");
		String cmd = br.readLine();
		Pattern pt = Pattern.compile("((\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}))");
		switch (cmd) {
		case "t":
		case "T":
			//ESCRITURA DE IP DESDE TECLADO-------------------------------------------------------------
			try {
				new Client().connect(InetAddress.getByAddress(makeIPfromKB(pt, br)));
			} catch (UnknownHostException e) {
				System.out.println("Error: llamada client.connect()");
				e.printStackTrace();
			}
			break;
		case "f":
		case "F":
			//LECTURA DESDE FICHERO--------------------------------------------------------------------
			if (args.length == 0) {
				System.out.println("No tenemos ningún fichero con la dirección en nuestros datos \n"
								+ " por favor introduce una ruta de fichero con una dirección:");
				cmd = br.readLine();
				File file = new File(cmd);
				if (file.exists()) {
					FileReader fr = null;
					try {
						fr = new FileReader(file);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					BufferedReader brFile = new BufferedReader(fr);
					try {
						new Client().connect(InetAddress.getByAddress(makeIPfromFile(pt, brFile)));
					} catch (UnknownHostException e) {
						System.out.println("Error: llamada client.connect()");
						e.printStackTrace();
					}	
					
				} else {
					System.out.println("El fichero no existe.");
					menuConnect(br);
				}
				break;
			}
        //LOCAL HOST
		case "l":
		case "L":
			String host = "127.0.0.1";
			new Client().connect(InetAddress.getByName(host));
			break;
		

		default:
			//LECTURA POR ARGS---------------------------------------------------------------------
			Matcher mat;
			String direcIP = "";
			for (String string : args) {
				mat = pt.matcher(string);
				if(mat.find()){
					direcIP = mat.group(2)+"."+mat.group(3)+"."+mat.group(4)+"."+mat.group(5);
					break;
				}
			}
			try {
				new Client().connect(InetAddress.getByAddress(direcIP.getBytes()));
			} catch (UnknownHostException e) {
				System.out.println("Error: llamada client.connect()");
				e.printStackTrace();
			}
		
		}	
	}

	public static void main(String[] args) throws IOException {
		Client.args = args;
		new Client().mainMenu();
	}
}