package attack.pack;

import base.BaseClass;
import bwapi.Game;
import bwapi.Player;

public class DefenseCoordinator extends BaseClass{
	private static DefenseCoordinator _instance;
	
	private DefenseCoordinator(){
	}
	
	public static DefenseCoordinator getInstance(){
		if(_instance == null){
			_instance = new DefenseCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(){
		
	}
}
