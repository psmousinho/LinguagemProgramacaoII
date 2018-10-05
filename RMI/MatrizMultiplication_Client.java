package RMI;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MatrizMultiplication_Client {
	public static void main(String args[]) {
		
		try {
			
			Context nameContext = new InitialContext();
			MatrizMultiplication multiplicador = (MatrizMultiplication) nameContext.lookup("rmi://127.0.0.1/multiplicador");
			
			int tamanho = 1000;
			int[][] matA = multiplicador.gerarMatriz(tamanho,tamanho);
			int[][] matB = multiplicador.gerarMatriz(tamanho,tamanho);
			
			System.out.println("Sequencial :");
			multiplicador.BySequencial(matA, matB, tamanho);
			//System.out.println("Por Threads :");
			//multiplicador.ByThreads(matA, matB, tamanho);
			System.out.println("Por Threads divididos :");
			multiplicador.ByThreadsDivididos(matA, matB, tamanho);
			System.out.println("por Pool de Threads :");
			multiplicador.ByPoolThreads(matA, matB, tamanho);
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
