package ExclusaoMutua;

public class Lock_Dijkstra implements Lock {
	
	volatile private int nProc, turn;
	volatile int[] status;
	
	public Lock_Dijkstra(int N) {
		nProc = N;
		turn = 0;
		status = new int[N];
		
		for(int i = 0; i < N; i++) {
			status[i] = 0;
		}
	}
	
	@Override
	public void requestCS(int id) {
		int j;
		
		do {
			status[id] = 1; //afirma que quer entrar
			while(turn != id) { //enquanto não for minha vez
				if(status[turn] == 0) //se o processo atual esta ocioso
					turn = id; //tenta pegar a vez
			}
			status[id] = 2; //afirma que esta dentro
			j = 0; 
			while( (j < nProc) && ((j == id) || (status[j] != 2))) //se todos os outros processos nao estão dentro
				j++;
		} while(j != nProc);
	}

	@Override
	public void releaseCS(int id) {
		status[id] = 0;
	}

}
