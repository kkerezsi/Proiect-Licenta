package terran.pack;

import java.util.List;

import action.pack.Action;
import action.pack.ActionQueue;
import action.pack.BuildAction;
import base.BaseClass;
import builder.pack.BuildOrder;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Requirements;
import constants.pack.Signatures;
import contracts.pack.IBuilding;
import listUtils.pack.BuildUtils;
import listUtils.pack.ListUtils;

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
        int nrOfBarracks = getNumberOfThisType();

		if(isBuilding()){
			return  false;
		}

		if(isAlreadyQueued()){
			return false;
		}

		if(TerranSupplyDepot.getInstance().getNumberOfThisType() < Requirements.MINIMUM_SUPPLY_DEPOTS_FOR_BARRACKS)
			return false;

        if(nrOfBarracks >= Requirements.MAX_NR_OF_BARRACKS)
            return false;

        if(nrOfBarracks >= determineFibonacciReport(TerranCommandCenter.getInstance().getNumberOfThisType()))
            return false;

		return true;
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

	public boolean isAlreadyQueued(){
		List<Action> buildActions = ActionQueue.getInstance().getActionsWithSignature(Signatures.BUILD_ACTION_SIG);
		for (Action a :
				buildActions) {
			if(((BuildAction)a).getUnitType() == UnitType.Terran_Barracks){
				return true;
			}
		}

		return  false;
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
	public List<Unit> getBuildingsOfThisType() {
		return ListUtils.getAllUnitsByType(_buildingType, _self);
	}

	@Override
	public List<Unit> getBuildingsOfThisTypeNotCompleted() {
		return ListUtils.getAllUnitsNotCompleted(_buildingType);
	}

	private int determineFibonacciReport(int nrOfCommandCenters){
        switch (nrOfCommandCenters){
            case 1: return 2;
            case 2: return 3;
            case 3: return 5;
            case 4: return 8;
            default: return 0;
        }
    }
}
