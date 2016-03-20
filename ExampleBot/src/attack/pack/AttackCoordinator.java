package attack.pack;

import bwapi.Game;
import bwapi.Player;

public class AttackCoordinator {
	private static AttackCoordinator _instance;
	
	private AttackCoordinator(){
	}
	
	public static AttackCoordinator getInstance(){
		if(_instance == null){
			_instance = new AttackCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
	
	}
}
