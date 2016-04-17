package attack.pack;

import bwapi.Game;
import bwapi.Player;

public class AttackStrategyCoordinator {
	private static AttackStrategyCoordinator _instance;
	
	private AttackStrategyCoordinator(){
	}
	
	public static AttackStrategyCoordinator getInstance(){
		if(_instance == null){
			_instance = new AttackStrategyCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
		
	}
}
