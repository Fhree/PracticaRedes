package es.um.redes.Confiable.Intercambio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Interfaz que especifica los métodos que contiene una conexión confiable
 */
public interface ReliableConnectionIface
{
	/**
	 * Usado por el cliente. 	
	 * @param to
	 * @throws IOException
	 */
	public abstract void connect(InetAddress to) throws IOException;

	
	/**
	 * Usado por el servidor	
	 * @throws IOException
	 */
	public abstract void accept() throws IOException;

	/**
	 * @return El MSS de esta conexión
	 */
	public abstract int getMSS();
	
	/**
	 * Establece el MSS de esta conexión. Se debe llamar antes de llamar a
	 * connect()/accept().
	 * 
	 * @param mss
	 */
	public abstract void setMSS(int mss);
	
	/**
	 * Cierra la conexión
	 * @throws IOException
	 */
	public abstract void close() throws IOException;

	/**
	 * @return El valor del rimeout
	 */
	public abstract int getTimeoutValue();

	/**
	 * @return La dirección de la otra parte
	 */
	public abstract InetSocketAddress getPeerAddress();

	/**
	 * Escribe en la conexión el contenido del buffer
	 * @param buffer
	 * @return El número de bytes escritos
	 * @throws IOException
	 */
	public abstract int write(byte[] buffer) throws IOException;

	/**
	 * Lee nuevos datos. Lee como máximo `buffer.length' datos que estén
	 * disponibles. Puede retornar menos datos de los que le caben al buffer,
	 * lo cual no es un error. Retorna los datos leídos.
	 * @param buffer El buffer donde se insertarán los datos. Debe estar creado
	 * 						previamente con el tamaño deseado 
	 * @return El número de bytes leidos
	 * @throws IOException
	 */
	public abstract int read(byte[] buffer) throws IOException;

}