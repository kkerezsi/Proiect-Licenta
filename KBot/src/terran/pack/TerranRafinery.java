package terran.pack;

import java.util.List;

import base.BaseClass;
import builder.pack.BuildOrder;
import builder.pack.BuilderSupplyCoordinator;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Requirements;
import contracts.pack.IBuilding;
import listUtils.pack.ListUtils;
import resource.pack.CompleteResourceModel;
import resource.pack.ResourceCoordinator;

public class TerranRafinery extends BaseClass implements IBuilding {
	private UnitType _buildingType = UnitType.Terran_Refinery;
	
	private static TerranRafinery _instance;
	
	public static TerranRafinery getInstance(){
		if(_instance == null){
			_instance = new TerranRafinery();
		}
		
		return _instance;
	}
	
	private TerranRafinery(){
	}
	
	@Override
	public boolean shouldBuild() {
		List<Unit> refineries = ListUtils.getAllUnitsByType(_buildingType, _self);
		int battleUnits = ListUtils.getNumberOfBattleUnitsCompleted();
		
		if (refineries != null && refineries.size() == 0) {
			boolean isEnoughInfantry = battleUnits != 0 && (battleUnits >= Requirements.MINIMUM_MARINES);			
			boolean isAnotherBaseAndFreeMinerals = TerranCommandCenter.getInstance().getNumberOfThisType() > 1;
			
			return isEnoughInfantry || isAnotherBaseAndFreeMinerals;
		}
		
		return false;
	}

	@Override
	public boolean canBuild() {
		ResourceCoordinator reCoord = ResourceCoordinator.getInstance();
		
		CompleteResourceModel mineralsAndGasRequired = reCoord.getRequirementsForType(_buildingType);
		CompleteResourceModel myResources = reCoord.getMyResources();
		
		if(mineralsAndGasRequired != null 
				&& mineralsAndGasRequired.getMinerals() < myResources.getMinerals()
				&& mineralsAndGasRequired.getGas() < myResources.getGas()){
			
			BuildOrder.getInstance().cacheBuild(_buildingType);
			return true;
		}
		
		return false;
	}

	@Override
	public void forceBuild() {
		BuildOrder.getInstance().cacheBuild(_buildingType);
	}

	@Override
	public int getNumberOfThisType() {
		List<Unit> myBuildings = ListUtils.getAllIdleUnitsByType(_buildingType, _self.getUnits());
		
		if(myBuildings != null)
			return myBuildings.size();
		
		return 0;
	}

	@Override
	public List<Unit> getBuildingsOfThisType() {
		return ListUtils.getAllIdleUnitsByType(_buildingType, _self.getUnits());
	}
}
