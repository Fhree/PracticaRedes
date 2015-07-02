/**
 *
 */
package es.um.redes.Confiable.App.Proto;

import java.io.IOException;
import java.nio.ByteBuffer;

import es.um.redes.Confiable.Intercambio.ReliableConnectionIface;

/**
 *   1b       2b       0 .. long.nombre-1       4b        0 .. long.datos-1
 * +-----+-----------+---------------------+-------------+------------------+
 * |tipo |long.nombre| nombre fichero      | long.datos  | datos/mensaje    |
 * +-----+-----------+---------------------+-------------+------------------+
 */
public class ByteIO
{
	private static final int TIPO_LEN = Byte.SIZE / 8;
	private static final int LONG_NOMBRE_LEN = Short.SIZE / 8;
	private static final int LONG_DATOS_LEN = Integer.SIZE / 8;
	
	public static byte[] toByteArray(AppData d)
	{
		int length = TIPO_LEN 
				+ LONG_NOMBRE_LEN + d.getFilename_length() 
				+ LONG_DATOS_LEN + d.getData_length();
		
		ByteBuffer buf = ByteBuffer.allocate(length);

		// Opcode
		buf.put((byte)d.getOPCode());
		
		// Filename length
		buf.putShort((short)d.getFilename_length());
		
		// Filename
		if (d.getFilename_length() != 0)
			buf.put(d.getFilename().getBytes());

		// Data length
		buf.putInt(d.getData_length());

		// Data/Message
		if (d.getData_length() != 0)
			buf.put(d.getData());
		
		return buf.array();
		
//      // Alternativo:
//		ByteArrayOutputStream baos = new ByteArrayOutputStream(20);
//		DataOutputStream dos = new DataOutputStream(baos);
//
//		try {
//			// Opcode
//			dos.writeByte(d.getOPCode());
//
//			// Filename length
//			dos.writeShort(d.getFilename_length());
//
//			// Filename
//			if (d.getFilename_length() != 0)
//				dos.write(d.getFilename().getBytes());
//
//			// Data length
//			dos.writeInt(d.getFile_length());
//
//			// Data/Message
//			if (d.getFile_length() != 0)
//				dos.write(d.getData());
//
//		} catch (IOException e) {
//			// Ignore. Can't happen
//		}
//
//		return baos.toByteArray();
	}
	
	public static AppData fromConnection(ReliableConnectionIface conn) throws IOException
	{
		ByteBuffer tmpbuffer;

		// Opcode
		byte[] tmp = new byte[1];
		readn(conn, tmp);
		byte opcode = tmp[0];

		// Filename length & filename
		tmp = new byte[Short.SIZE / 8];
		readn(conn, tmp);
		tmpbuffer = ByteBuffer.wrap(tmp);
		int filename_length = tmpbuffer.getShort();
		byte[] filename = new byte[filename_length];
		if (filename_length != 0)
			readn(conn, filename);

		// File length & data
		tmp = new byte[Integer.SIZE / 8];
		readn(conn, tmp);
		tmpbuffer = ByteBuffer.wrap(tmp);
		int file_length = tmpbuffer.getInt();
		byte[] data = new byte[file_length];
		if (file_length != 0)
			readn(conn, data);

		return new AppData(opcode, new String(filename), data);
	}

	public static int readn(ReliableConnectionIface conn, byte[] buf) throws IOException
	{
		int read = 0;
		int remaining = buf.length;
		while (remaining > 0)
		{
			byte[] tmp = new byte[remaining];
			int actuallyread = conn.read(tmp);

			if (actuallyread > 0)
			{
				System.arraycopy(tmp, 0, buf, read, actuallyread);

				read += actuallyread;
				remaining -= actuallyread;
			}
		}

		return read;
	}
}
