package terran.pack;

import java.util.List;

import base.BaseClass;
import builder.pack.BuildOrder;
import bwapi.Unit;
import bwapi.UnitType;
import contracts.pack.IBuilding;
import listUtils.pack.BuildUtils;
import listUtils.pack.ListUtils;

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
	public List<Unit> getBuildingsOfThisTypeNotCompleted() {
		return ListUtils.getAllUnitsNotCompleted(_buildingType);
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
	public List<Unit> getBuildingsOfThisType() {
		return ListUtils.getAllUnitsByType(_buildingType, _self);
	}
}
