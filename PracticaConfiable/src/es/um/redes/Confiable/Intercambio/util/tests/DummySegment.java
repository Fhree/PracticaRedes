/**
 * 
 */
package es.um.redes.Confiable.Intercambio.util.tests;

import es.um.redes.Confiable.Intercambio.data.SegmentIface;

/**
 * @author dsevilla
 *
 */
public final class DummySegment implements SegmentIface
{
	private int seq_n;
	private int ack_n;
	private byte[] data;
	
	public DummySegment(int seq_n, int ack_n, byte[] data)
	{
		this.seq_n = seq_n;
		this.ack_n = ack_n;
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.data.SegmentIface#getSeq_n()
	 */
	@Override
	public int getSeq_n() {
		return seq_n;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.data.SegmentIface#getAck_n()
	 */
	@Override
	public int getAck_n() {
		return ack_n;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.data.SegmentIface#getData_length()
	 */
	@Override
	public int getData_length() {
		return data.length;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.data.SegmentIface#getData()
	 */
	@Override
	public byte[] getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.data.SegmentIface#toByteArray()
	 */
	@Override
	public byte[] toByteArray() {
		return null;
	}

	/* (non-Javadoc)
	 * @see es.um.redes.Confiable.Intercambio.data.SegmentIface#fromByteArray(byte[])
	 */
	@Override
	public void fromByteArray(byte[] buf) {
	}

}
