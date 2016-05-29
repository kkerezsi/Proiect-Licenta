package action.pack;

import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import constants.pack.Signatures;
import resource.pack.ResourceCoordinator;
import sun.net.ResourceManager;

/**
 * Created by Alex on 5/1/2016.
 */
public class TrainAction extends Action{

    private UnitType type;
    private Boolean trainingBeginned;

    public TrainAction(Unit unit, UnitType type, Action nextAction){
        super(unit,nextAction);

        this.type = type;
        this.signature = Signatures.TRAINING_SIGNATURE;
        this.trainingBeginned = false;
    }

    @Override
    public void executeActions() {
        if(trainingBeginned != null){
            if (!unit.isTraining()) {
                trainingBeginned = false;
                setActionExecuted(true);
            }
        }
        else if(isPreconditionPassed() && isConditionPassed()){
            unit.train(type);

            trainingBeginned = true;
            this.setActionExecuted(true);
        }
    }

    @Override
    public boolean isConditionPassed() {
        if(this.isActionExecuted())
            return true;

        if(unit.isTraining())
            return false;

        if(!ResourceCoordinator.getInstance().canAffoard(unit.getType()))
            return false;

        return true;
    }
}
