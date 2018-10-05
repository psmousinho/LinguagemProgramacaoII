package ExclusaoMutua;

public class Lock_Peterson implements Lock {
	volatile private boolean wantCS[] = {false,false};
	volatile private int turn = 0;
	
	@Override
	public void requestCS(int id) {
		int otherId = 1 - id;
		wantCS[id] = true; //quer entrar
		turn = otherId; //da a vez para o outro
		while (wantCS[otherId] && turn == otherId); //espera o outro terminar se ele quiser entrar
	}

	@Override
	public void releaseCS(int id) {
		wantCS[id] = false;
	}

}
