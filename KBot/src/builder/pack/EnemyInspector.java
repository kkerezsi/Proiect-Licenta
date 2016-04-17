package builder.pack;

import bwapi.Game;
import bwapi.Player;

public class EnemyInspector {
	private static EnemyInspector _instance;
	
	private EnemyInspector(){
	}
	
	public static EnemyInspector getInstance(){
		if(_instance == null){
			_instance = new EnemyInspector();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
		
	}
}
