package terran.pack;

import java.util.List;

import action.pack.Action;
import action.pack.ActionQueue;
import action.pack.BuildAction;
import base.BaseClass;
import builder.pack.BuildOrder;
import unit.pack.SupplyCoordinator;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Requirements;
import constants.pack.Signatures;
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
		if(isAlreadyQueued()){
			return false;
		}

		if(isBuilding()){
			return false;
		}

		int usableSupply = SupplyCoordinator.getInstance().getUsableSupply();

		if(usableSupply <= Requirements.ALERT_SUPPLY_ZONE)
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

	public boolean isAlreadyQueued(){
		List<Action> buildActions = ActionQueue.getInstance().getActionsWithSignature(Signatures.BUILD_ACTION_SIG);
		for (Action a :
				buildActions) {
			if(((BuildAction)a).getUnitType() == _buildingType){
				return true;
			}
		}

		return  false;
	}

	public boolean isBuilding(){
		List<Unit> unitsOfThisType = this.getBuildingsOfThisType();

		for(Unit u : unitsOfThisType){
			if(!u.isCompleted() || u.isBeingConstructed())
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
	public List<Unit> getBuildingsOfThisTypeNotCompleted() {
		return ListUtils.getAllUnitsNotCompleted(_buildingType);
	}

	@Override
	public List<Unit> getBuildingsOfThisType() {
		return ListUtils.getAllIdleUnitsByType(_buildingType, _self.getUnits());
	}
}
