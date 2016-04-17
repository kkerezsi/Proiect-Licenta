package attack.pack;

import bwapi.Game;
import bwapi.Player;

public class DefenseCoordinator {
	private static DefenseCoordinator _instance;
	
	private DefenseCoordinator(){
	}
	
	public static DefenseCoordinator getInstance(){
		if(_instance == null){
			_instance = new DefenseCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
		
	}
}
