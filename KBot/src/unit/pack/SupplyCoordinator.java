package unit.pack;

import java.util.ArrayList;
import java.util.List;

import base.BaseClass;
import bwapi.Position;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Requirements;
import listUtils.pack.ListUtils;
import resource.pack.ResourceCoordinator;

public class SupplyCoordinator extends BaseClass {
	private static SupplyCoordinator _instance;

	private SupplyCoordinator(){
	}
	
	public static SupplyCoordinator getInstance(){
		if(_instance == null){
			_instance = new SupplyCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(){
		List<Unit> supplyBuilders = ListUtils.getMyCommandCenters();

		WorkerCoordinator wCoordInstance = WorkerCoordinator.getInstance();
		ResourceCoordinator resCInstance = ResourceCoordinator.getInstance();
		for (int i = 0; i < supplyBuilders.size(); i++) {
			if(wCoordInstance.areMinerWorkersRequired(i)
			|| wCoordInstance.areGasWorkersRequired(i)
			|| wCoordInstance.areBuildersRequired(i)){

				if(resCInstance.getMyResources().getMinerals() > (100* (wCoordInstance.getBuilders(i).size() + 1))
						&& wCoordInstance.getBuilders(i).size() < Requirements.MAX_NR_BASE_BUILDERS) {
					populateBuilders(supplyBuilders,i);
				}

				if(getVirtualSupplyUsed() < getTotalSupply() - Requirements.ALERT_SUPPLY_ZONE
						&& !supplyBuilders.get(i).isTraining()) {
					supplyBuilders.get(i).train(UnitType.Terran_SCV);
				}
			}
		}
	}
	
	public int getTotalSupply(){
		return _self.supplyTotal(Race.Terran) / 2;
	}
	
	public int getSupplyUsed(){
		return _self.supplyUsed(Race.Terran) / 2;
	}
	
	public int getUsableSupply(){
		return (_self.supplyTotal(Race.Terran) - _self.supplyUsed(Race.Terran)) / 2;
	}

	public int getVirtualSupplyUsed(){
		return ListUtils.getQueuedUnits(UnitType.Terran_SCV).size() + getSupplyUsed();
	}

	public void populateBuilders(List<Unit> supplyBuilders, int baseIndex) {
		WorkerCoordinator workerCInstance =  WorkerCoordinator.getInstance();
		List<Unit> builderList = ListUtils.getNearestUnitsTo(supplyBuilders.get(baseIndex).getTilePosition(),UnitType.Terran_SCV,Requirements.SEARCH_RANGE_WORKERS);
		Unit builder = builderList.size() > 0 ? builderList.get(0) : null;

		if( builder != null ) {
			workerCInstance.removeFromMinersOrExtractors(builder, baseIndex);
			if(builder.isIdle())
				builder.move(new Position(supplyBuilders.get(baseIndex).getPosition().getX() + 150 , supplyBuilders.get(baseIndex).getPosition().getY() + 150));
			workerCInstance.getBuilders(baseIndex).add(builder);
		}
	}
}
