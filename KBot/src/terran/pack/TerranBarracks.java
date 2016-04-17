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

public class TerranBarracks extends BaseClass implements IBuilding {
	private UnitType _buildingType = UnitType.Terran_Supply_Depot;
	
	private static TerranBarracks _instance;
	
	public static TerranBarracks getInstance(){
		if(_instance == null){
			_instance = new TerranBarracks();
		}
		
		return _instance;
	}
	
	private TerranBarracks(){
	}
	
	@Override
	public boolean shouldBuild() {
		if(BuilderSupplyCoordinator.getInstance().getUsableSupply() < Requirements.MINIMUM_SUPPLY_DEPOTS_FOR_BRRACKS)
			return true;
		
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
