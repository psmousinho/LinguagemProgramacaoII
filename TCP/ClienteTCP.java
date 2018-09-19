package TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

import Threads.MatrizMultiplication;

public class ClienteTCP {
	
	public static void main(String args[]) {
		try {
			int tamanho = Integer.parseInt(args[0]);
			
			Socket s = new Socket("localhost",2123);
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			
			int[][] matA = MatrizMultiplication.gerarMatriz(tamanho,tamanho);
			int[][] matB = MatrizMultiplication.gerarMatriz(tamanho,tamanho);
			
			long inicio = System.currentTimeMillis();
			
			out.writeInt(tamanho);
			out.writeObject(matA);
			out.writeObject(matB);
			
			int[][] matC = (int[][]) in.readObject();
			
			System.out.println(System.currentTimeMillis() - inicio);
			if(tamanho <= 10) {
				MatrizMultiplication.display(matA);
				MatrizMultiplication.display(matB);
				MatrizMultiplication.display(matC);
			}
			
			out.close();
			in.close();
			s.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
}
