package terran.pack;

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

import java.util.List;

public class TerranAcademy extends BaseClass implements IBuilding {
	private UnitType _buildingType = UnitType.Terran_Academy;

	private static TerranAcademy _instance;
	private int count = 0;

	public static TerranAcademy getInstance(){
		if(_instance == null){
			_instance = new TerranAcademy();
		}

		return _instance;
	}

	private TerranAcademy(){
		count = 0;
	}
	
	@Override
	public boolean shouldBuild() {
        int nrOfAcademies = getCount();

		if(isAlreadyQueued()){
			return false;
		}

        if(nrOfAcademies == Requirements.MAX_MIN_NR_OF_ACADEMIES)
            return false;

		if(getBuildingsOfThisTypeNotCompleted().size() > 0){
			return false;
		}

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
			if(((BuildAction)a).getUnitType() == _buildingType){
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

	@Override
	public void updateCount() {
		count = getNumberOfThisType();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
