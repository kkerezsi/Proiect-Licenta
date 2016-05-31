package attack.pack;

import base.BaseClass;
import bwapi.Unit;
import constants.pack.Requirements;
import listUtils.pack.ListUtils;

import java.util.List;

public class AttackStrategyCoordinator extends BaseClass {
	private static AttackStrategyCoordinator _instance;

	private List<Unit> enemyMasterBase = null;
	private boolean attackLaunched = false;
	private Unit enemyTarget;

	private AttackStrategyCoordinator(){
	}
	
	public static AttackStrategyCoordinator getInstance(){
		if(_instance == null){
			_instance = new AttackStrategyCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(){
		List<Unit> myArmy = AttackCoordinator.getInstance().getArmy();

		attackLaunched = !(myArmy.size() >= Requirements.MIN_ARMY_ATTACK_SIZE
				&& (enemyTarget == null || (enemyTarget != null && !enemyTarget.exists()) || myArmy.get(0).isIdle()));

		if(!attackLaunched && ( myArmy.size() >= Requirements.MIN_ARMY_ATTACK_SIZE)){
			enemyTarget = !attackLaunched ? ListUtils.getClosestNonBuildingUnits( myArmy.get(0) ,_game.enemy().getUnits()): enemyTarget;

			if(enemyTarget != null && enemyTarget.exists()){
				attackLaunched = true;

				for (Unit u:
					 myArmy) {
					u.attack(enemyTarget);
				}
			}
			else{
				enemyTarget = ListUtils.getClosestNonBuildingUnits(myArmy.get(0),_game.enemy().getUnits());
			}
		}
		else if(attackLaunched && ( myArmy.size() >= 0 )){
			if(enemyTarget != null && !enemyTarget.exists())
				attackLaunched = false;
		}
	}

	public List<Unit> getEnemyMasterBase() {
		return enemyMasterBase;
	}

	public void setEnemyMasterBase(List<Unit> enemyMasterBase) {
		this.enemyMasterBase = enemyMasterBase;
	}
}
