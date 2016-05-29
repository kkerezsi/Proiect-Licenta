package action.pack;

import base.Tuple;
import bwapi.Position;
import bwapi.Unit;
import bwta.BaseLocation;
import constants.pack.Signatures;

/**
 * Created by Alex on 5/29/2016.
 */
public class ScoutingAction extends Action {

    public Tuple<BaseLocation,Boolean> location;
    public Boolean isMoving;
    public boolean hasReachedPoint;

    public  ScoutingAction(Unit unit, Tuple<BaseLocation,Boolean> loc){
        super(unit, null);
        this.signature = Signatures.SCOUTING_SIGNATURE;

        location = loc;
        isMoving = null;
        hasReachedPoint = false;
    }

    @Override
    public void executeActions() {
        if(isActionExecuted())
            return;

        if(isMoving == null) {
            unit.move(location.x.getPosition());
            isMoving = true;
        }
        else if (isPreconditionPassed() && isConditionPassed()) {
            isMoving = false;
            hasReachedPoint = true;
            location.y = true;
            setActionExecuted(true);
        }
    }

    @Override
    public boolean isConditionPassed() {
        if(isActionExecuted()) {
            return true;
        }

        if(unit.isMoving()) {
            return false;
        }

        if(unit.getDistance(location.x.getPosition()) > 50){
            return false;
        }

        return true;
    }
}
