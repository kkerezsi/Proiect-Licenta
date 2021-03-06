package builder.pack;

import java.util.ArrayList;
import java.util.List;

import ListUtils.ListUtils;
import bwapi.Game;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import unit.pack.Worker;

public class BuilderSupplyCoordinator {
	private static BuilderSupplyCoordinator _instance;
	private List<Integer> _currentNrOfWorkers;
	
	private BuilderSupplyCoordinator(){
		_currentNrOfWorkers = new ArrayList<Integer>();
		_currentNrOfWorkers.add(0);
	}
	
	public static BuilderSupplyCoordinator getInstance(){
		if(_instance == null){
			_instance = new BuilderSupplyCoordinator();
		}
		
		return _instance;
	}
	
	public void runCoordinator(Game game,Player self){
		List<Unit> supplyBuilders = ListUtils.getAllIdleUnitsByType(UnitType.Terran_Command_Center, self.getUnits());
		
		for (int i = 0; i < supplyBuilders.size(); i++) {
			if(Worker.getInstance().areMinerWorkersRequired() || Worker.getInstance().areGasWorkersRequired()){
				supplyBuilders.get(i).train(UnitType.Terran_SCV);
			}
		}
	}
}
