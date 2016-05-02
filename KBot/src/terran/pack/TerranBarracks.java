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

	@Override
	public List<Unit> getBuildingsOfThisType() {
		return ListUtils.getAllUnitsByType(_buildingType, _self);
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
