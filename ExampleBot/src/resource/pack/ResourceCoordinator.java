package resource.pack;

import bwapi.Game;
import bwapi.Player;

public class ResourceCoordinator {
	private static ResourceCoordinator _instance;
	
	private ResourceCoordinator(){
	}
	
	public static ResourceCoordinator getInstance(){
		if(_instance == null){
			_instance = new ResourceCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
		
	}
}
