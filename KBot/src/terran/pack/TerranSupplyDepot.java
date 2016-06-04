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
import unit.pack.WorkerCoordinator;

//Each class is created as a singleton
//They implement BaseClass in order to
//get full access of the game input data
public class TerranSupplyDepot extends BaseClass implements IBuilding {
	private UnitType _buildingType = UnitType.Terran_Supply_Depot;
	
	private static TerranSupplyDepot _instance;

	//Unit Controllers keep an overall track of when a unit is beeing added
	private int count = 0;

	public static TerranSupplyDepot getInstance(){
		if(_instance == null){
			_instance = new TerranSupplyDepot();
		}
		
		return _instance;
	}

	private TerranSupplyDepot(){
		count = 0;
	}
	
	//A primary method for the building units is the should build method
	//The method provides a boolean value weather the deapot is required or not
	@Override
	public boolean shouldBuild() {
		if(BuildOrder.getInstance().isCached(_buildingType)){
			return  false;
		}

		if(getBuildingsOfThisTypeNotCompleted().size() > 0){
			return false;
		}

		if(isAlreadyQueued())
			return false;

		if(getCount() == 1 && TerranBarracks.getInstance().getCount() == 0)
			return false;

		if(getCount() == 0 && WorkerCoordinator.getInstance().getBuilders(0).size() == 1) {
			return true;
		}

		int usableSupply = SupplyCoordinator.getInstance().getUsableSupply();
		if(usableSupply > Requirements.ALERT_SUPPLY_ZONE)
			return false;

		return true;
	}

	//Can build method caches the building and sets an awaiting
	//for a builder worker to pick it up
	@Override
	public boolean canBuild() {
		BuildOrder buildOrderI = BuildOrder.getInstance();

		if (BuildUtils.canGenericBuild(_buildingType)){
			buildOrderI.cacheBuild(_buildingType);

			return true;
		}

		return false;
	}

	//checks if any building action has already been queued
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

	//provides a check for currently building units of this type
	public boolean isBuilding(){
		List<Unit> unitsOfThisType = this.getBuildingsOfThisType();

		for(Unit u : unitsOfThisType){
			if(!u.isCompleted() || u.isBeingConstructed())
				return true;
		}

		return false;
	}

	//force build 
	@Override
	public void forceBuild() {
		BuildOrder.getInstance().cacheBuild(_buildingType);
	}

	//Provides the number of units of this type available for the player
	@Override
	public int getNumberOfThisType() {
		List<Unit> myBuildings = ListUtils.getAllIdleUnitsByType(_buildingType, _self.getUnits());
		
		if(myBuildings != null)
			return myBuildings.size();
		
		return 0;
	}

	//Get buildings that are not completed
	@Override
	public List<Unit> getBuildingsOfThisTypeNotCompleted() {
		return ListUtils.getAllUnitsNotCompleted(_buildingType);
	}

	//Updates the current number of buildings of this type
	@Override
	public void updateCount() {
		count = getNumberOfThisType();
	}

	//Gets the objects of this type 
	@Override
	public List<Unit> getBuildingsOfThisType() {
		return ListUtils.getAllIdleUnitsByType(_buildingType, _self.getUnits());
	}

	//provides the count of my units of this type
	public int getCount() {
		return count;
	}

	//sets the count for the counter
	public void setCount(int count) {
		this.count = count;
	}
}
