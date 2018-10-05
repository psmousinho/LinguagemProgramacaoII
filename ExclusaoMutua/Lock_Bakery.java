package ExclusaoMutua;

public class Lock_Bakery implements Lock {

	volatile private int N;
	volatile private boolean[] choosing;
	volatile private int[] number;
	
	public Lock_Bakery(int nProc) {
		N = nProc;
		choosing = new boolean[N];
		number = new int[N];
		
		for(int i = 0; i < N; i++) {
			choosing[i] = false;
			number[i] = 0;
		}
	}
	
	@Override
	public void requestCS(int id) {
		choosing[id] = true;
		for(int i = 0; i < N; i++) {
			if(number[i] > number[id])
				number[id] = number[i];
		}
		number[id]++;
		choosing[id] = false;
		
		for(int i = 0; i < N; i++) {
			while(choosing[i]);
			while( (number[i] != 0) &&  //i tem senha e
					( (number[i] < number[id]) || //a senha i é menor que a senha id ou
					   ( (number[i] == number[id]) && (i < id) ) ) ); //a senha i igual a senha id e i e menor que id 
		}
	}

	@Override
	public void releaseCS(int id) {
		number[id] = 0;
	}

}
