package action.pack;

import bwapi.Unit;

/**
 * Created by Alex on 5/1/2016.
 */
public abstract class Action {

    protected Unit unit;
    protected Action nextAction;
    private boolean actionExecuted;
    protected int signature;

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
        if(hasNextAction() && nextAction.isActionExecuted())
            return true;
        else if(hasNextAction() && !nextAction.isActionExecuted()){
            nextAction.executeActions();

            return false;
        }

        return  true;
    }

    public Unit getActionUnit(){
        return  this.unit;
    }

    public Action getNextAction(){
        return this.nextAction;
    }

    public int getSignature(){
        return this.signature;
    }
}
