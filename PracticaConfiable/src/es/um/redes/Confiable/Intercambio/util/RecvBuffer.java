package es.um.redes.Confiable.Intercambio.util;

import java.nio.ByteBuffer;

import es.um.redes.Confiable.Intercambio.data.SegmentIface;

/**
 * Clase simple que representa un buffer de recepción. Al ser un protocolo parada y espera,
 * el protocolo nunca insertará en el buffer un segmento fuera de orden, con lo que todas
 * las inserciones se hacen al final, y las lecturas al principio.
 *
 */
public class RecvBuffer
{
	protected ByteBuffer buffer;

	public RecvBuffer()
	{
		 buffer = ByteBuffer.allocate(0);
	}

	/**
	 * Consumir buffer de recepción si hay datos recibidos.
	 * El número de bytes leídos es como máximo buf.length
	 * @param buf El buffer donde se acogerán los datos. Tiene que tener el espacio que se quiere obtener
	 * @return El número de bytes leídos, como máximo buf.length
	 */
	public int obtainDataInOrder(byte[] buf)
	{
		int possibleCopy = Math.min(buf.length, buffer.remaining());

		if (possibleCopy != 0)
		{
			buffer.get(buf, 0, possibleCopy);

			if (buffer.remaining() == 0)
				buffer = ByteBuffer.allocate(0);
		}

		return possibleCopy;
	}
	
	public byte[] getSlice(int position, int mss){
		byte[] aux = null;
		//meter en aux la parte a devolver
		buffer.position(position);
		buffer.slice();
		return aux;
	}
	
	/**
	 * Añade un nuevo segmento recibido al Buffer.
	 * @param s el segmento a añadir.
	 */
	public void addReceivedSegment(SegmentIface s)
	{
		int existing = buffer.remaining();
		int newlength = existing + s.getData_length();
		byte[] buf = new byte[newlength];

		if (existing != 0)
			buffer.get(buf, 0, existing);

		System.arraycopy(s.getData(), 0, buf, existing, s.getData_length());

		buffer = ByteBuffer.wrap(buf);
	}

	/**
	 * @return los datos que están disponibles en el buffer (siempre en bytes)
	 */
	public int getAvailableDataLength()
	{
		return buffer.remaining();
	}
	
	public boolean isBufferEmpty(){
		return buffer.hasRemaining();
	}
}