package listUtils.pack;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import bwapi.*;

public class ListUtils {
	public static Game _game;
	public static Player _self;
	
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
	
	public static List<Unit> getAllUnitsByType(UnitType type,List<Unit> units, boolean isVisible){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : units) {
			if(u.getType() == type && u.isVisible() == isVisible)
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static List<Unit> getAllIdleUnitsByType(UnitType type,Player player){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : player.getUnits()) {
			if(u.getType() == type && u.isIdle() && u.isCompleted())
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static List<Unit> getAllIdleUnitsByType(UnitType type,List<Unit> units){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : units) {
			if(u.getType() == type && u.isIdle() && u.isCompleted())
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static List<Unit> getAllIdleUnitsFromList(List<Unit> units){
		List<Unit> filteredUnits = new ArrayList<>();
		for (Unit u : units) {
			if(u.isIdle() && u.isCompleted())
				filteredUnits.add(u);
		}
		
		return filteredUnits;
	}
	
	public static Unit getFirstIdleUnit(List<Unit> units){
		for (Unit u : units) {
			if(u.isIdle() && u.isCompleted())
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
		List<Unit> result = getAllUnitsByType(unitType, units, true);
	    
		Unit closestUnit = result.size() > 0 ? result.get(0) : null;
	    
	    if(closestUnit == null)
	    	return null;
	    
		for (Unit u : result) {
			if(u.getType() == unitType && u.getDistance(unit) < u.getDistance(closestUnit))
				closestUnit = u;
		}
		
		return closestUnit;
	}

	public static double getMaxElement(Collection<Double> collection) {
		double max = -9999999;
		for (double number : collection) {
			if (max < number) {
				max = number;
			}
		}
		return max;
	}
	
	public Unit getUnitOfTypeNearestTo(List<Unit> units, UnitType type, int x, int y, boolean allowIncompleted) {
		double nearestDistance = 999999;
		Unit nearestUnit = null;

		for (Unit otherUnit : getUnitsOfType(units, type)) {
			if (!allowIncompleted && !otherUnit.isCompleted()) {
				continue;
			}

			double distance = getDistanceBetween(otherUnit.getPosition().getX(), 
					otherUnit.getPosition().getY(), x, y);
			
			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearestUnit = otherUnit;
			}
		}

		return nearestUnit;
	}
	
	public ArrayList<Unit> getUnitsOfType(List<Unit> units,UnitType unitType) {
		ArrayList<Unit> objectsOfThisType = new ArrayList<Unit>();

		for (Unit unit : units) {
			if (unit.getType() == unitType) {
				objectsOfThisType.add(unit);
			}
		}

		return objectsOfThisType;
	}
	
	public double getDistanceBetween(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) / 32;
	}

	public static int getNumberOfUnitsCompleted(UnitType type) {
		int result = 0;
		for (Unit unit : getAllUnitsByType(type, _self)) {
			if (unit.isCompleted()) {
				result++;
			}
		}
		return result;
	}

	public static int getNumberOfVehicleUnitsCompleted() {
		return getNumberOfUnitsCompleted(UnitType.Terran_Vulture)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Siege_Tank_Siege_Mode)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Siege_Tank_Tank_Mode)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Goliath);
	}

	public static int getNumberOfShipUnitsCompleted() {
		return getNumberOfUnitsCompleted(UnitType.Terran_Dropship)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Wraith)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Valkyrie)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Science_Vessel)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Battlecruiser);
	}
	
	public static int getNumberOfInfantryUnitsCompleted() {
		return getNumberOfUnitsCompleted(UnitType.Terran_Marine)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Firebat)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Ghost)
				+ getNumberOfUnitsCompleted(UnitType.Terran_Medic);
	}

	public static int getNumberOfBattleUnitsCompleted() {
		return getNumberOfInfantryUnitsCompleted() + +getNumberOfVehicleUnitsCompleted()
				+ getNumberOfShipUnitsCompleted();
	}
}
