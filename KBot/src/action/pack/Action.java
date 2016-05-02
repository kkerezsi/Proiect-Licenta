package action.pack;

import bwapi.Unit;

/**
 * Created by Alex on 5/1/2016.
 */
public abstract class Action {

    protected Unit unit;
    protected Action nextAction;
    private boolean actionExecuted;

    public Action(Unit unit, Action nextAction){
        this.unit = unit;
        this.nextAction = nextAction;
    }

    public abstract void executeActions();
    public abstract boolean isConditionPassed();

    public boolean hasNextAction(){
        return nextAction != null;
    }

    public boolean isActionExecuted() {
        return actionExecuted;
    }

    public void setActionExecuted(boolean actionExecuted) {
        this.actionExecuted = actionExecuted;
    }

    public boolean isPreconditionPassed(){
        if(hasNextAction() && !nextAction.isActionExecuted()){
            executeActions();
            if(!nextAction.isActionExecuted())
                return false;
        }

        return  true;
    }
}
