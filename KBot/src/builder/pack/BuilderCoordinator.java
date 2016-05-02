package builder.pack;

import java.util.List;
import java.util.Random;

import base.BaseClass;
import base.TimeManager;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import listUtils.pack.ListUtils;
import listUtils.pack.RandomProvider;

public class BuilderCoordinator extends BaseClass {
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
	
	public void runCoordinator(){
		//Get available builder
		List<Unit> scvList = ListUtils.getAllUnitsByType(UnitType.Terran_SCV, _self);
		Building nextBuilding = BuildOrder.getInstance().peekNextBuilding();

		if(BuildOrder.getInstance().canNextBuildingBeBuilt()) {
			Building building =  BuildOrder.getInstance()
					.getNextBuilding();

			if(building != null ) {
				if (building.isSpecialBuilding()) {
					TilePosition tile = BuildLogicsCoordinator.getInstance().determineSpecialBuildPosition(building.get_unitType());

					if (tile != null) {
						Unit idleScv = ListUtils.getFirstIdleUnit(scvList);

						if (idleScv == null)
							idleScv = ListUtils.getFirstUnit(scvList);

						idleScv.build(building.get_unitType(), tile);
					}
				} else {
					TilePosition tile = BuildLogicsCoordinator.getInstance().determineBuildPosition(building.get_unitType());

					if(tile != null){

					}
				}
			}
		}

//		if(!scvList.isEmpty() && nextBuilding != null){
//
//			if(nextBuilding.get_unitType() == UnitType.Terran_Refinery && _self.minerals() > nextBuilding.get_unitType().mineralPrice()){
//				Unit idleScv = ListUtils.getFirstIdleUnit(scvList);
//				if(idleScv == null)
//					idleScv = ListUtils.getFirstUnit(scvList);
//
//				if(idleScv == null)
//					return;
//
//				Unit extractor = ListUtils.getClosestUnit(_game.getNeutralUnits(), idleScv, UnitType.Resource_Vespene_Geyser);
//
//				idleScv.build(UnitType.Terran_Refinery, extractor.getTilePosition());
//			}
//
//			if(_self.supplyUsed(Race.Terran) > _self.supplyTotal() - _supplyIndexValue && _self.minerals() > nextBuilding.get_unitType().mineralPrice()){
//				Unit idleScv = ListUtils.getFirstIdleUnit(scvList);
//				if(idleScv == null)
//					idleScv = ListUtils.getFirstUnit(scvList);
//
//				if(idleScv == null)
//					return;
//
//				Unit commandCenter = ListUtils.getFirstBuilding(_self.getUnits(), UnitType.Terran_Command_Center);
//
//				int randomDistanceX = RandomProvider.randInt(-20,20);
//				int randomDistanceY = RandomProvider.randInt(-20,20);
//
//
//				TilePosition position = new TilePosition(commandCenter.getInitialTilePosition().getX() + randomDistanceX
//						, commandCenter.getInitialTilePosition().getY() + randomDistanceY);
//
//				if(idleScv.canBuild(nextBuilding.get_unitType() , position)){
//					idleScv.build(nextBuilding.get_unitType(), position);
//					nextBuilding.set_isBuilt(true);
//				}
//			}
//		}
	}

	public void runCoordinator2(){
		
	}
}
