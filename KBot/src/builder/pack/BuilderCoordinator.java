package builder.pack;

import java.util.List;

import action.pack.ActionQueue;
import action.pack.BuildAction;
import action.pack.MoveAction;
import base.BaseClass;
import bwapi.*;
import listUtils.pack.ListUtils;
import listUtils.pack.Painter;
import unit.pack.WorkerCoordinator;

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
		Building nextBuilding = BuildOrder.getInstance().peekNextBuilding();

		if(BuildOrder.getInstance().canNextBuildingBeBuilt()) {

			// gat random base
			Unit scv = WorkerCoordinator.getInstance().getAvailableBuilder(0);
			Building building = nextBuilding;

			if(scv != null) {
				if (!building.isBuilding() && building.getBuilt())
					BuildOrder.getInstance().getBuildingOrder();
				else if (building != null && !building.isBuilding()) {
					if (building.isSpecialBuilding()) {
						TilePosition tile = BuildLogicsCoordinator.getInstance().determineSpecialBuildPosition(building.getUnitType());

						if (tile != null) {
							scv.build(building.getUnitType(), tile);
						}
					} else {
						Painter.getInstance().clearTiles();
						TilePosition tile = BuildLogicsCoordinator.getInstance().determineBuildPosition(building.getUnitType());

						if (tile != null) {
							ActionQueue.getInstance().enqueueAction(
									new BuildAction(scv, building.getUnitType(), tile, null));
						}
					}
				}
			}
		}
	}
}
