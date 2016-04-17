package builder.pack;
import java.util.ArrayList;
import java.util.Stack;

import base.Tuple;
import bwapi.UnitType;

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
	
	public Building getNextItemToBuild(){
		for (Building building : _buildOrders) {
			if(building.get_isBuilt() == false){
				return building;
			}
		}
		
		return null;
	}
	
	public Building getNextItemToBuild(Tuple<Long,Long> time){
		for (Building building : _buildOrders) {
			if(building.get_isBuilt() == false && time.x >= building.get_minutes() && time.y >= building.get_seconds()){
				return building;
			}
		}
		
		return null;
	}
	
	public void cacheBuild(UnitType unitType){
		_buildOrders.add(new Building(unitType, false, 0, 0));
	}
	
	public Building previewNextBuild(){
		if(!_buildOrders.isEmpty())
			return _buildOrders.peek();
		
		return null;
	}
	
	public Building getNextBuild(){
		if(!_buildOrders.isEmpty())
			return _buildOrders.pop();
		
		return null;
	}
	
	private void initialize(){
		//read from file
		_buildOrders = new Stack<Building>();
	}
}
