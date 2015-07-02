/**
 *
 */
package es.um.redes.Confiable.App;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import es.um.redes.Confiable.App.Proto.AppData;
import es.um.redes.Confiable.App.Proto.ByteIO;
import es.um.redes.Confiable.Intercambio.ReliableConnectionIface;
import es.um.redes.Confiable.Intercambio.RelieableConnection;

/**
 * @author dsevilla
 *
 */
public class Server
{
	private ReliableConnectionIface conn;

	private int serve_requests()
	{
		// Crear la conexión
		conn = new RelieableConnection("servidor");
		
		
		// Aceptar la conexión
		try {
			conn.accept();
		} catch (IOException e) {
			System.err.println("No se puede conectar");
			return -1;
		}

		// Bucle de peticiones
		boolean endloop = false;
		while (!endloop) try
		{
			AppData paquete = ByteIO.fromConnection(conn);

			switch(paquete.getOPCode())
			{
			case AppData.OP_GET:
				process_get(paquete);
				break;

			case AppData.OP_PUT:
				process_put(paquete);
				break;

			case AppData.OP_END:
				endloop = true;
				break;
			}

		} catch (IOException e) {
			endloop = true;
		}

		System.err.println("Client disconnected.");

		// Cerrar la conexión
		try {
			conn.close();
		} catch (IOException e) {
			System.err.println("Error al desconectar");
			return -1;
		}

		return 0;
	}

	private void process_put(AppData paquete)
	{
		AppData retData;

		// File name
		File origfilename = new File(paquete.getFilename());
		String filename = "received" + File.separator + origfilename.getName();
		File retfile = new File(filename);
		// Crear los directorios si no existen
		retfile.getParentFile().mkdirs();

		try {
			// Write file
			Files.write(retfile.toPath(), paquete.getData());
			retData = new AppData(AppData.OP_OK, filename, null);
		} catch (IOException e) {
			retData = new AppData(AppData.OP_ERROR, filename, "No se puede crear el fichero.".getBytes());
		}

		try {
			conn.write(ByteIO.toByteArray(retData));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void process_get(AppData paquete)
	{
		String filename = paquete.getFilename();
		File retfile = new File(filename);

		long filesize = retfile.length();
		byte[] bytes = new byte[(int) filesize];

		AppData retData;

		try {
			bytes = Files.readAllBytes(retfile.toPath());
			retData = new AppData(AppData.OP_FILE, filename, bytes);
		} catch (IOException e) {
			retData = new AppData(AppData.OP_ERROR, filename, "Error recuperando el fichero.".getBytes());
		}

		try {
			conn.write(ByteIO.toByteArray(retData));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("Servidor de ficheros REDES - Curso 14/15.");

		int result  = (new Server()).serve_requests();
		System.exit(result);
	}

}
