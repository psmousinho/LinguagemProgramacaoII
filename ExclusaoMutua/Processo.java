package ExclusaoMutua;

public class Processo extends Thread {
	private Lock lock;
	private int id;
	
	public Processo(Lock lock, int id) {
		this.lock = lock;
		this.id = id;
	}
	
	public void run() {
		while(true) {
			lock.requestCS(id);
			System.out.println("Processo " + id + " esta na seção critica.");
			lock.releaseCS(id);
		}
	}
	
	public static void main(String[] args) {
		//ByDekker();
		//ByPeterson();
		//ByDijkstra(10);
		ByBakery(10);
	}
	public static void ByDekker() {
		Lock lock = new Lock_Dekker();
		Processo A = new Processo(lock, 0);
		Processo B = new Processo(lock, 1);
		A.start();
		B.start();
	}
	
	public static void ByPeterson() {
		Lock lock = new Lock_Peterson();
		Processo A = new Processo(lock, 0);
		Processo B = new Processo(lock, 1);
		A.start();
		B.start();
	}
	
	public static void ByDijkstra(int nProc) {
		Lock lock = new Lock_Dijkstra(nProc);
		for(int i = 0; i < nProc; i++) {
			(new Processo(lock,i)).start();
		}
	}
	
	public static void ByBakery(int nProc) {
		Lock lock = new Lock_Dijkstra(nProc);
		for(int i = 0; i < nProc; i++) {
			(new Processo(lock,i)).start();
		}
	}
	
}
