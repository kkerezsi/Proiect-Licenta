package ListUtils;
import java.util.ArrayList;
import java.util.List;
import bwapi.*;
import bwapi.Unit;
import bwapi.UnitType;

public class ListUtils {

	public static List<Unit> getAllUnitsByType(UnitType type,Player player){
		List<Unit> filteredUnits = new ArrayList<Unit>();
		for (Unit u : player.getUnits()) {
			if(u.getType() == type)
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static List<Unit> getAllUnitsByActionAndType(UnitType type, String action, Player player){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : player.getUnits()) {
			if(u.getType() == type ){
				switch (action) {
				case "gatherMinerals":
					if(u.isGatheringMinerals())
						filteredUnits.add(u);
					break;
				case "gatherGas":
					if(u.isGatheringGas())
						filteredUnits.add(u);
					break;
				case "attacked":
					if(u.isUnderAttack())
						filteredUnits.add(u);
					break;
				default:
					break;
				}
			}
		}
		
		return filteredUnits;
	}
	
	public static List<Unit> getAllUnitsByType(UnitType type,List<Unit> units){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : units) {
			if(u.getType() == type)
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static List<Unit> getAllIdleUnitsByType(UnitType type,Player player){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : player.getUnits()) {
			if(u.getType() == type && u.isIdle())
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static List<Unit> getAllIdleUnitsByType(UnitType type,List<Unit> units){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : units) {
			if(u.getType() == type && u.isIdle())
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static List<Unit> getAllIdleUnitsFromList(List<Unit> units){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : units) {
			if(u.isIdle())
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static Unit getFirstIdleUnit(List<Unit> units){
		for (Unit u : units) {
			if(u.isIdle())
				return u;
		}
		
		return null;
	}
	
	public static Unit getFirstUnit(List<Unit> units){
		if(!units.isEmpty()){
			return units.get(0);
		}
		
		return null;
	}
	
	public static List<Unit> removeDeadUnits(List<Unit> units){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : units) {
			if(u.exists())
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static Unit getFirstBuilding(List<Unit> units, UnitType firstType) {
		for (Unit u : units) {
			if(u.getType() == firstType)
				return u;
		}
		
		return null;
	}

	public static Unit getClosestUnit(List<Unit> units, Unit unit, UnitType unitType) {
		List<Unit> result = getAllUnitsByType(unitType, units);
	    Unit closestUnit = result.size() > 0 ? result.get(0) : null;
	    
	    if(closestUnit == null)
	    	return null;
	    
		for (Unit u : result) {
			if(u.getType() == unitType && u.getDistance(unit) < u.getDistance(closestUnit))
				closestUnit = u;
		}
		
		return closestUnit;
	}
}
