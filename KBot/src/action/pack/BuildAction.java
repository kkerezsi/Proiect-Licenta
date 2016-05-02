package action.pack;

import builder.pack.BuildLogicsCoordinator;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Alex on 5/1/2016.
 */
public class BuildAction extends Action{

    private UnitType buildingTypeNeeded;
    private TilePosition position;

    public BuildAction(Unit unit, UnitType buildingTypeNeeded, TilePosition position , Action nextAction){
        super(unit,nextAction);

        this.buildingTypeNeeded = buildingTypeNeeded;
        this.position = position;
    }

    @Override
    public void executeActions() {
        if(isPreconditionPassed() && isConditionPassed()){
            this.unit.build(buildingTypeNeeded,position);
            this.setActionExecuted(true);
        }
    }

    @Override
    public boolean isConditionPassed() {
        if(isActionExecuted())
            return true;

        if(!BuildLogicsCoordinator.getInstance().canBuildAt(position,buildingTypeNeeded)){
            return  false;
        }

        return  true;
    }
}
