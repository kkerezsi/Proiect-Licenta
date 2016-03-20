package builder.pack;
import java.util.ArrayList;

import base.Tuple;
import bwapi.UnitType;

public class BuildOrder {

	private static ArrayList<Building> _buildOrders;
	
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

	public ArrayList<Building> getBuildingOrder(){
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
	
	public void initialize(){
		//read from file
		_buildOrders = new ArrayList<Building>();
		
		_buildOrders.add(new Building(UnitType.Terran_Supply_Depot, false, 1, 00));
		_buildOrders.add(new Building(UnitType.Terran_Refinery, false, 1, 50));
		_buildOrders.add(new Building(UnitType.Terran_Supply_Depot, false, 2, 10));
		_buildOrders.add(new Building(UnitType.Terran_Barracks, false, 2, 50));
	}
}
