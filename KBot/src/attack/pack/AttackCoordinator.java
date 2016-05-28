package attack.pack;

import base.BaseClass;
import bwapi.Game;
import bwapi.Player;

public class AttackCoordinator extends BaseClass{
	private static AttackCoordinator _instance;
	
	private AttackCoordinator(){
	}
	
	public static AttackCoordinator getInstance(){
		if(_instance == null){
			_instance = new AttackCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(){
	}
}
