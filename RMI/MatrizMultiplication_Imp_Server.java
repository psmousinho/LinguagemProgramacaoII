package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class MatrizMultiplication_Imp_Server extends UnicastRemoteObject implements MatrizMultiplication {
	
	protected MatrizMultiplication_Imp_Server() throws RemoteException {
		super();
	}

	public int[][] BySequencial(int[][] matA, int[][]matB, int tamanho) throws RemoteException {
		int[][] matC = new int[tamanho][tamanho];
		
		int soma;
		
		long inicio = System.currentTimeMillis();
		
		for(int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				soma = 0;
				for (int elem = 0; elem < tamanho; elem++) {
					soma += matA[i][elem] * matB[elem][j];
				}
				matC[i][j] = soma;
			}
		}
		
		System.out.println(System.currentTimeMillis() - inicio);
		if(tamanho <= 10) {
			display(matA);
			display(matB);
			display(matC);
		}
		
		return matC;
	}
	
	public int[][] ByThreads(int[][] matA, int[][]matB, int tamanho) throws RemoteException {
		int[][] matC = new int[tamanho][tamanho];
		
		long inicio = System.currentTimeMillis();
		
		for(int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				Thread T = new ThreadSimples(matA, matB, matC, i, j);
				T.setPriority(7);
				T.start();
			}
		}
		
		System.out.println(System.currentTimeMillis() - inicio);
		if(tamanho <= 10) {
			display(matA);
			display(matB);
			display(matC);
		}
		
		return matC;
	}
	
	public int[][] ByThreadsDivididos(int[][] matA, int[][]matB, int tamanho) throws RemoteException {
		int[][] matC = new int[tamanho][tamanho];
		
		long inicio = System.currentTimeMillis();
		
		Thread T1 = new ThreadDividido(matA,matB,matC, 0        , tamanho/2 ,0         ,tamanho/2);
		Thread T2 = new ThreadDividido(matA,matB,matC, tamanho/2, tamanho   , 0        ,tamanho/2);
		Thread T3 = new ThreadDividido(matA,matB,matC, 0	    , tamanho/2 ,tamanho/2 ,tamanho);
		Thread T4 = new ThreadDividido(matA,matB,matC, tamanho/2, tamanho   ,tamanho/2 ,tamanho);
		
		T1.start();
		T2.start();
		T3.start();
		T4.start();
		
		try {
			T1.join();
			T2.join();
			T3.join();
			T4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(System.currentTimeMillis() - inicio);
		if(tamanho <= 10) {
			display(matA);
			display(matB);
			display(matC);
		}
		return matC;
	}
	
	public int[][] ByPooldeThreds(int[][] matA, int[][]matB, int tamanho) throws RemoteException {
		int[][] matC = new int[tamanho][tamanho];
		
		ExecutorService e = Executors.newCachedThreadPool();
		
		long inicio = System.currentTimeMillis();
		
		for(int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				Runnable R = new RunnablePool(matA, matB, matC, i, j);
				e.execute(R);
				
			}
		}
		
		try {
			e.shutdown();
			while(!e.awaitTermination(500, TimeUnit.MILLISECONDS)) {}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println(System.currentTimeMillis() - inicio);
		if(tamanho <= 10) {
			display(matA);
			display(matB);
			display(matC);
		}	
		
		return matC;
	}
	
	public int[][] gerarMatriz(int lin, int col) throws RemoteException {
		Random ran = new Random();
		int[][] mat = new int[lin][col];
		
		for (int i = 0; i < lin; i++) {
			for (int j = 0; j < col; j++) {
				mat[i][j] = ran.nextInt(10);
			}
		}
		
		return mat;
	}

	public void display(int[][] mat) {
		String s;
	
		for(int i = 0; i < mat.length; i++) {
			s = "";
			for (int j = 0; j < mat[0].length; j++) {
				s += mat[i][j] + " ";
			}
			System.out.println(s);
		}
	}
	
	public static void main() {
		try {
			MatrizMultiplication_Imp_Server multiplicador = new MatrizMultiplication_Imp_Server();
			
			Context nameContext = new InitialContext();
			nameContext.rebind("rmi:multiplicador", multiplicador);

			System.out.println("Pronto para requisições.");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}
	
}
