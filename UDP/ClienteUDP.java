package UDP;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import Threads.MatrizMultiplication;

public class ClienteUDP {

	public static void main(String[] args) {
		try {
			int tamanho = Integer.parseInt(args[0]);
			
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket in, out;
			
			ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
			ObjectOutputStream OOS = new ObjectOutputStream (BAOS);
			
			 
			int[][] matA = MatrizMultiplication.gerarMatriz(tamanho,tamanho);
			int[][] matB = MatrizMultiplication.gerarMatriz(tamanho,tamanho);
			int[][] matC = new int[tamanho][tamanho];
			
			long inicio = System.currentTimeMillis();
			
			OOS.writeObject(matA);
			byte[] matABytes = BAOS.toByteArray();
			BAOS.reset();
			OOS.writeObject(matB);
			byte[] matBBytes = BAOS.toByteArray();
			BAOS.reset();
			OOS.writeObject(matC);
			byte[] matCBytes = BAOS.toByteArray();
			
			InetAddress server = InetAddress.getByName("localhost");
 
			out = new DatagramPacket(matABytes, matABytes.length, server, 8558);
			socket.send(out);
			out = new DatagramPacket(matBBytes, matBBytes.length, server, 8558);
			socket.send(out);
			
			in = new DatagramPacket(matCBytes, matCBytes.length);
			socket.receive(in);
			
			ByteArrayInputStream BAIS = new ByteArrayInputStream(matCBytes, 0 , matCBytes.length);
		    ObjectInputStream OIS = new ObjectInputStream (BAIS);
		    
		    matC = (int[][]) OIS.readObject();
			
		    System.out.println(System.currentTimeMillis() - inicio);
			if(tamanho <= 10) {
				MatrizMultiplication.display(matA);
				MatrizMultiplication.display(matB);
				MatrizMultiplication.display(matC);
			}
			
			socket.close();
			BAOS.close();
			OOS.close();
			BAIS.close();
			OIS.close();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
