package action.pack;

import builder.pack.BuildLogicsCoordinator;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;
import constants.pack.Requirements;
import constants.pack.Signatures;
import listUtils.pack.Painter;

/**
 * Created by Alex on 5/1/2016.
 */
public class BuildAction extends Action{

    private UnitType buildingTypeNeeded;
    private TilePosition position;
    private Boolean isConstructionFinished;
    private boolean actionStarted;
    private boolean isSpecialBuilding;

    public BuildAction(Unit unit, UnitType buildingTypeNeeded, TilePosition position , Action nextAction, boolean isSpecialB){
        super(unit,nextAction);

        this.buildingTypeNeeded = buildingTypeNeeded;
        this.position = position;
        this.signature = Signatures.BUILD_ACTION_SIG;

        this.isConstructionFinished = null;
        this.actionStarted = false;
        this.isSpecialBuilding = isSpecialB;
    }

    // if construction has not started, we check for pre and post conditions
    // in order to create a reliable real time system of data checking
    // After the building has been built, mark action as executed
    @Override
    public void executeActions() {
        if(isConstructionFinished != null && isConstructionFinished == false) {
            if(!unit.isConstructing() && !unit.isMoving()) {
                isConstructionFinished = true;
                this.setActionExecuted(true);
            }
        }
        else {
            if (isPreconditionPassed() && isConditionPassed()) {
                if (!unit.isConstructing() && !actionStarted) {
                    if(this.unit.build(buildingTypeNeeded, position))
                        this.actionStarted = true;
                    else
                        ActionQueue.getInstance().getActionQueue().remove(this);
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

        if(!isSpecialBuilding && !BuildLogicsCoordinator.getInstance().canBuildAt(position,buildingTypeNeeded)){
            this.position = BuildLogicsCoordinator.getInstance().getLegitTileToBuildNear(buildingTypeNeeded, position, 6, Requirements.MAX_SEARCH_RANGE);

            return false;
        }

        if(unit.isMoving())
            return false;

        if(unit.isConstructing())
            return false;

        return  true;
    }

    public UnitType getUnitType(){
        return this.buildingTypeNeeded;
    }
}
