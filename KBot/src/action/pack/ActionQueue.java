package action.pack;

import base.BaseClass;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Alex on 5/4/2016.
 */
public class ActionQueue extends BaseClass {

    private static ActionQueue _instance;

    private Queue<Action> actionQueue;
    private ActionQueue(){
        this.setActionQueue(new LinkedList<Action>());
    }

    public static ActionQueue getInstance(){
        if(_instance == null){
            _instance = new ActionQueue();
        }

        return _instance;
    }

    public Queue<Action> getActionQueue() {
        return actionQueue;
    }

    public void setActionQueue(Queue<Action> actionQueue) {
        this.actionQueue = actionQueue;
    }

    public void enqueueAction(Action action){
        for (Action a: getActionQueue()
             ) {
            if(a.getSignature() == action.getSignature())
                return;
        }

        this.getActionQueue().add(action);
    }

    public Action dequeueAction(){
        return this.getActionQueue().remove();
    }

    public Action peekAction(){
        return this.getActionQueue().peek();
    }

    public List<Action> getActionsWithSignature(int signature){
        List<Action> actionList = new ArrayList<>();
        for (Action a:
             this.getActionQueue()) {
            if(a.getSignature() == signature){
                actionList.add(a);
            }
        }

        return actionList;
    }

    public void executeActions(){
        List<Action> remaining = new ArrayList<>();

        for (Action action:
             this.actionQueue) {
            if(action.isActionExecuted())
                remaining.add(action);
            else
                action.executeActions();
        }

        actionQueue.removeAll(remaining);
    }
}
