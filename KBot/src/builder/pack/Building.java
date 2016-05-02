package builder.pack;
import bwapi.UnitType;

public class Building {
	
	private UnitType _unitType;
	private boolean _isBuilt;

	public Building(UnitType unitType,boolean isBuilt){
		this._unitType = unitType;
		this._isBuilt = isBuilt;
	}
	
	public UnitType get_unitType() {
		return _unitType;
	}
	public void set_unitType(UnitType _unitType) {
		this._unitType = _unitType;
	}
	public boolean get_isBuilt() {
		return _isBuilt;
	}
	public void set_isBuilt(boolean _isBuilt) {
		this._isBuilt = _isBuilt;
	}

	public boolean isSpecialBuilding() {
		return get_unitType() == UnitType.Terran_Refinery
				|| get_unitType() == UnitType.Terran_Bunker
				|| get_unitType() == UnitType.Terran_Command_Center;
	}
}
