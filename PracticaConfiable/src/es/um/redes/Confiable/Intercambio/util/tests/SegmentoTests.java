/**
 * 
 */
package es.um.redes.Confiable.Intercambio.util.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.um.redes.Confiable.Intercambio.data.*;

/**
 * @author dsevilla
 *
 */
public class SegmentoTests
{
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testSerNoData()
	{
		Segmento s = new Segmento(Segmento.S_ACK | Segmento.S_CONNECT, 0, 0, null);
		
		byte[] bytes = s.toByteArray();
		System.err.println(new String(bytes));
		
		Segmento s2 = new Segmento();
		s2.fromByteArray(bytes);
		
		assertEquals(s.getFlags(),s2.getFlags());
		assertEquals(s.getAck_n(),s2.getAck_n());
		assertEquals(s.getSeq_n(),s2.getSeq_n());
		assertEquals(s.getData_length(), s.getData_length());
	}

	@Test
	public void testSerData()
	{
		Segmento s = 
			new Segmento(Segmento.S_ACK | Segmento.S_DATA, 1, 100, "012345689".getBytes());
		
		byte[] bytes = s.toByteArray();
		System.err.println(new String(bytes));
		
		Segmento s2 = new Segmento();
		s2.fromByteArray(bytes);
		
		assertEquals(s.getFlags(),s2.getFlags());
		assertEquals(s.getAck_n(),s2.getAck_n());
		assertEquals(s.getSeq_n(),s2.getSeq_n());
		assertEquals(s.getData_length(), s.getData_length());
		assertArrayEquals(s.getData(), s2.getData());
	}
}

