package attack.pack;

import action.pack.ActionQueue;
import action.pack.TrainAction;
import base.BaseClass;
import bwapi.Game;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Requirements;
import listUtils.pack.ListUtils;
import resource.pack.ResourceCoordinator;
import terran.pack.TerranBarracks;
import unit.pack.SupplyCoordinator;
import unit.pack.WorkerCoordinator;

import java.util.ArrayList;
import java.util.List;

public class AttackCoordinator extends BaseClass{
	private static AttackCoordinator _instance;

	private List<Unit> army;
	private AttackCoordinator(){
		army = new ArrayList<>();
	}
	
	public static AttackCoordinator getInstance(){
		if(_instance == null){
			_instance = new AttackCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(){
		if(WorkerCoordinator.getInstance().checkIfNewUnitsAdded()) {
			updateArmy();
		}
		else if(TerranBarracks.getInstance().getCount() > 0){

			List<Unit> availableBarracks = TerranBarracks.getInstance().getBarracks();

			for (Unit brk:
				 availableBarracks) {
				if(brk.canTrain(UnitType.Terran_Marine) && canAffordUnit(UnitType.Terran_Marine) && !brk.isTraining()
				&& SupplyCoordinator.getInstance().getUsableSupply() > Requirements.ALERT_SUPPLY_ZONE){
					brk.train(UnitType.Terran_Marine);
				}
			}
		}
	}

	private void updateArmy() {
		this.army = ListUtils.getAllUnitsByType(UnitType.Terran_Marine,_self);
	}

	private boolean canAffordUnit(UnitType terranMarine) {
		return ResourceCoordinator.getInstance().canAffoard(terranMarine);
	}

	public List<Unit> getArmy() {
		return army;
	}

	public void setArmy(List<Unit> army) {
		this.army = army;
	}
}
