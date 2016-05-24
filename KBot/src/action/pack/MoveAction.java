package action.pack;

import builder.pack.BuildLogicsCoordinator;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Signatures;

/**
 * Created by Alex on 5/1/2016.
 */
public class MoveAction extends Action{

    private Position position;

    public MoveAction(Unit unit, Position position, Action nextAction){
        super(unit,nextAction);

        this.position = position;
        this.signature = Signatures.MOVE_ACTION_SIG;
    }

    @Override
    public void executeActions() {
        if(isPreconditionPassed() && isConditionPassed()){
            this.setActionExecuted(true);
        }
    }

    @Override
    public boolean isConditionPassed() {
        if(isActionExecuted())
            return true;

        unit.move(position);

        return true;
    }
}
