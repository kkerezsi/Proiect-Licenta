package unit.pack;

import java.util.List;

import Constants.Requirements;
import ListUtils.ListUtils;
import base.BaseClass;
import bwapi.*;

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
	
	public void runWorkers(Game game,Player self){
		//filter all worker units
		List<Unit> workerUnits = ListUtils.getAllIdleUnitsByType(UnitType.Terran_SCV, self);
		_currentNrOfMinerWorkers  = ListUtils.getAllUnitsByActionAndType(UnitType.Terran_SCV, "gatherMinerals",self).size();
		_currentNrOfGasWorkers = ListUtils.getAllUnitsByActionAndType(UnitType.Terran_SCV, "gatherGas",self).size();

		//iterate through my units
        for (Unit worker : workerUnits) {
        	//if there's enough minerals, train an SCV
        	game.drawTextMap(worker.getPosition().getX(), worker.getPosition().getY(), worker.getTilePosition().toString());

            //if it's a drone and it's idle, send it to the closest mineral patch
			if(worker.isIdle()){
		
		        //find the closest mineral
		        if(this.areMinerWorkersRequired()){
			        Unit closestMineral = null;
			        for (Unit neutralUnit : _game.neutral().getUnits()) {
			            if (neutralUnit.getType().isMineralField()) {
			                if (closestMineral == null || worker.getDistance(neutralUnit) < worker.getDistance(closestMineral)) {
			                    closestMineral = neutralUnit;
			                }
			            }
			        }
			
			        //if a mineral patch was found, send the drone to gather it
			        if (closestMineral != null) {
			        	worker.gather(closestMineral, false);
			        }
		        }else if(this.areGasWorkersRequired()){
			        List<Unit> rafineryList =  ListUtils.getAllUnitsByType(UnitType.Terran_Refinery, self.getUnits());
			        
			        Unit closestGasExtractor = rafineryList.size() > 0 ? rafineryList.get(0) : null;
		        	
		        	//if a gas patch was found, send the drone to gather it
			        if (closestGasExtractor != null) {
			        	worker.gather(closestGasExtractor, false);
			        }
		        }
			}
        }
    }
}
