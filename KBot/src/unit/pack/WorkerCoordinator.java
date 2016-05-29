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

	private int currentSize;
	private Unit scout = null;

	private WorkerCoordinator(){
		minerWorkers.put(0, new ArrayList<Unit>());
		gasWorkers.put(0, new ArrayList<Unit>());
		builderWorkers.put(0, new ArrayList<Unit>());

		currentSize = 0;
	}
	
	public static WorkerCoordinator getInstance(){
		if(_instance == null){
			_instance = new WorkerCoordinator();
		}
		
		return _instance;
	}
	
	public boolean areMinerWorkersRequired(int commandCenterIndex){
        int mineralWorkersCount = minerWorkers.get(commandCenterIndex).size();

        return (mineralWorkersCount <  Requirements.MAX_NR_MINING_WORKERS_ON_BASE);
	}
	
	public boolean areGasWorkersRequired(int commandCenterIndex){
		return gasWorkers.get(commandCenterIndex).size() <  Requirements.MAX_NR_GAS_WORKERS_ON_BASE;
	}

	public boolean areBuildersRequired(int commandCenterIndex) {
        int mineralWorkersCount = minerWorkers.get(commandCenterIndex).size();
        int builderWorkersCount = builderWorkers.get(commandCenterIndex).size();

		return (builderWorkersCount < Requirements.MAX_NR_BASE_BUILDERS)
			|| (mineralWorkersCount % Requirements.MAX_NR_MINING_WORKERS_WITHOUT_BUILDER == 0);
	}

	public void runWorkers(){
		//filter all worker units
		int stateUnitsCount = _self.getUnits().size();

		if(stateUnitsCount != currentSize) {
			currentSize = stateUnitsCount;
			ArrayList<Unit> commandCenters = ListUtils.getMyCommandCenters();

			//handle idle units
			if(commandCenters.size() > 0) {
				for (int i = 0; i < commandCenters.size(); i++) {
					updateWorkers(i, commandCenters);

					Unit closestMineral = null;
					Unit closestGasExtractor = null;

					TilePosition tile = commandCenters.get(i).getTilePosition();
					List<Unit> workers = ListUtils.getNearestUnitsTo(tile, UnitType.Terran_SCV, Requirements.SEARCH_RANGE_WORKERS);

					for (Unit worker : workers) {
						if (!isWorkerInAnyCategory(i,worker)) {
							if (this.areMinerWorkersRequired(i)) {
								if (closestMineral == null) {
									closestMineral = ListUtils.getClosestUnit(_game.getMinerals(), commandCenters.get(i),
											UnitType.Resource_Mineral_Field);
								}

								sendToGatherMinerals(worker, closestMineral, i);
							} else if (this.areGasWorkersRequired(i)) {
								if (closestGasExtractor == null) {
									closestGasExtractor = ListUtils.getClosestUnit(_self.getUnits(), commandCenters.get(i), UnitType.Terran_Refinery);
								}

								sendToGatherGas(worker, closestGasExtractor, i);
							} else if (this.areScoutsRequired()) {
								initiateScout(worker, commandCenters.get(i));
							}
						}
					}
				}
			}
		}
    }

	private void initiateScout(Unit worker, Unit commandCenter) {
		this.scout = worker;

        if(!worker.isMoving() && !worker.isConstructing())
		    worker.move(new Position(commandCenter.getPosition().getX() + Requirements.RELATIVE_BASE_DISTANCE ,
			    	                 commandCenter.getPosition().getY() + Requirements.RELATIVE_BASE_DISTANCE));

	}

	private void sendToGatherMinerals(Unit worker, Unit closestMineral, int commandCenterIndex){
        //if a mineral patch was found, send the drone to gather it
        if (closestMineral != null) {
        	worker.gather(closestMineral, false);
			minerWorkers.get(commandCenterIndex).add(worker);
        }else {
            minerWorkers.get(commandCenterIndex).add(worker);
            System.out.println("Can't do anything. I am waiting for gas minerals");
        }
	}
	
	private void sendToGatherGas(Unit worker, Unit closestGasExtractor, int commandCenterIndex){
    	//if a gas patch was found, send the drone to gather it
        if (closestGasExtractor != null) {
        	worker.gather(closestGasExtractor, false);
        	gasWorkers.get(commandCenterIndex).add(worker);
        }
		else {
			worker.stop();
            gasWorkers.get(commandCenterIndex).add(worker);
			System.out.println("Can't do anything. I am waiting for gas extractor");
		}
	}

	public ArrayList<Unit> getBuilders(int commandCenterIndex){
		return this.builderWorkers.get(commandCenterIndex);
	}

	public boolean areScoutsRequired() {
		if(minerWorkers.size() <= 0)
			return false;

		if(minerWorkers.get(0).size() < Requirements.MIN_MINERS_FOR_SCOUT_SELECTION){
			return false;
		}

		if(scout != null && !scout.exists()) {
			return false;
		}

		return  true;
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

        for (Unit w :
                workers) {
            w.move(new Position(commandCenters.get(commandCenterIndex).getPosition().getX() + Requirements.RELATIVE_BASE_DISTANCE ,
                                commandCenters.get(commandCenterIndex).getPosition().getY() + Requirements.RELATIVE_BASE_DISTANCE));
        }

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

    private boolean isWorkerInAnyCategory(int baseIndex, Unit worker){
        return (worker.isIdle()
                && gasWorkers.get(baseIndex).contains(worker)
                || minerWorkers.get(baseIndex).contains(worker)
                || builderWorkers.get(baseIndex).contains(worker));
    }

	public Unit getScout() {
		return scout;
	}

	public void setScout(Unit scout) {
		this.scout = scout;
	}
}
