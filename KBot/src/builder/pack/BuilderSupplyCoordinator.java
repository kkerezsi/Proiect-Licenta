package builder.pack;

import java.util.ArrayList;
import java.util.List;

import base.BaseClass;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;
import listUtils.pack.ListUtils;
import unit.pack.WorkerCoordinator;

public class BuilderSupplyCoordinator extends BaseClass {
	private static BuilderSupplyCoordinator _instance;

	private BuilderSupplyCoordinator(){
	}
	
	public static BuilderSupplyCoordinator getInstance(){
		if(_instance == null){
			_instance = new BuilderSupplyCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(){
		List<Unit> supplyBuilders = ListUtils.getMyCommandCenters();
		
		for (int i = 0; i < supplyBuilders.size(); i++) {
			if(WorkerCoordinator.getInstance().areMinerWorkersRequired(i)
					|| WorkerCoordinator.getInstance().areGasWorkersRequired(i)
					|| WorkerCoordinator.getInstance().areScoutsRequired()){
				supplyBuilders.get(i).train(UnitType.Terran_SCV);
			}
		}
	}
	
	public int getTotalSupply(){
		return _self.supplyTotal(Race.Terran);
	}
	
	public int getSupplyUsed(){
		return _self.supplyUsed(Race.Terran);
	}
	
	public int getUsableSupply(){
		return _self.supplyTotal(Race.Terran) - _self.supplyUsed(Race.Terran);
	}
}
