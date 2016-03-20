package builder.pack;

import java.util.List;
import java.util.Random;

import ListUtils.ListUtils;
import base.TimeManager;
import bwapi.Game;
import bwapi.Player;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class BuilderCoordinator {
	private static BuilderCoordinator _instance;
	private static int _supplyIndexValue = 3;
	
	private BuilderCoordinator(){
	}
	
	public static BuilderCoordinator getInstance(){
		if(_instance == null){
			_instance = new BuilderCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player player){
		//Get available builder
		List<Unit> scvList = ListUtils.getAllUnitsByType(UnitType.Terran_SCV, player);
		Building nextBuilding = BuildOrder.getInstance()
				.getNextItemToBuild(TimeManager.getInstance().getTimeDifference(game));

		if(!scvList.isEmpty() && nextBuilding != null){			

			if(nextBuilding.get_unitType() == UnitType.Terran_Refinery && player.minerals() > nextBuilding.get_unitType().mineralPrice()){
				Unit idleScv = ListUtils.getFirstIdleUnit(scvList);
				if(idleScv == null)
					idleScv = ListUtils.getFirstUnit(scvList);
				
				if(idleScv == null)
					return;
				
				Unit extractor = ListUtils.getClosestUnit(game.getNeutralUnits(), idleScv, UnitType.Resource_Vespene_Geyser);
				
				idleScv.build(UnitType.Terran_Refinery, extractor.getTilePosition());
			}
			
			if(player.supplyUsed(Race.Terran) > player.supplyTotal() - _supplyIndexValue && player.minerals() > nextBuilding.get_unitType().mineralPrice()){
				Unit idleScv = ListUtils.getFirstIdleUnit(scvList);
				if(idleScv == null)
					idleScv = ListUtils.getFirstUnit(scvList);
				
				if(idleScv == null)
					return;
				
				Unit commandCenter = ListUtils.getFirstBuilding(player.getUnits(), UnitType.Terran_Command_Center);
				
				int randomDistanceX = randInt(-20,20);
				int randomDistanceY = randInt(-20,20);

				
				TilePosition position = new TilePosition(commandCenter.getInitialTilePosition().getX() + randomDistanceX
						, commandCenter.getInitialTilePosition().getY() + randomDistanceY);
				
				if(idleScv.canBuild(nextBuilding.get_unitType() , position)){
					idleScv.build(nextBuilding.get_unitType(), position);
					nextBuilding.set_isBuilt(true);					
				}
			}
		}
	}
	
	public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
}
