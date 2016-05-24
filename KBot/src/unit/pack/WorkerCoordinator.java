package unit.pack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.BaseClass;
import bwapi.*;
import constants.pack.Requirements;
import listUtils.pack.ListUtils;

public class WorkerCoordinator extends BaseClass {
	private static WorkerCoordinator _instance = null;
	private Map<Integer, ArrayList<Unit>> minerWorkers = new HashMap<Integer, ArrayList<Unit> >();
	private Map<Integer, ArrayList<Unit>> gasWorkers = new HashMap<Integer, ArrayList<Unit> >();
	private Map<Integer, ArrayList<Unit>> builderWorkers = new HashMap<Integer, ArrayList<Unit> >();
	
	private WorkerCoordinator(){
		minerWorkers.put(0, new ArrayList<Unit>());
		gasWorkers.put(0, new ArrayList<Unit>());
		builderWorkers.put(0, new ArrayList<Unit>());
	}
	
	public static WorkerCoordinator getInstance(){
		if(_instance == null){
			_instance = new WorkerCoordinator();
		}
		
		return _instance;
	}
	
	public boolean areMinerWorkersRequired(int commandCenterIndex){
		return minerWorkers.get(commandCenterIndex).size() <  Requirements.MAX_NR_MINING_WORKERS_ON_BASE;
	}
	
	public boolean areGasWorkersRequired(int commandCenterIndex){
		return gasWorkers.get(commandCenterIndex).size() <  Requirements.MAX_NR_GAS_WORKERS_ON_BASE;
	}

	public boolean areBuildersRequired(int commandCenterIndex) {
		return builderWorkers.get(commandCenterIndex).size() < Requirements.MAX_NR_BASE_BUILDERS;
	}

	public void runWorkers(){
		//filter all worker units
		ArrayList<Unit> commandCenters = ListUtils.getMyCommandCenters();


		//handle idle units
		if(commandCenters.size() > 0){
			for (int i = 0; i < commandCenters.size(); i++) {
				updateWorkers(i, commandCenters);

				Unit closestMineral = null;
				Unit closestGasExtractor = null;

				TilePosition tile = commandCenters.get(i).getTilePosition();
				List<Unit> workers = ListUtils.getNearestUnitsTo(tile,UnitType.Terran_SCV, 450.0);

				for (Unit worker : workers) {
					if(worker.isIdle() && worker.isCompleted()){
						if(this.areMinerWorkersRequired(i)){
							if(closestMineral == null){
								closestMineral = ListUtils.getClosestUnit(_game.getMinerals(), commandCenters.get(i),
										UnitType.Resource_Mineral_Field);
							}

							sendToGatherMinerals(worker, closestMineral,i);
						}else if(this.areGasWorkersRequired(i)){
							if(closestGasExtractor == null){
								closestGasExtractor = ListUtils.getClosestUnit(_self.getUnits(),commandCenters.get(i), UnitType.Terran_Refinery);
							}

							sendToGatherGas(worker, closestGasExtractor, i);
						} else if (this.areScoutsRequired()){
							//send unit to scout
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
			minerWorkers.get(commandCenterIndex).add(worker);
        }
	}
	
	private void sendToGatherGas(Unit worker, Unit closestGasExtractor, int commandCenterIndex){
    	//if a gas patch was found, send the drone to gather it
        if (closestGasExtractor != null) {
        	worker.gather(closestGasExtractor, false);
        	gasWorkers.get(commandCenterIndex).add(worker);
        }
	}

	public ArrayList<Unit> getBuilders(int commandCenterIndex){
		return this.builderWorkers.get(commandCenterIndex);
	}

	public boolean areScoutsRequired() {
		return false;
	}

	public Unit getAvailableBuilder(int commandCenterIndex){
		ArrayList<Unit> builders = builderWorkers.get(commandCenterIndex);

		Unit builder = ListUtils.getFirstIdleUnit(builders);

		return builder;
	}

	public void updateWorkers(int commandCenterIndex, ArrayList<Unit> commandCenters){
		updateMiners(commandCenterIndex,commandCenters);
		updateGasExtractors(commandCenterIndex,commandCenters);
		updateBuilders(commandCenterIndex,commandCenters);
	}

	private void updateBuilders(int commandCenterIndex, ArrayList<Unit> commandCenters){
		ArrayList<Unit> workers = ListUtils.removeDeadUnits(builderWorkers.get(commandCenterIndex));
		builderWorkers.get(commandCenterIndex).clear();
		builderWorkers.get(commandCenterIndex).addAll(workers);
	}

	private void updateMiners(int commandCenterIndex, ArrayList<Unit> commandCenters){
		ArrayList<Unit> workers = ListUtils.removeDeadUnits(minerWorkers.get(commandCenterIndex));
		minerWorkers.get(commandCenterIndex).clear();
		minerWorkers.get(commandCenterIndex).addAll(workers);

		Unit closestMineral = null;
		for (Unit u : workers){
			if(!u.isGatheringMinerals()) {

				if(closestMineral == null)
					closestMineral = ListUtils.getClosestUnit(_game.getMinerals(), commandCenters.get(commandCenterIndex),
							UnitType.Resource_Mineral_Field);

				u.gather(closestMineral);
			}
		}
	}

	private void updateGasExtractors(int commandCenterIndex, ArrayList<Unit> commandCenters){
		ArrayList<Unit> workers = ListUtils.removeDeadUnits(gasWorkers.get(commandCenterIndex));
		gasWorkers.get(commandCenterIndex).clear();
		gasWorkers.get(commandCenterIndex).addAll(workers);

		Unit closestGasExtractor = null;

		for (Unit u : workers){
			if(!u.isGatheringGas()) {

				if( closestGasExtractor == null)
					closestGasExtractor = ListUtils.getClosestUnit(_self.getUnits(),commandCenters.get(commandCenterIndex), UnitType.Terran_Refinery);

				u.gather(closestGasExtractor);
			}
		}
	}

	public void removeFromMinersOrExtractors(Unit builder, int commandCenterIndex) {
		minerWorkers.get(commandCenterIndex).remove(builder);
		gasWorkers.get(commandCenterIndex).remove(builder);
	}
}
