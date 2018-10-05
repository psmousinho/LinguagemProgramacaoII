package ExclusaoMutua;

public class Lock_Dekker implements Lock {
	volatile private boolean wantCS[] = {false,false};
	volatile private int turn = 0;
	
	@Override
	public void requestCS(int id) {
		int otherId = 1 - id;
		wantCS[id] = true; //quer entrar na CS
		while(wantCS[otherId]) { //Se o outro processor quer entrar na CS
			if(turn == otherId) { //se for a vez do outro entrar
				wantCS[id] = false; //desista de entrar
				while(turn == otherId); //espere o outro terminar
				wantCS[id] = true; //volte a querer entrar
			}
		}
	}

	@Override
	public void releaseCS(int id) {
		turn = 1 - id;
		wantCS[id] = false;
	}

}
