package es.um.redes.Confiable.Intercambio.testimpl;
/**
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import es.um.redes.Confiable.Intercambio.ReliableConnectionIface;

/**
 * @author dsevilla
 *
 */
public class ReliableConnectionTCPImpl implements ReliableConnectionIface
{
	private static final int PORT = 8009;
	private Socket socket = null;

	private int _mss = 2000;

	private InputStream is = null;
	private OutputStream os = null;

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#connect(java.net.InetAddress)
	 */
	@Override
	public void connect(InetAddress to) throws IOException
	{
		socket = new Socket(to, PORT);
		is = socket.getInputStream();
		os = socket.getOutputStream();
		socket.setSoTimeout(50); // 50ms timeout.
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#accept()
	 */
	@Override
	public void accept() throws IOException
	{
		ServerSocket server_socket = new ServerSocket();
		try {
			server_socket.bind(new InetSocketAddress(PORT));
			server_socket.setReuseAddress(true);

			socket = server_socket.accept();

			is = socket.getInputStream();
			os = socket.getOutputStream();
			socket.setSoTimeout(50); // 50ms timeout.
		} catch (IOException ext)
		{
			throw ext;
		}
		finally
		{
			try {
				server_socket.close();
			} catch (IOException e)
			{
				// Ignore
			}
		}
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#getMSS()
	 */
	@Override
	public int getMSS()
	{
		return _mss;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#setMSS(int)
	 */
	@Override
	public void setMSS(int mss)
	{
		_mss = mss;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#close()
	 */
	@Override
	public void close() throws IOException
	{
		if (socket != null)
			socket.close();
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#getTimeoutValue()
	 */
	@Override
	public int getTimeoutValue()
	{
		return 1000;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#getPeerAddress()
	 */
	@Override
	public InetSocketAddress getPeerAddress()
	{
		return (InetSocketAddress) socket.getRemoteSocketAddress();
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#write(byte[])
	 */
	@Override
	public int write(byte[] buffer) throws IOException
	{
		os.write(buffer);
		return buffer.length;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#read(byte[])
	 */
	@Override
	public int read(byte[] buffer) throws IOException
	{
		int bytes_read = 0;
		try
		{
			bytes_read = is.read(buffer);
		} catch(SocketTimeoutException toe)
		{
			// Ignored. Simulates non-blocking read
		}

		return bytes_read;
	}
}
