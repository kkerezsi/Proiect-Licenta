package builder.pack;

import bwapi.Game;
import bwapi.Player;

public class ScoutCoordinator {
	private static ScoutCoordinator _instance;
	
	private ScoutCoordinator(){
	}
	
	public static ScoutCoordinator getInstance(){
		if(_instance == null){
			_instance = new ScoutCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
		
	}
}
