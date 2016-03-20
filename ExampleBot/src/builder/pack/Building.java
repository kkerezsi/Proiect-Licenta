package builder.pack;
import bwapi.UnitType;

public class Building {
	
	private UnitType _unitType;
	private boolean _isBuilt;
	private int _minutes;
	private int _seconds;
	
	public Building(UnitType unitType,boolean isBuilt,int minutes,int seconds){
		this._unitType = unitType;
		this._isBuilt = isBuilt;
		this._minutes = minutes;
		this._seconds = seconds;
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
	public int get_minutes() {
		return _minutes;
	}
	public void set_minutes(int _minutes) {
		this._minutes = _minutes;
	}
	public int get_seconds() {
		return _seconds;
	}
	public void set_seconds(int _seconds) {
		this._seconds = _seconds;
	}
}
