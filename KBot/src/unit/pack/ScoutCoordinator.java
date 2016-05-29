package unit.pack;

import action.pack.ActionQueue;
import action.pack.ScoutingAction;
import base.BaseClass;
import base.Tuple;
import bwapi.Game;
import bwapi.Player;
import bwapi.Unit;
import bwta.BWTA;
import bwta.BaseLocation;
import constants.pack.Requirements;
import constants.pack.Signatures;

import java.util.ArrayList;

public class ScoutCoordinator extends BaseClass {
	private static ScoutCoordinator _instance;

	private ArrayList<Tuple<BaseLocation,Boolean>> availableLocations;
	private Unit scout;
	private WorkerCoordinator worckerCoordI;

	private ScoutCoordinator(){
		availableLocations = new ArrayList<>();
		worckerCoordI = WorkerCoordinator.getInstance();

		for (BaseLocation base : BWTA.getBaseLocations()) {
			availableLocations.add(new Tuple<>(base, false));
		}
	}
	
	public static ScoutCoordinator getInstance(){
		if(_instance == null){
			_instance = new ScoutCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(){
		ActionQueue queue = ActionQueue.getInstance();
		scout = worckerCoordI.getScout();

		if(availableLocations.size() > 0 && availableLocations.get(0).y == true)
			availableLocations.remove(0);
		else if(scout != null && scout.exists() && !queue.isActionQueued(Signatures.SCOUTING_SIGNATURE)){
			if(availableLocations.size() > 0 ){
				Tuple<BaseLocation,Boolean> first = availableLocations.get(0);
				queue.enqueueAction(new ScoutingAction(scout, first));
			}
		}
	}
}
