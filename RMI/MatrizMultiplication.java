package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatrizMultiplication extends Remote{
	
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
	
	public int[][] BySequencial(int[][] matA, int[][]matB, int tamanho) throws RemoteException;
	
	public int[][] ByThreads(int[][] matA, int[][]matB, int tamanho) throws RemoteException;
	
	public int[][] ByThreadsDivididos(int[][] matA, int[][]matB, int tamanho) throws RemoteException;
	
	public int[][] ByPoolThreads(int[][] matA, int[][]matB, int tamanho) throws RemoteException;
	
	public int[][] gerarMatriz(int lin, int col) throws RemoteException;
	
	public void display(int[][] mat) throws RemoteException;
}
