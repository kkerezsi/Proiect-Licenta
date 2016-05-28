package terran.pack;

import java.util.List;

import base.BaseClass;
import builder.pack.BuildOrder;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Requirements;
import contracts.pack.IBuilding;
import listUtils.pack.BuildUtils;
import listUtils.pack.ListUtils;

public class TerranRefinery extends BaseClass implements IBuilding {
	private UnitType _buildingType = UnitType.Terran_Refinery;
	
	private static TerranRefinery _instance;
	
	public static TerranRefinery getInstance(){
		if(_instance == null){
			_instance = new TerranRefinery();
		}
		
		return _instance;
	}
	
	private TerranRefinery(){
	}
	
	@Override
	public boolean shouldBuild() {
		if(isBuilding()){
			return false;
		}

		List<Unit> refineries = ListUtils.getAllUnitsByType(_buildingType, _self);
		//int battleUnits = ListUtils.getNumberOfBattleUnitsCompleted();
		
		if (refineries != null) {
			int nrOfCommandCenters = TerranCommandCenter.getInstance().getNumberOfThisType();
			int nrOfRefineries = this.getNumberOfThisType();
			boolean isEnoughInfantry =  false; //battleUnits != 0 && (battleUnits >= Requirements.MINIMUM_MARINES);
			boolean isAnotherBaseAndFreeMinerals = nrOfCommandCenters >= 0 &&
					nrOfRefineries < nrOfCommandCenters  ;
			
			return isEnoughInfantry || isAnotherBaseAndFreeMinerals;
		}
		
		return false;
	}

	public boolean isBuilding(){
		List<Unit> unitsOfThisType = this.getBuildingsOfThisType();

		for(Unit u : unitsOfThisType){
			if(!u.isCompleted())
				return true;
		}

		return false;
	}

	@Override
	public boolean canBuild() {
		if (BuildUtils.canGenericBuild(_buildingType)){
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
		List<Unit> myBuildings = ListUtils.getAllUnitsByType(_buildingType, _self);
		
		if(myBuildings != null)
			return myBuildings.size();
		
		return 0;
	}

	@Override
	public List<Unit> getBuildingsOfThisTypeNotCompleted() {
		return ListUtils.getAllUnitsNotCompleted(_buildingType);
	}

	@Override
	public List<Unit> getBuildingsOfThisType() {
		return ListUtils.getAllUnitsByType(_buildingType, _self);
	}
}
