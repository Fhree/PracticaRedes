/**
 *
 */
package es.um.redes.Confiable.App.Proto;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que guarda todos los datos de un paquete de aplicación con monoformato
 *
 * @author dsevilla
 *
 */
public class AppData
{
	public static final byte OP_GET = 1; //dame
	public static final byte OP_PUT = 2; //pon 
	public static final byte OP_OK = 3;  // ack
	public static final byte OP_ERROR = 4; //fallo
	public static final byte OP_FILE = 5; //archivo
	public static final byte OP_END = 6; //desconexion

	private static final Byte[] _valid_opcodes = {OP_GET, OP_PUT, OP_OK, OP_ERROR, OP_FILE, OP_END};
	private static final Set<Byte> valid_opcodes =
		Collections.unmodifiableSet(new HashSet<Byte>(Arrays.asList(_valid_opcodes)));

	private byte OPCode;
	private String filename;
	private byte[] data;

	private void _check_opcode(byte opcode)
	{
		if (!valid_opcodes.contains(opcode))
			throw new RuntimeException("Opcode " + opcode + " no es válido.");
	}

	/**
	 * @param oPCode
	 * @param filename
	 * @param file_length
	 * @param data
	 */
	public AppData(byte oPCode, String filename, byte[] data)
	{
		_check_opcode(oPCode);

		OPCode = oPCode;
		this.filename = filename;
		this.data = data;
	}
	public final byte getOPCode() {
		return OPCode;
	}
	public final void setOPCode(byte oPCode) {
		_check_opcode(oPCode);

		OPCode = oPCode;
	}
	public final int getFilename_length() {
		if (filename == null)
			return 0;
		return filename.length();
	}
	public final String getFilename() {
		return filename;
	}
	public final void setFilename(String filename) {
		this.filename = filename;
	}
	public final int getData_length() {
		if (data == null)
			return 0;
		return data.length;
	}
	public final byte[] getData() {
		return data;
	}
	public final void setData(byte[] data) {
		this.data = data;
	}
}
