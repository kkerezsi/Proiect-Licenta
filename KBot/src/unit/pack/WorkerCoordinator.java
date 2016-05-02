package unit.pack;

import java.util.ArrayList;
import java.util.List;

import base.BaseClass;
import bwapi.*;
import constants.pack.Requirements;
import listUtils.pack.ListUtils;

public class WorkerCoordinator extends BaseClass {
	private static WorkerCoordinator _instance = null;
	private ArrayList<Integer> _currentNrOfMinerWorkers;
	private ArrayList<Integer> _currentNrOfGasWorkers;
	
	private WorkerCoordinator(){
		_currentNrOfMinerWorkers = new ArrayList<>();
		_currentNrOfMinerWorkers.add(0);

		_currentNrOfGasWorkers = new ArrayList<>();
		_currentNrOfGasWorkers.add(0);
	}
	
	public static WorkerCoordinator getInstance(){
		if(_instance == null){
			_instance = new WorkerCoordinator();
		}
		
		return _instance;
	}
	
	public boolean areMinerWorkersRequired(int commandCenterIndex){
		return _currentNrOfMinerWorkers.get(commandCenterIndex) <  Requirements.MAX_NR_MINING_WORKERS_ON_BASE;
	}
	
	public boolean areGasWorkersRequired(int commandCenterIndex){
		return _currentNrOfGasWorkers.get(commandCenterIndex) <  Requirements.MAX_NR_GAS_WORKERS_ON_BASE;
	}
	
	public void runWorkers(){
		//filter all worker units
		List<Unit> commandCenters = ListUtils.getMyCommandCenters();

		//iterate through my units
		if(commandCenters.size() > 0){
			for (int i = 0; i < commandCenters.size(); i++) {
				Unit closestMineral = ListUtils.getClosestUnit(_game.getMinerals(), commandCenters.get(i),
						UnitType.Resource_Mineral_Field);
				Unit closestGasExtractor = ListUtils.getClosestUnit(_game.getGeysers(), commandCenters.get(i),
						UnitType.Resource_Vespene_Geyser);

				TilePosition tile = commandCenters.get(i).getTilePosition();
				List<Unit> workers = ListUtils.getNearestUnitsTo(tile,UnitType.Terran_SCV, 30.0);

				for (Unit worker : workers) {
					if(worker.isIdle()){
						if(this.areMinerWorkersRequired(i)){
							sendToGatherMinerals(worker, closestMineral,i);
						}else if(this.areGasWorkersRequired(i)){
							sendToGatherGas(worker, closestGasExtractor, i);
						}
					}
				}
			}
		}
    }
	
	private void sendToGatherMinerals(Unit worker, Unit closestMineral, int commandCenterIndex){
        //if a mineral patch was found, send the drone to gather it
        if (closestMineral != null) {
        	worker.gather(closestMineral, false);
        	_currentNrOfMinerWorkers.set(commandCenterIndex, _currentNrOfMinerWorkers.get(commandCenterIndex) + 1);
        }
	}
	
	private void sendToGatherGas(Unit worker, Unit closestGasExtractor, int commandCenterIndex){
    	//if a gas patch was found, send the drone to gather it
        if (closestGasExtractor != null) {
        	worker.gather(closestGasExtractor, false);
        	_currentNrOfGasWorkers.set(commandCenterIndex, _currentNrOfGasWorkers.get(commandCenterIndex) + 1);
        }
	}

	public boolean areScoutsRequired() {
		return false;
	}
}
