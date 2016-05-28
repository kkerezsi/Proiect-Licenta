package action.pack;

import builder.pack.BuildLogicsCoordinator;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Requirements;
import constants.pack.Signatures;

/**
 * Created by Alex on 5/1/2016.
 */
public class BuildAction extends Action{

    private UnitType buildingTypeNeeded;
    private TilePosition position;
    private Boolean isConstructionFinished;
    private boolean actionStarted;
    public BuildAction(Unit unit, UnitType buildingTypeNeeded, TilePosition position , Action nextAction){
        super(unit,nextAction);

        this.buildingTypeNeeded = buildingTypeNeeded;
        this.position = position;
        this.signature = Signatures.BUILD_ACTION_SIG;

        this.isConstructionFinished = null;
        this.actionStarted = false;
    }

    @Override
    public void executeActions() {
        if(isConstructionFinished != null && isConstructionFinished == false) {
            if(!unit.isConstructing()) {
                isConstructionFinished = true;
                this.setActionExecuted(true);
            }
        }
        else {
            if (isPreconditionPassed() && isConditionPassed()) {
                if (!unit.isConstructing() && !actionStarted) {
                    this.unit.build(buildingTypeNeeded, position);
                    this.actionStarted = true;
                }else {
                    isConstructionFinished = false;
                }
            }
        }
    }

    @Override
    public boolean isConditionPassed() {
        if(isActionExecuted())
            return true;

        if(!BuildLogicsCoordinator.getInstance().canBuildAt(position,buildingTypeNeeded)){
            this.position = BuildLogicsCoordinator.getInstance().getLegitTileToBuildNear(buildingTypeNeeded, position, 6, Requirements.MAX_SEARCH_RANGE);

            return false;
        }

        if(unit.isConstructing())
            return false;

        return  true;
    }

    public UnitType getUnitType(){
        return this.buildingTypeNeeded;
    }
}
