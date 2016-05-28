package unit.pack;

import base.BaseClass;
import bwapi.Game;
import bwapi.Player;

public class ScoutCoordinator extends BaseClass {
	private static ScoutCoordinator _instance;
	
	private ScoutCoordinator(){
	}
	
	public static ScoutCoordinator getInstance(){
		if(_instance == null){
			_instance = new ScoutCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(){
	}
}
