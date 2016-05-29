package resource.pack;

import base.BaseClass;
import bwapi.Game;
import bwapi.Player;
import bwapi.UnitType;

public class ResourceCoordinator extends BaseClass {
	private static ResourceCoordinator _instance;
	
	private ResourceCoordinator(){
	}
	
	public static ResourceCoordinator getInstance(){
		if(_instance == null){
			_instance = new ResourceCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
	}

	public CompleteResourceModel getRequirementsForType(UnitType _buildingType) {
		
		CompleteResourceModel resourceModel = new CompleteResourceModel(_buildingType.mineralPrice(), _buildingType.gasPrice());
		
		return resourceModel;
	}
	
	public CompleteResourceModel getMyResources(){
		CompleteResourceModel resourceModel = new CompleteResourceModel(_self.minerals(), _self.gas());
		
		return resourceModel;
	}

	public boolean canAffoard(UnitType unit){
		CompleteResourceModel res = this.getMyResources();

		if(res.getMinerals() - unit.mineralPrice() >= 0 && res.getGas() - unit.gasPrice() >= 0)
			return true;

		return false;
	}
}
