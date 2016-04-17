package resource.pack;

public class CompleteResourceModel {

	private int _gas;
	private int _minerals;

	public CompleteResourceModel(int gas, int minerals){
		setGas(gas);
		setMinerals(minerals);
	}

	public int getGas() {
		return _gas;
	}

	public void setGas(int _gas) {
		this._gas = _gas;
	}

	public int getMinerals() {
		return _minerals;
	}

	public void setMinerals(int _minerals) {
		this._minerals = _minerals;
	}
}
