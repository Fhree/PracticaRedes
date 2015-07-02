/**
 * 
 */
package es.um.redes.Confiable.Intercambio.util.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.um.redes.Confiable.Intercambio.util.RecvBuffer;

/**
 * @author dsevilla
 *
 */
public class RecvBufferTests
{
	private RecvBuffer buffer;
	private static final byte[] _content = "0123456789".getBytes();
	private static final byte[] _tentnoc = "9876543210".getBytes();
	private static final int _content_length = _content.length;
	private static final byte[] _content3 = ("0123456789"+"0123456789"+"0123456789").getBytes();
	private static final byte[] _content2 = ("0123456789"+"0123456789").getBytes();
		
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		buffer = new RecvBuffer();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testTwo()
	{
		assertEquals(0, buffer.getAvailableDataLength());
		
		// Insert a segment
		buffer.addReceivedSegment(new DummySegment(_content_length, 0, _content));
		
		assertEquals(_content_length, buffer.getAvailableDataLength());
		
		// Insert another one
		buffer.addReceivedSegment(new DummySegment(2 * _content_length, 0, _content));
		
		assertEquals(2*_content_length, buffer.getAvailableDataLength());
				
		byte[] recv = new byte[_content2.length];
		// Get the buffer and check the contents
		assertEquals(recv.length, buffer.obtainDataInOrder(recv));
		assertArrayEquals(_content2, recv);		
	}

	@Test
	public void testEmpty()
	{
		assertEquals(0, buffer.getAvailableDataLength());
						
		byte[] recv = new byte[_content2.length];
		// Get the buffer and check the contents
		assertEquals(0, buffer.obtainDataInOrder(recv));
	}

	@Test
	public void testByteByByte()
	{
		// Insert a segment
		buffer.addReceivedSegment(new DummySegment(0, 0, _tentnoc));
		
		byte[] recv = new byte[1];
		int i = 0;

		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _tentnoc[i++]);
		
		assertEquals(0, buffer.getAvailableDataLength());
		assertEquals(0, buffer.obtainDataInOrder(recv));
	}
	
	@Test
	public void testTest3()
	{
		// Insert a segment
		buffer.addReceivedSegment(new DummySegment(0, 0, _content3));
		
		byte[] recv = new byte[1];
		buffer.obtainDataInOrder(recv);
		assertEquals(recv[0], _content3[0]);

		recv = new byte[9];
		assertEquals(recv.length, buffer.obtainDataInOrder(recv));

		assertEquals(2*_content.length, buffer.getAvailableDataLength());

		buffer.addReceivedSegment(new DummySegment(0, 0, _content));
		recv = new byte[_content3.length];
		assertEquals(recv.length, buffer.obtainDataInOrder(recv));
		assertArrayEquals(_content3, recv);
	}
	
	@Test
	public void testPartial()
	{
		// Insert a segment
		buffer.addReceivedSegment(new DummySegment(_content_length, 0, _content));
		
		// Insert another one
		buffer.addReceivedSegment(new DummySegment(2 * _content_length, 0, _content));

		byte[] recv = new byte[_content.length];
		// Get the buffer and check the contents
		assertEquals(recv.length, buffer.obtainDataInOrder(recv));
		assertArrayEquals(_content, recv);
		
		// Insert another one
		buffer.addReceivedSegment(new DummySegment(2 * _content_length, 0, _content));

		recv = new byte[_content2.length];
		assertEquals(recv.length, buffer.obtainDataInOrder(recv));
		assertArrayEquals(_content2, recv);
	}

}

