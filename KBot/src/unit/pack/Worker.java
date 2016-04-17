package unit.pack;

import java.util.List;

import base.BaseClass;
import bwapi.*;
import constants.pack.Requirements;
import listUtils.pack.ListUtils;

public class Worker extends BaseClass {
	private static Worker _instance = null;
	private int _currentNrOfMinerWorkers;
	private int _currentNrOfGasWorkers;
	
	private Worker(){
		_currentNrOfMinerWorkers = 0;
		_currentNrOfGasWorkers = 0;
	}
	
	public static Worker getInstance(){
		if(_instance == null){
			_instance = new Worker();
		}
		
		return _instance;
	}
	
	public boolean areMinerWorkersRequired(){
		return _currentNrOfMinerWorkers <  Requirements.MAX_NR_MINING_WORKERS_ON_BASE;
	}
	
	public boolean areGasWorkersRequired(){
		return _currentNrOfGasWorkers <  Requirements.MAX_NR_GAS_WORKERS_ON_BASE;
	}
	
	public void runWorkers(){
		//filter all worker units
		List<Unit> workerUnits = ListUtils.getAllIdleUnitsByType(UnitType.Terran_SCV, _self);

		//iterate through my units
        for (Unit worker : workerUnits) {
			if(worker.isIdle()){
		        if(this.areMinerWorkersRequired()){
		        	sendToGatherMinerals(worker);
		        }else if(this.areGasWorkersRequired()){
		        	sendToGatherGas(worker);
		        }
			}
        }
    }
	
	private void sendToGatherMinerals(Unit worker){
        Unit closestMineral = null;
        
        closestMineral = ListUtils.getClosestUnit(_game.getMinerals(), worker, UnitType.Resource_Mineral_Field);

        //if a mineral patch was found, send the drone to gather it
        if (closestMineral != null) {
        	worker.gather(closestMineral, false);
        	_currentNrOfMinerWorkers++;
        }
	}
	
	private void sendToGatherGas(Unit worker){
        Unit closestGasExtractor = ListUtils.getClosestUnit(_game.getGeysers(), worker, UnitType.Resource_Vespene_Geyser);
    	
    	//if a gas patch was found, send the drone to gather it
        if (closestGasExtractor != null) {
        	worker.gather(closestGasExtractor, false);
        	_currentNrOfGasWorkers++;
        }
	}
}
