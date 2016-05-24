package builder.pack;
import bwapi.UnitType;

public class Building {
	
	private UnitType unitType;
	private boolean isBuilt;
	private boolean isBuilding;

	public Building(UnitType unitType,boolean isBuilt){
		this.unitType = unitType;
		this.isBuilt = isBuilt;
		isBuilding = false;
	}
	
	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public boolean getBuilt() {
		return isBuilt;
	}

	public void setBuilt(boolean built) {
		this.isBuilt = built;
	}

	public boolean isSpecialBuilding() {
		return getUnitType() == UnitType.Terran_Refinery
				|| getUnitType() == UnitType.Terran_Bunker
				|| getUnitType() == UnitType.Terran_Command_Center;
	}

	public boolean isBuilding() {
		return isBuilding;
	}

	public void setBuilding(boolean building) {
		isBuilding = building;
	}
}
