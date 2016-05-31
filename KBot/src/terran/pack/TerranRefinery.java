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
	private int count = 0;

	public static TerranRefinery getInstance(){
		if(_instance == null){
			_instance = new TerranRefinery();
		}
		
		return _instance;
	}
	
	private TerranRefinery(){
		count = 0;
	}
	
	@Override
	public boolean shouldBuild() {
		if(getBuildingsOfThisTypeNotCompleted().size() > 0){
			return false;
		}

		if(BuildOrder.getInstance().isCached(_buildingType)){
			return false;
		}

		int nrOfCommandCenters = TerranCommandCenter.getInstance().getCount();
		int nrOfRefineries = this.getCount();

		if(nrOfCommandCenters <= nrOfRefineries)
			return false;

		int nrOfDepots = TerranSupplyDepot.getInstance().getCount();
		if(nrOfDepots < nrOfCommandCenters)
			return false;

		return true;
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
	public void updateCount() {
		count = getNumberOfThisType();
	}

	@Override
	public List<Unit> getBuildingsOfThisType() {
		return ListUtils.getAllUnitsByType(_buildingType, _self);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
