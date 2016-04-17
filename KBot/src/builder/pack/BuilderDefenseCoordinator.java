package builder.pack;

import bwapi.Game;
import bwapi.Player;

public class BuilderDefenseCoordinator {
	private static BuilderDefenseCoordinator _instance;
	
	private BuilderDefenseCoordinator(){
	}
	
	public static BuilderDefenseCoordinator getInstance(){
		if(_instance == null){
			_instance = new BuilderDefenseCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
		
	}
}
