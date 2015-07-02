package es.um.redes.Confiable.App.Proto.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.um.redes.Confiable.App.Proto.AppData;
import es.um.redes.Confiable.App.Proto.ByteIO;
import es.um.redes.Confiable.Intercambio.ReliableConnectionIface;


public class ProtoTests
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

	@Test(expected=RuntimeException.class)
	public void testOpCodeError()
	{
		new AppData((byte)25, null, null);
	}

	@Test
	public void testOpCodes()
	{
		AppData pack = new AppData(AppData.OP_ERROR, null, null);
		assertNotNull(pack);

		pack = new AppData(AppData.OP_FILE, null, null);
		assertNotNull(pack);

		pack = new AppData(AppData.OP_GET, null, null);
		assertNotNull(pack);

		pack = new AppData(AppData.OP_OK, null, null);
		assertNotNull(pack);

		pack = new AppData(AppData.OP_PUT, null, null);
		assertNotNull(pack);
	}


	/**
	 * Serialización con todos los campos null
	 * @throws IOException
	 */
	@Test
	public void testSerNull() throws IOException
	{
		AppData pack = new AppData(AppData.OP_ERROR, null, null);
		assertNotNull(pack);

		byte[] packed = ByteIO.toByteArray(pack);
		ReliableConnectionIface conn = DummyReaderReliableConnection.create(packed);

		AppData pack_deserialized = ByteIO.fromConnection(conn);
		assertEquals(0, pack_deserialized.getData_length());
		assertEquals(0, pack_deserialized.getFilename_length());
	}

	/**
	 * Serialización con el campo de datos null
	 * @throws IOException
	 */
	@Test
	public void testSerNull2() throws IOException
	{
		AppData pack = new AppData(AppData.OP_OK, "ficherotest.xxx", null);
		assertNotNull(pack);

		byte[] packed = ByteIO.toByteArray(pack);
		ReliableConnectionIface conn = DummyReaderReliableConnection.create(packed);

		AppData pack_deserialized = ByteIO.fromConnection(conn);
		assertEquals(0, pack_deserialized.getData_length());
		assertEquals("ficherotest.xxx", pack_deserialized.getFilename());
	}

	/**
	 * Serialización con el campo de datos null
	 * @throws IOException
	 */
	@Test
	public void testSerNormal() throws IOException
	{
		AppData pack = new AppData(AppData.OP_FILE, null, null);
		pack.setOPCode(AppData.OP_FILE);
		pack.setFilename("ficherotest.xxx");
		pack.setData("0123456789".getBytes());
		assertNotNull(pack);

		byte[] packed = ByteIO.toByteArray(pack);
		ReliableConnectionIface conn = DummyReaderReliableConnection.create(packed);

		AppData pack_deserialized = ByteIO.fromConnection(conn);
		assertEquals(10, pack_deserialized.getData_length());
		assertEquals("ficherotest.xxx", pack_deserialized.getFilename());
		assertArrayEquals("0123456789".getBytes(), pack_deserialized.getData());
	}

	/**
	 * Serialización con el campo de datos null
	 * @throws IOException
	 */
	@Test
	public void testSerNoFilename() throws IOException
	{
		AppData pack = new AppData(AppData.OP_FILE, null, null);
		pack.setOPCode(AppData.OP_FILE);
		pack.setFilename("");
		pack.setData("".getBytes());
		assertNotNull(pack);

		byte[] packed = ByteIO.toByteArray(pack);
		ReliableConnectionIface conn = DummyReaderReliableConnection.create(packed);

		AppData pack_deserialized = ByteIO.fromConnection(conn);
		assertEquals(0, pack_deserialized.getData_length());
		assertEquals("", pack_deserialized.getFilename());
		assertArrayEquals("".getBytes(), pack_deserialized.getData());
	}
}
