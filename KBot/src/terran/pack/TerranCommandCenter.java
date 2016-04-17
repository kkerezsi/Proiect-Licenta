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

public class TerranCommandCenter extends BaseClass implements IBuilding {
	private UnitType _buildingType = UnitType.Terran_Command_Center;
	
	private static TerranCommandCenter _instance;
	
	public static TerranCommandCenter getInstance(){
		if(_instance == null){
			_instance = new TerranCommandCenter();
		}
		
		return _instance;
	}
	
	private TerranCommandCenter(){
	}
	
	@Override
	public boolean shouldBuild() {
		return false;
	}

	@Override
	public boolean canBuild() {
		return false;
	}

	@Override
	public void forceBuild() {
		//BuildOrder.getInstance().cacheBuild(_buildingType);
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
