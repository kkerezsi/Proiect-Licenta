package builder.pack;
import java.util.Stack;

import bwapi.UnitType;
import listUtils.pack.BuildUtils;

public class BuildOrder {

	private static Stack<Building> _buildOrders;
	
	private static BuildOrder _instance;
	
	public static BuildOrder getInstance(){
		if(_instance == null){
			_instance = new BuildOrder();
		}
		
		return _instance;
	}
	
	private BuildOrder(){
		initialize();
	}

	public Stack<Building> getBuildingOrder(){
		return _buildOrders;
	}
	
	public Building peekNextBuilding(){
        if(_buildOrders.size() == 0)
            return null;

		return _buildOrders.peek();
	}

	public boolean canNextBuildingBeBuilt(){
        if(_buildOrders.size() == 0)
            return false;

		return BuildUtils.canGenericBuild(peekNextBuilding().getUnitType());
	}

	public Building getNextBuilding(){
        if(_buildOrders.size() == 0)
            return null;

		return _buildOrders.pop();
	}

	public void cacheBuild(UnitType unitType){
		_buildOrders.add(new Building(unitType, false));
	}
	
	private void initialize(){
		//read from file
		_buildOrders = new Stack<>();
	}
}
