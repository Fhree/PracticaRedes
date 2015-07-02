package es.um.redes.Confiable.Intercambio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import es.um.redes.Confiable.App.Client;
import es.um.redes.Confiable.Intercambio.data.Segmento;
import es.um.redes.Confiable.Intercambio.util.RecvBuffer;
import es.um.redes.Confiable.Medio.MediumPacket;
import es.um.redes.Confiable.Medio.MediumSocket;

public class RelieableConnection implements ReliableConnectionIface {

	private final int TIMEOUT = 10;
	
	private int mss;
	private InetAddress destinationIP;
	private InetAddress myIp;
	private Log log;
	private long startTime;
	private RecvBuffer inRcvBuffer;
	private boolean exit; // controlador de si viene una desconexion
	private String lastOpCode = "";
	
	private MediumSocket ms; 
	 /*quizas fuera interesante tener esto como variable global, por 
	 * asegurarnos que cuando mandamos algo por un MS, no fallamos de objeto MS, ademas, para realizar la desconexion
	 * deberemos cerrar el socket un vez recibido el ACK-DISCONNECT, entonces para cerrar algo, deberia ser global */
	
	
	public RelieableConnection(String path){
		try {
			myIp = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("Incapaz de capturar mi IP.");
			e.printStackTrace();
		}
		inRcvBuffer = new RecvBuffer();
		log = new Log(path);
		exit = false;
	}
	
	@Override
	public void connect(InetAddress to) throws IOException {
		InetSocketAddress isa = new InetSocketAddress(to, Client.PORT);
		Segmento segment = new Segmento();
		destinationIP = to;
		System.out.println("origen:"+myIp.getHostAddress());
		System.out.println("destino:"+destinationIP.getHostAddress());
		segment.makeConnect(myIp.getHostAddress(), to.getHostAddress(), mss);
		System.out.println(segment.toString());
		MediumPacket mp = new MediumPacket(segment.toByteArray(),segment.toByteArray().length, isa);
		ms = new MediumSocket();//CONSTRUCCION DEL OBJETO MS PARA EL CLIENTE
		startTime = System.currentTimeMillis();
		ms.send(mp);
		log.send(0, segment, mss);
		do {
			mp = ms.receive();
		} while (mp == null);
		segment.clear();
		segment.fromByteArray(mp.getData());
		try {
			if (segment.getOpcode().equals("ACK")) {
				log.receive(System.currentTimeMillis() - startTime, segment, mss);
				System.out.println(segment.toString());
				System.out.println("El tercer dia mirando al alba ha llegado con un ACK");
			}
		} catch (NullPointerException e) {
			System.out.println("El paquete recibido no es de tipo Ack");
			e.printStackTrace();
		}
	}

	@Override
	public void accept() throws IOException {
		ms = new MediumSocket(Client.PORT); //CONSTRUCCION DEL OBJETO SOCKET, PARA EL SERVIDOR 
		MediumPacket mp = new MediumPacket();
		do {
			mp = ms.receive();
		} while (mp == null);
		Segmento segment = new Segmento();
		segment.fromByteArray(mp.getData());
		if(segment.isChecksumOK()){
			log.receive(System.currentTimeMillis() - startTime, segment, mss);
			destinationIP = InetAddress.getByName(segment.getSourceIP());
			System.out.println("origen:"+myIp.getHostAddress());
			System.out.println("destino:"+destinationIP.getHostAddress());
			try {
				if (segment.getOpcode().equals("CONNECT")) {
					segment.clear();
					segment.makeAck(myIp.getHostAddress(), destinationIP.getHostAddress());
				}
			} catch (NullPointerException e) {
				System.out.println("El paquete recibido no es de tipo Connect");
				e.printStackTrace();
			}
			mp.setData(segment.toByteArray());
			mp.setLength(segment.toByteArray().length);
			ms.send(mp);
			log.send(System.currentTimeMillis() - startTime, segment, mss);
			//listen();
		}
	}

	@Override
	public int getMSS() {
		return mss;
	}

	@Override
	public void setMSS(int mss) {
		this.mss = mss;
	}

	@Override
	public void close() throws IOException {
		ms.
		log.close();
	}

	@Override
	public int getTimeoutValue() {
		return TIMEOUT;
	}

	@Override
	public InetSocketAddress getPeerAddress() {
		return null;
	}

	/*
	 * metodo zen() renombrado a -> listen(), mas que nada porque creo que cuando el profesor revise el codigo
	 * entendera mejor nuestra idea si lee escuchando que zen. Ademas, como he cambiado MediumSocket (por lo dicho arriba)
	 * a global, la he eliminado del argumento.
	 */
	
	private void listen() throws IOException{
		while(!exit){
			read(waitMP(ms).getData());
			write(outRcvBuffer);
		}
			close();
	}
	
	private MediumPacket waitMP(MediumSocket ms) throws IOException {
		MediumPacket mp = new MediumPacket();
		do {
			mp = ms.receive();
		} while (mp == null);
		return mp;
	}
	
	/*
	 * Dado que el ACK que recibimos (inconmingAck) nos pide el siguiente nuemro de secuencia
	 * preparamos el nuemro de secuencia estatico (Segmento.nSeq) al valor pasado por incomingAck
	 */
	private void specifySeqN(int incomingAck){
		Segmento.nSeq = incomingAck;
	}
	
	/*
	 * En este caso es al contrario, dado el numero de secuencia recibido, actualizamos al contrario, para pdoer pedir
	 * el siguiente 
	 */
	private void specifyAckN(int incomingSeq){
		if(incomingSeq == 0)
			Segmento.nAck = 1;
		else if(incomingSeq == 1)
			Segmento.nAck = 0;
	}

	@Override
	public int write(byte[] buffer) throws IOException {
		
		return 0;
	}
	
	/**
	 * Metodo apoyo para write(), englobando el codigo necesario para la escritura en socket.
	 * @param segment
	 * @return int - cantidad de datos escribidos
	 */
	public int writing(Segmento segment){
		MediumPacket mp = new MediumPacket(segment.toByteArray(),segment.toByteArray().length,new InetSocketAddress(destinationIP, Client.PORT));
		try {
			ms.send(mp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return segment.getData_length();
	}
	
	/*
	 * Como se supone que el read lo que hace es preparar el terreno para write, vamos a dejarle todas las
	 * comrpobaciones de envio al write, es decir, cosas del tipo: ¿esta la bandeja de salida vacia? al write.
	 * 
	 * He quitado tu premisa del "AckOk" porque antes de entrar en el estudio de casos, se comprueba si los archivos
	 * estan en orden, un Ack no puede confirmar nada erroneo porqeu:
	 * 	1 - Si se pierde, no llega y pro lo tanto -> timeout
	 *  2 - Si llegara corrupto, al ser corrupto, -> descarte -> timeout
	 *  3 - Si llegara un MSG fuera de orden -> descarte -> timeout
	 */
	
	@Override
	public int read(byte[] buffer) throws IOException {
		
		return 0;
	}

}
