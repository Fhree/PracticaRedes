/**
 * 
 */
package es.um.redes.Confiable.App.Proto.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import es.um.redes.Confiable.Intercambio.ReliableConnectionIface;

/**
 * @author dsevilla
 *
 */
public class DummyReaderReliableConnection implements ReliableConnectionIface
{
	private ByteBuffer buffer;
	
	public static DummyReaderReliableConnection create(byte[] _buffer)
	{
		DummyReaderReliableConnection conn = new DummyReaderReliableConnection();
		conn.buffer = ByteBuffer.wrap(_buffer);
		return conn; 
	}
	
	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#connect(java.net.InetAddress)
	 */
	@Override
	public void connect(InetAddress to) throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#accept()
	 */
	@Override
	public void accept() throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#getMSS()
	 */
	@Override
	public int getMSS() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#close()
	 */
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#getTimeoutValue()
	 */
	@Override
	public int getTimeoutValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#getPeerAddress()
	 */
	@Override
	public InetSocketAddress getPeerAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#write(byte[])
	 */
	@Override
	public int write(byte[] buffer) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.ReliableConnectionIface#read(byte[])
	 */
	@Override
	public int read(byte[] buf) throws IOException 
	{
		int returned_bytes = Math.min(buffer.remaining(), buf.length);
		buffer.get(buf, 0, returned_bytes);
		return returned_bytes;
	}

	@Override
	public void setMSS(int mss) {
		// TODO Auto-generated method stub
		
	}

}
