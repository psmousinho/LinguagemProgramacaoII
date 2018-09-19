package TCP;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.ServerSocket;

import Threads.MatrizMultiplication;


public class ServidorTCP {
	
	public static void main(String args[]) {
		
		try {
			ServerSocket server = new ServerSocket(2123);
			
			Socket client = server.accept();
			ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(client.getInputStream());
			
			int tamanho = in.readInt();
			int[][] matA = (int[][]) in.readObject();
			int[][] matB = (int[][]) in.readObject();
			
			int[][] matC = MatrizMultiplication.ByThreadsDivididos(matA,matB,tamanho);
			
			out.writeObject(matC);
			
			out.close();
			in.close();
			client.close();
			server.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}

