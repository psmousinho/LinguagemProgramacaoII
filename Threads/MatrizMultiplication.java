package Threads;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;

public class MatrizMultiplication extends Thread {
	
	static class ThreadSimples extends Thread {
		private int[][] matA;
		private int[][] matB;
		private int[][] matC;
		private int lin,col;
		
		public ThreadSimples (int[][] matA, int[][] matB, int[][] matC, int lin, int col) {
			this.matA = matA;
			this.matB = matB;
			this.matC = matC;
			this.lin = lin;
			this.col = col;
		}
		
		public void run() {
			int soma = 0;
			for (int elem = 0; elem < matA.length; elem++) {
				soma += matA[lin][elem] * matB[elem][col];
			}
			matC[lin][col] = soma;
		}
	}
	
	static class ThreadDividido extends Thread {
		private int[][] matA;
		private int[][] matB;
		private int[][] matC;
		private int linI, linF, colI, colF;
		
		public ThreadDividido (int[][] matA, int[][] matB, int[][] matC, int linI, int linF, int colI, int colF) {
			this.matA = matA;
			this.matB = matB;
			this.matC = matC;
			this.linI = linI;
			this.colI = colI;
			this.linF = linF;
			this.colF = colF;
		}
		
		public void run() {
			int soma;
			for(int i = linI; i < linF; i++) {
				for(int j = colI; j < colF; j++) {
					soma = 0;
					for(int elem = 0; elem < matA.length; elem++) {
						soma += matA[i][elem] * matB[elem][j];
					}
					matC[i][j] = soma;
				}
			}
		}
		
	}

	static class RunnablePool implements Runnable {
		private int[][] matA;
		private int[][] matB;
		private int[][] matC;
		private int lin,col;
		
		public RunnablePool(int[][] matA, int[][] matB, int[][] matC, int lin, int col) {
			this.matA = matA;
			this.matB = matB;
			this.matC = matC;
			this.lin = lin;
			this.col = col;
		}
		
		public void run() {
			int soma = 0;
			for (int elem = 0; elem < matA.length; elem++) {
				soma += matA[lin][elem] * matB[elem][col];
			}
			matC[lin][col] = soma;
		}
	}
	
	public static void main(String[] args) {
		int tamanho = 4;
		int[][] matA = gerarMatriz(tamanho,tamanho);
		int[][] matB = gerarMatriz(tamanho,tamanho);
		
		System.out.println("Sequencial :");
		BySequencial(matA, matB, tamanho);
		System.out.println("Por Threads :");
		ByThreads(matA, matB, tamanho);
		System.out.println("Por Threads divididos :");
		ByThreadsDivididos(matA, matB, tamanho);
		System.out.println("por Pool de Threads :");
		ByPoolThreads(matA, matB, tamanho);
		
	}

	public static int[][] BySequencial(int[][] matA, int[][]matB, int tamanho) {
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
	
	public static int[][] ByThreads(int[][] matA, int[][]matB, int tamanho) {
		int[][] matC = new int[tamanho][tamanho];
		
		long inicio = System.currentTimeMillis();
		
		ArrayList<Thread> threads = new ArrayList<>();
		
		for(int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				Thread T = new ThreadSimples(matA, matB, matC, i, j);
				threads.add(T);
				T.start();
			}
		}
		
		try {
			for (int i = 0 ; i < threads.size(); i++) {
				threads.get(i).join();
			}
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

	public static int[][] ByThreadsDivididos(int[][] matA, int[][]matB, int tamanho) {
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
	
	public static int[][] ByPoolThreads(int[][] matA, int[][]matB, int tamanho) {
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
	
	public static int[][] gerarMatriz(int lin, int col) {
		Random ran = new Random();
		int[][] mat = new int[lin][col];
		
		for (int i = 0; i < lin; i++) {
			for (int j = 0; j < col; j++) {
				mat[i][j] = ran.nextInt(10);
			}
		}
		
		return mat;
	}
	
	public static void display(int[][] mat) {
		String s;
	
		for(int i = 0; i < mat.length; i++) {
			s = "";
			for (int j = 0; j < mat[0].length; j++) {
				s += mat[i][j] + " ";
			}
			System.out.println(s);
		}
	}
}