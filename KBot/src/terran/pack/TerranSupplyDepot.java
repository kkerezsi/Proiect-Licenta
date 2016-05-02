package terran.pack;

import java.util.List;

import base.BaseClass;
import builder.pack.BuildOrder;
import builder.pack.BuilderSupplyCoordinator;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Requirements;
import contracts.pack.IBuilding;
import listUtils.pack.BuildUtils;
import listUtils.pack.ListUtils;

public class TerranSupplyDepot extends BaseClass implements IBuilding {
	private UnitType _buildingType = UnitType.Terran_Supply_Depot;
	
	private static TerranSupplyDepot _instance;
	
	public static TerranSupplyDepot getInstance(){
		if(_instance == null){
			_instance = new TerranSupplyDepot();
		}
		
		return _instance;
	}
	
	private TerranSupplyDepot(){
	}
	
	@Override
	public boolean shouldBuild() {
		int usableSupply = BuilderSupplyCoordinator.getInstance().getUsableSupply();
		int usedSupply = BuilderSupplyCoordinator.getInstance().getSupplyUsed();

		if( usedSupply >= usableSupply - Requirements.ALERT_SUPPLY_ZONE)
			return true;
		
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
